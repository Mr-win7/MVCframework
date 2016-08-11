package com.demo.mvc;

import java.io.UnsupportedEncodingException;
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
import com.demo.mvc.converter.ConverterFactory;
import com.demo.mvc.renderer.Renderer;
import com.demo.mvc.template.JspTemplateFactory;
import com.demo.mvc.template.TemplateFactory;

public class Dispatcher
{
	private Interceptor[] interceptors;
	private ExceptionHandler exceptionHandler;
	private ServletContext servletContext;
	private ContainerFactory containerFactory;
	private ConverterFactory converterFactory;
	private Map<UrlMatcher, Action> urlMap = new HashMap<UrlMatcher, Action>();

	void init(Config config)
	{
		this.servletContext = config.getServletContext();
		try
		{
			initAll(config);
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	void initTemplateFactory(Config config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		String name = config.getInitParameter("template");
		if (name == null)
		{
			name = JspTemplateFactory.class.getName();
		}

		TemplateFactory templateFactory = (Class.forName(name).newInstance() instanceof TemplateFactory)
				? (TemplateFactory) Class.forName(name).newInstance()
				: (TemplateFactory) Class.forName(TemplateFactory.class.getPackage().getName() + "." + name
						+ TemplateFactory.class.getSimpleName()).newInstance();
		templateFactory.init(config);
		TemplateFactory.setTemplateFactory(templateFactory);
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

	public boolean service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{
		String uri = request.getRequestURI();
		String path = request.getContextPath();
		if (path.length() > 0)
		{
			uri = uri.substring(path.length());
		}
		if (request.getCharacterEncoding() == null)
		{
			request.setCharacterEncoding("UTF-8");
		}
		Execution execution = null;
		for (UrlMatcher urlMatcher : this.urlMap.keySet())
		{
			String[] args = urlMatcher.getMatchedParameters(uri);
			if (args != null)
			{
				Action action = urlMap.get(urlMatcher);
				Object[] arguments = new Object[args.length];
				for (int i = 0; i < arguments.length; i++)
				{
					Class<?> type = action.arguments[i];
					if (type.equals(String.class))
					{
						arguments[i] = args[i];
					}
					else
					{
						arguments[i] = converterFactory.convert(type, args[i]);
					}
				}
				execution = new Execution(request, response, action, arguments);
				break;
			}
		}
		return execution != null;
	}
}
