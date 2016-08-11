package com.demo.mvc.converter;

public interface Converter<T>
{
	T convert(String s);
}
