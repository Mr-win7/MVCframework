package com.demo.mvc.template;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Template
{
	void render(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception;
}
