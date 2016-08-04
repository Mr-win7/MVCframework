package com.demo.mvc;

public interface Interceptor
{
	void intercept(Execution execution, InterceptorChain chain) throws Exception;
}
