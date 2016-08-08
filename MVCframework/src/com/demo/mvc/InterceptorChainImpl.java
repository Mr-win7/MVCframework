package com.demo.mvc;

public class InterceptorChainImpl implements InterceptorChain
{
	private final Interceptor[] interceptors;
	private int index = 0;
	private Object result = null;

	public InterceptorChainImpl(Interceptor[] interceptors)
	{
		this.interceptors = interceptors;
	}

	@Override
	public void doInterceptor(Execution execution) throws Exception
	{
		if (index == interceptors.length)
		{
			result = execution.execute();
		}
		else
		{
			index++;
			interceptors[index - 1].intercept(execution, this);
		}
	}

	public Object getResult()
	{
		return result;
	}

}
