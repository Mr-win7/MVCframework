package com.demo.mvc.template;

import com.demo.mvc.Config;

public class JspTemplateFactory extends TemplateFactory
{

	@Override
	public Template loadTemplate(String path) throws Exception
	{
		return new JspTemplate(path);
	}

	public void init(Config config)
	{

	}

}
