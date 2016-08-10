package com.demo.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionContext
{
	private static final ThreadLocal<ActionContext> actionContextThreadLocal = new ThreadLocal<ActionContext>();
	private ServletContext context;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public ServletContext getServletContext()
	{
		return context;
	}

	public HttpServletRequest getHttpServletRequest()
	{
		return request;
	}

	public HttpServletResponse getHttpServletResponse()
	{
		return response;
	}

	public HttpSession getHttpSession()
	{
		return request.getSession();
	}

	public static ActionContext getActionContext()
	{
		return actionContextThreadLocal.get();
	}

	static void setActionContext(ServletContext context, HttpServletRequest request, HttpServletResponse response)
	{
		ActionContext actionContext = new ActionContext();
		actionContext.context = context;
		actionContext.request = request;
		actionContext.response = response;
		actionContextThreadLocal.set(actionContext);
	}

	static void removeActionContext()
	{
		actionContextThreadLocal.remove();
	}
}
