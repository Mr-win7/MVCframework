package com.demo.mvc;

public interface InterceptorChain
{
	void doInterceptor(Action action) throws Exception;
}
