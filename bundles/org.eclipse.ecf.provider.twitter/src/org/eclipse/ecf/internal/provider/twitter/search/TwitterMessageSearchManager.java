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

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ecf.internal.provider.twitter.Activator;
import org.eclipse.ecf.presence.search.ICriteria;
import org.eclipse.ecf.presence.search.ICriterion;
import org.eclipse.ecf.presence.search.IRestriction;
import org.eclipse.ecf.presence.search.IResultList;
import org.eclipse.ecf.presence.search.ISearch;
import org.eclipse.ecf.presence.search.message.IMessageSearchListener;
import org.eclipse.ecf.presence.search.message.IMessageSearchManager;
import org.eclipse.ecf.presence.search.message.MessageSearchCompleteEvent;
import org.eclipse.ecf.presence.search.message.MessageSearchException;
import org.eclipse.ecf.provider.twitter.search.IResultTweetList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * 
 *@since 2.0
 */
public class TwitterMessageSearchManager implements IMessageSearchManager {

	protected Twitter twitter;

	public TwitterMessageSearchManager(Twitter twitter) {
		this.twitter = twitter;
	}


	public ISearch search(ICriteria criteria) throws MessageSearchException {
		Assert.isNotNull(criteria);
		Assert.isNotNull(twitter);
		try {
			List criterions = criteria.getCriterions();
			ICriterion criterion = (ICriterion) criterions.get(0);
			Query query = new Query(criterion.toExpression());
			QueryResult queryResult = twitter.search(query);
			
			IResultTweetList result = new ResultTweetList(queryResult);
			
			ISearch search = new ISearch(){
			
				IResultList list = null;
				
				public void setResultList(IResultList resultList) {
					this.list = resultList;
				}
			
				public IResultList getResultList() {
					return this.list;
				}
			};
			
			search.setResultList(result);
			return search;
			
		} catch (TwitterException e) {
			throw new MessageSearchException(e, "Problem during the tweet searching");
		}
	}

	public void search(final ICriteria criteria, final IMessageSearchListener listener) {
		Assert.isNotNull(criteria);
		Assert.isNotNull(listener);
		Job job = new Job(Messages.getString("SearchMessage.0")) {//$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				try {
					ISearch search = search(criteria);
					MessageSearchCompleteEvent complete = new MessageSearchCompleteEvent(search);
					listener.handleMessageSearchEvent(complete);
				} catch (MessageSearchException e) {
					Activator.log(e.getLocalizedMessage(), e);
					return new Status(IStatus.ERROR,Activator.PLUGIN_ID,IStatus.ERROR,"Exception in user search",e);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();

		
	}

	public ICriteria createCriteria() {
		return new TwitterCriteria();
	}

	public IRestriction createRestriction() {
		return new TwitterRestriction();
	}

}
