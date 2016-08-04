package com.demo.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.demo.mvc.container.ContainerFactory;

public class Dispatcher
{
	private ExceptionHandler exceptionHandler;
	private ServletContext servletContext;
	private ContainerFactory containerFactory;
	private Map<String, Map<String, Map<UrlMatcher, Action>>> map = new HashMap<String, Map<String, Map<UrlMatcher, Action>>>();
	private Map<UrlMatcher, Action> urlMap = new HashMap<UrlMatcher, Action>();

	void initComponents(List<Object> beans)
	{
		List<Interceptor> interceptors = new ArrayList<Interceptor>();
		for (Object bean : beans)
		{
			if (bean instanceof Interceptor)
			{
				interceptors.add((Interceptor) bean);
			}
			else if (bean instanceof ExceptionHandler && this.exceptionHandler == null)
			{
				this.exceptionHandler = (ExceptionHandler) bean;
			}
			else
			{
				addAction(bean);
			}
			if (this.exceptionHandler == null)
			{
				this.exceptionHandler = new DefaultExceptionHandler();
			}
		}
	}

	void addAction(Object bean)
	{
		Class<?> clazz = bean.getClass();
		Mapping mapping = clazz.getAnnotation(Mapping.class);
		String projectName = servletContext.getContextPath();
		String className = null;

		if ((mapping == null) || (mapping.value().length() == 0))
		{
			className = "";
		}
		else
		{
			className = mapping.value();
		}

		if (!map.containsKey(projectName))
		{
			map.put(projectName, new HashMap<String, Map<UrlMatcher, Action>>());
		}
		else if (!map.get(projectName).containsKey(className))
		{
			map.get(projectName).put(className, new HashMap<UrlMatcher, Action>());
		}

		Method[] methods = clazz.getMethods();
		for (Method method : methods)
		{
			Mapping annotation = method.getAnnotation(Mapping.class);

			if (!((annotation == null) || (annotation.value().length() == 0)))
			{
				String url = annotation.value();
				UrlMatcher urlMatcher = new UrlMatcher(projectName, className, url);
				if (urlMatcher.params.size() != method.getParameterCount())
				{
					continue;
				}
				urlMap.put(urlMatcher, new Action(bean, method));
			}
		}
	}

}
