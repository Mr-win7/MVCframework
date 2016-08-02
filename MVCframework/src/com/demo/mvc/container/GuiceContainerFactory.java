package com.demo.mvc.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.demo.mvc.Config;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

public class GuiceContainerFactory implements ContainerFactory
{
	private Injector injector;

	@Override
	public void init(Config config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> findAllBeans()
	{
		Map<Key<?>, Binding<?>> map = injector.getBindings();
		Set<Key<?>> keys = map.keySet();
		List<Object> list = new ArrayList<Object>(keys.size());
		for (Key<?> key : keys)
		{
			Object bean = injector.getInstance(key);
			list.add(bean);
		}
		return list;
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
