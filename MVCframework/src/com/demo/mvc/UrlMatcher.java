package com.demo.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlMatcher
{
	Pattern pattern;
	List<String> params;

	public UrlMatcher(String projectName, String className, String uri)
	{
		Pattern pattern = Pattern.compile("(\\{[^/\\\\]*?\\})");
		Matcher matcher = pattern.matcher(uri);
		params = new ArrayList<String>();
		while (matcher.find())
		{
			params.add(matcher.group());
		}
		String patternString = matcher.replaceAll("([^/\\\\]*?)");
		this.pattern = Pattern.compile(projectName + className + patternString);
	}
}
