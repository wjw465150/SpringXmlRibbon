package org.wjw.springxmlribbon.service;

import org.wjw.springxmlribbon.model.User;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface RemoteService {
	/**
	GET /add?a=1&b=2 HTTP/1.1
	Content-Type: application/json
	Accept: application/json
	User-Agent: Java/1.8.0_121
	Host: 192.168.2.113:8861
	Connection: keep-alive
	*/
	@Headers({ "Content-Type: application/json", "Accept: application/json" })
	@RequestLine("GET /add?a={a}&b={b}")
	int getAdd(@Param("a") int a, @Param("b") int b);
	
	/**
	POST /user HTTP/1.1
	Content-Type: application/json
	Accept: application/json
	User-Agent: Java/1.8.0_121
	Host: 127.0.0.1:8861
	Connection: keep-alive
	Content-Length: 41

	{
	  "id" : 0,
	  "username" : "hello"
	}
	*/
	@Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("POST /user")
    User getOwner(User user);	
}
