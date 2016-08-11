package com.demo.mvc.converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory
{
	private Map<Class<?>, Converter<?>> map = new HashMap<Class<?>, Converter<?>>();

	public boolean canConvert(Class<?> clazz)
	{
		return clazz.equals(String.class) || map.containsKey(clazz);
	}

	public Object convert(Class<?> clazz, String string)
	{
		Converter<?> converter = map.get(clazz);
		return converter.convert(string);
	}
}
