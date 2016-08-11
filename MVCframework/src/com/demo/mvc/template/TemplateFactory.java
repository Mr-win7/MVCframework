package com.demo.mvc.template;

import com.demo.mvc.Config;

public abstract class TemplateFactory
{
	private static TemplateFactory instance;

	public static void setTemplateFactory(TemplateFactory templateFactory)
	{
		instance = templateFactory;
	}

	public static TemplateFactory getTemplateFactory()
	{
		return instance;
	}

	public abstract void init(Config config);

	public abstract Template loadTemplate(String path) throws Exception;
}
