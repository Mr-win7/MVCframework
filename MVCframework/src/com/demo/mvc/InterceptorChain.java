package com.demo.mvc;

public interface InterceptorChain
{
	void doInterceptor(Execution execution) throws Exception;
}
