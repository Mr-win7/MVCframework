package com.demo.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherFilter implements Filter
{
	private Dispatcher dispatcher;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String method = request.getMethod();
		if ("GET".equals(method) || "POST".equals(method))
		{
			if (!dispatcher.service(request, response))
			{
				chain.doFilter(req, resp);
				return;
			}
		}
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

}
