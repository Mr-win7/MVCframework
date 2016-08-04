package com.demo.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler
{
	void handle(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception;
}
