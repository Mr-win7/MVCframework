package com.demo.mvc;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewResolver
{
	void init(ServletContext context) throws ServletException;

	void resolveView(String view, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws ServletException;
}
