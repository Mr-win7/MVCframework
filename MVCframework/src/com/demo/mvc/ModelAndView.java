package com.demo.mvc;

import java.util.Collections;
import java.util.Map;

public class ModelAndView
{

	private String view;
	private Map<String, Object> model;

	public ModelAndView(String view)
	{
		this.view = view;
		this.model = Collections.emptyMap();
	}

	public ModelAndView(String view, Map<String, Object> model)
	{
		this.view = view;
		this.model = model;
	}

	public String getView()
	{
		return view;
	}

	public Map<String, Object> getModel()
	{
		return model;
	}

}
