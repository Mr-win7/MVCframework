package com.demo.mvc.template;

public abstract class TemplateFactory
{
	private static TemplateFactory instance;

	public static TemplateFactory getTemplateFactory()
	{
		return instance;
	}

	public abstract Template loadTemplate(String path) throws Exception;
}
