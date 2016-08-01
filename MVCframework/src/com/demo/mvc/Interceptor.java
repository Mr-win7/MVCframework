package com.demo.mvc;

public interface Interceptor
{
	void intercept(Action action, InterceptorChain chain) throws Exception;
}
