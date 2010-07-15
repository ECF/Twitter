/*******************************************************************************
 * Copyright (c) 2008 Marcelo Mayworm. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 	Marcelo Mayworm - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.twitter.search;

import java.util.Date;

import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.provider.twitter.search.ITweetItem;

import twitter4j.Tweet;

/**
 * 
 *@since 2.0
 */
public class TweetItem implements ITweetItem{

	protected Tweet tweet;

	public TweetItem(Tweet tweet) {
		this.tweet = tweet;
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getCreatedAt()
	 */
	public Date getCreatedAt() {
		return tweet.getCreatedAt();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getFromUser()
	 */
	public String getFromUser() {
		return tweet.getFromUser();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getFromUserId()
	 */
	public int getFromUserId() {
		return tweet.getFromUserId();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getId()
	 */
	public long getId() {
		return tweet.getId();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getIsoLanguageCode()
	 */
	public String getIsoLanguageCode() {
		return tweet.getIsoLanguageCode();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getProfileImageUrl()
	 */
	public String getProfileImageUrl() {
		return tweet.getProfileImageUrl();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getSource()
	 */
	public String getSource() {
		return tweet.getSource();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getText()
	 */
	public String getText() {
		return tweet.getText();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getToUser()
	 */
	public String getToUser() {
		return tweet.getToUser();
	}

	/**
	 * @return
	 * @see twitter4j.Tweet#getToUserId()
	 */
	public int getToUserId() {
		return tweet.getToUserId();
	}

	public IUser getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
