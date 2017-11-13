package org.wjw.springxmlribbon;

import java.util.Properties;

import org.wjw.springxmlribbon.service.RemoteService;

import com.netflix.client.ClientFactory;
import com.netflix.client.config.IClientConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.LBClient;
import feign.ribbon.LBClientFactory;
import feign.ribbon.RibbonClient;

public class RibbonConfig {
	private Properties properties;
	private String clientName;

	public RemoteService getRemoteService() {
		ConfigurationManager.loadProperties(properties);

		RibbonClient client = RibbonClient.builder().lbClientFactory(new LBClientFactory() {
			@Override
			public LBClient create(String clientName) {
				IClientConfig config = ClientFactory.getNamedConfig(clientName);
				ILoadBalancer lb = ClientFactory.getNamedLoadBalancer(clientName);
				ZoneAwareLoadBalancer zb = (ZoneAwareLoadBalancer) lb;
				zb.setRule(new RandomRule());
				return LBClient.create(lb, config);
			}
		}).build();

		/*
		 * 重点看ribbon.properties文件里的service-one.ribbon.listOfServers配置项,
		 * 该配置项指定了服务生产端的真实地址. 与RemoteService接口绑定的URL地址是"http://service-one/",
		 * 在调用时会被替换为http://192.168.2.113:8861/或http://192.168.2.114:8861/,再与接口中@
		 * RequestLine指定的地址进行拼接,得到最终请求地址
		 * 本例中最终请求地址为"http://192.168.2.113:8861/add"或"http://192.168.2.114:8861/add"
		 */
		RemoteService service = Feign.builder().client(client).encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder()).target(RemoteService.class, "http://" + this.clientName + "/");

		return service;
	}

	/**
	 * Ribbon负载均衡策略实现
	 * 使用ZoneAvoidancePredicate和AvailabilityPredicate来判断是否选择某个server，前一个判断判定一个zone的运行性能是否可用，
	 * 剔除不可用的zone（的所有server），AvailabilityPredicate用于过滤掉连接数过多的Server。
	 * 
	 * @return
	 */
	private static IRule zoneAvoidanceRule() {
		return new ZoneAvoidanceRule();
	}

	/**
	 * Ribbon负载均衡策略实现 随机选择一个server。
	 * 
	 * @return
	 */
	private static IRule randomRule() {
		return new RandomRule();
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
