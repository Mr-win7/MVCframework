package com.demo.mvc.container;

import java.util.List;

import com.demo.mvc.Config;

public interface ContainerFactory
{
	void init(Config config);

	List<Object> findAllBeans();

	void destroy();
}
