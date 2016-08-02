package com.demo.mvc.container;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.demo.mvc.Config;

public class SpringContainerFactory implements ContainerFactory
{
	private ApplicationContext appContext;

	@Override
	public void init(Config config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> findAllBeans()
	{
		String[] beanNames = appContext.getBeanDefinitionNames();
		List<Object> beans = new ArrayList<Object>(beanNames.length);
		for (String bean : beanNames)
		{
			beans.add(appContext.getBean(bean));
		}
		return beans;
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
