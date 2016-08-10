package com.demo.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.mvc.container.ContainerFactory;
import com.demo.mvc.renderer.Renderer;

public class Dispatcher
{
	private Interceptor[] interceptors;
	private ExceptionHandler exceptionHandler;
	private ServletContext servletContext;
	private ContainerFactory containerFactory;
	private Map<UrlMatcher, Action> urlMap = new HashMap<UrlMatcher, Action>();

	void initAll(Config config) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{

		String container = config.getInitParameter("container");
		if (container == null)
		{
			// TODO
			// throw new
		}
		this.containerFactory = (Class.forName(container).newInstance() instanceof ContainerFactory)
				? (ContainerFactory) Class.forName(container).newInstance()
				: (ContainerFactory) Class.forName(ContainerFactory.class.getPackage().getName() + "." + container
						+ ContainerFactory.class.getSimpleName()).newInstance();
		containerFactory.init(config);
		List<Object> beans = containerFactory.findAllBeans();
		initComponents(beans);

		initTemplateFactory(config);
	}

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

	// TODO
	void initTemplateFactory(Config config)
	{

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

	void handleExecution(Execution execution, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionContext.setActionContext(servletContext, request, response);
		try
		{
			InterceptorChainImpl chainImpl = new InterceptorChainImpl(interceptors);
			chainImpl.doInterceptor(execution);
			handleResult(request, response, chainImpl.getResult());
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		finally
		{
			ActionContext.removeActionContext();
		}
	}

	void handleResult(HttpServletRequest request, HttpServletResponse response, Object result) throws Exception
	{
		if (result == null)
		{
			return;
		}
		if (result instanceof Renderer)
		{
			Renderer renderer = (Renderer) result;
			renderer.render(this.servletContext, request, response);
			return;
		}
		if (result instanceof String)
		{
			String string = (String) result;
			if (string.startsWith("redirect:"))
			{
				response.sendRedirect(string.substring(9));
				return;
			}
		}
		// TODO
		throw new ServletException();
	}
}
