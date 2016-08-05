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

	public String[] getMatchedParameters(String url)
	{
		Matcher matcher = pattern.matcher(url);
		if (!matcher.matches())
		{
			return null;
		}
		if (params.size() == 0)
		{
			return new String[0];
		}

		String[] args = new String[params.size()];
		for (int i = 0; i < args.length && matcher.find(); i++)
		{
			args[i] = matcher.group();
		}

		return args;
	}
}
