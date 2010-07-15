package org.eclipse.ecf.provider.twitter.ui.logic;

import org.eclipse.ecf.provider.twitter.container.TwitterUser;

public class TwitterMessage 
{
	private String message; 
	private TwitterUser user; 
	
	
	public TwitterMessage(TwitterUser user, String message)
	{
		this.message = message;
		this.user = user;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public TwitterUser getUser()
	{
		return user;
	}
}
