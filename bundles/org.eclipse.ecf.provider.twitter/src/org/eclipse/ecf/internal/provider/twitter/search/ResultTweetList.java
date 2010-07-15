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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ecf.presence.search.IResult;
import org.eclipse.ecf.provider.twitter.search.IResultTweetList;

import twitter4j.QueryResult;
import twitter4j.Tweet;

public class ResultTweetList implements IResultTweetList {

	protected QueryResult queryResult;
	protected List tweets = Collections.synchronizedList(new ArrayList(5));

	public ResultTweetList(QueryResult queryResult) {
		this.queryResult = queryResult;
	
		List queryTweets = queryResult.getTweets();
		for (Iterator iterator = queryTweets.iterator(); iterator.hasNext();) {
			Tweet tweet = (Tweet) iterator.next();
			TweetItem item = new TweetItem(tweet);
			tweets.add(item);
		}

	}

	public double getCompletedIn() {
		return queryResult.getCompletedIn();
	}

	public long getMaxId() {
		return queryResult.getMaxId();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ecf.provider.twitter.search.IResultTweetList#getResults()
	 */
	public List getResults() {
		return tweets;
	}

	public int getResultsPerPage() {
		return queryResult.getResultsPerPage();
	}

	public long getSinceId() {
		return queryResult.getSinceId();
	}

	public int getTotal() {
		return tweets.size();
	}

	public IResult getResult(String field, String value) {
		return null;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	
}
