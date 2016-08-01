package com.demo.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionContext
{

	private static ThreadLocal<ActionContext> contextThreadLocal = new ThreadLocal<>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private ServletContext context;

	public static ActionContext getActionContext()
	{
		return (ActionContext) contextThreadLocal.get();
	}

	public static void setActionContext(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ServletContext context)
	{
		ActionContext actionContext = new ActionContext();
		actionContext.setRequest(request);

	}

	public void remove()
	{
		contextThreadLocal.remove();
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public HttpSession getSession()
	{
		return session;
	}

	public void setSession(HttpSession session)
	{
		this.session = session;
	}

	public ServletContext getContext()
	{
		return context;
	}

	public void setContext(ServletContext context)
	{
		this.context = context;
	}
}
