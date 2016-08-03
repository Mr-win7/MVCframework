package com.demo.mvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlMatcher
{
	Pattern pattern;

	public UrlMatcher(String projectName, String className, String uri)
	{
		Pattern params = Pattern.compile("(\\{[^/\\\\]*?\\})");
		Matcher matcher = params.matcher(uri);
		String patternString = matcher.replaceAll("([^/\\\\]*?)");
		this.pattern = Pattern.compile(projectName + className + patternString);
	}
}
