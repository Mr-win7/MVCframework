package com.demo.mvc;

import java.util.regex.Pattern;

public final class UrlMatcher
{
	final String url;
	int[] orders;
	Pattern pattern;

	public UrlMatcher(String url)
	{
		this.url = url;

	}
}
