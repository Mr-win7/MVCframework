package com.demo.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Execution
{
	public final HttpServletRequest request;
	public final HttpServletResponse response;
	private final Action action;
	private final Object[] args;

	public Execution(HttpServletRequest request, HttpServletResponse response, Action action, Object[] args)
	{
		super();
		this.request = request;
		this.response = response;
		this.action = action;
		this.args = args;
	}

	public Object execute() throws Exception
	{
		return action.method.invoke(action.instance, args);
	}
}
