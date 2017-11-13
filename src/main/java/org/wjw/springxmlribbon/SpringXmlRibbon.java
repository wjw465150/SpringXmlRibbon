package org.wjw.springxmlribbon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wjw.springxmlribbon.model.User;
import org.wjw.springxmlribbon.service.RemoteService;

public class SpringXmlRibbon {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

		RibbonConfig ribbonConfig = (RibbonConfig) ctx.getBean("ribbonConfig");
		RemoteService service = ribbonConfig.getRemoteService();
		/**
		 * 调用测试
		 */
		User user = new User();
		user.setUsername("hello");
		for (int i = 1; i <= 10; i++) {
			int result = service.getAdd(1, 2);
			System.out.println("result:" + result);

			User userB = service.getOwner(user);
			System.out.println("user:" + userB);
		}

		((ConfigurableApplicationContext) ctx).close();
	}
}
