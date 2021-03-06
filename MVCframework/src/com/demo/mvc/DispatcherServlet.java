package com.demo.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5504716990119848726L;
	private Dispatcher dispatcher;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doGet(req, resp);
		dispatcher.service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
		doGet(req, resp);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		// TODO Auto-generated method stub
		super.init(config);
		this.dispatcher = new Dispatcher();
		this.dispatcher.init(new Config()
		{

			@Override
			public ServletContext getServletContext()
			{
				// TODO Auto-generated method stub
				return config.getServletContext();
			}

			@Override
			public String getInitParameter(String name)
			{
				// TODO Auto-generated method stub
				return config.getInitParameter(name);
			}
		});
	}

}
