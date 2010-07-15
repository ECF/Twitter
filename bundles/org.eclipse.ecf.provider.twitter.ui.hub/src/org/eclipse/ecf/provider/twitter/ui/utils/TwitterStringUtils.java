package org.eclipse.ecf.provider.twitter.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterStringUtils 
{
	
	private static final String TWITTER_URL_START ="<a href='http://www.twitter.com/";
	private static final String TWITTER_URL_MID ="'>";
	private static final String TWITTER_URL_END="</a>";
	
	/**
	 * Decorate all the @ symbols in a string as a link
	 */
	public static String decorateUserTags(String message)
	{
		
		Pattern pattern = Pattern.compile("[@]+[A-Za-z0-9-_]*");
		Matcher matcher = pattern.matcher(message);
		StringBuffer url = null;
		boolean found = false;
		while (matcher.find()) {
//            System.err.format("I found the text \"%s\" starting at " +
//               "index %d and ending at index %d.%n",
//                matcher.group(), matcher.start(), matcher.end());
             
             url = new StringBuffer(TWITTER_URL_START);
             url.append(matcher.group().substring(1));
             url.append(TWITTER_URL_MID);
             url.append(matcher.group());
             url.append(TWITTER_URL_END);
             message = message.replace(matcher.group(), url.toString());
            found = true;
        }
		
		/**
		 * Replace any instances of & with &amp;
		 */
		message = message.replaceAll("&", "&amp;");
		
		return message;
	}

}
