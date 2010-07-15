/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.provider.twitter.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.ecf.core.AbstractContainer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.events.ContainerConnectedEvent;
import org.eclipse.ecf.core.events.ContainerConnectingEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectingEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.core.user.User;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.internal.provider.twitter.DirectMessageTwitter;
import org.eclipse.ecf.internal.provider.twitter.StatusTwitter;
import org.eclipse.ecf.presence.IAccountManager;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.roster.IRosterManager;
import org.eclipse.ecf.presence.search.ICriteria;
import org.eclipse.ecf.presence.search.IRestriction;
import org.eclipse.ecf.presence.search.ISearch;
import org.eclipse.ecf.presence.search.IUserSearchListener;
import org.eclipse.ecf.presence.search.IUserSearchManager;
import org.eclipse.ecf.presence.search.UserSearchException;
import org.eclipse.ecf.presence.service.IPresenceService;
import org.eclipse.ecf.provider.twitter.identity.TwitterID;
import org.eclipse.ecf.provider.twitter.identity.TwitterNamespace;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 */
public class TwitterContainer extends AbstractContainer implements
		IPresenceService {

	public final static int MAX_STATUS_LENGTH = 140;

	private final Object connectLock = new Object();

	private final TwitterChatManager chatManager;
	private final TwitterRosterManager rosterManager;
	private final TwitterAccountManager accountManager;

	TwitterID targetID;
	Twitter twitter;

	private final ID containerID;

	public TwitterContainer(ID containerID) {
		this.containerID = containerID;
		this.chatManager = new TwitterChatManager(this);
		this.rosterManager = new TwitterRosterManager(this);
		this.accountManager = new TwitterAccountManager(this);
	}

	Twitter getTwitter() throws ECFException {
		synchronized (connectLock) {
			if (twitter == null)
				throw new ECFException("Not connected");
			return twitter;
		}
	}

	TwitterID getTargetID() {
		return targetID;
	}

	void sendTwitterUpdate(String body) throws ECFException {
		try {
			this.getTwitter().updateStatus(body);
		} catch (final TwitterException e) {
			throw new ECFException(e);
		}
	}

	/**
	 * Return a shorten URL for save characters on tweet
	 * @param url
	 *            This url needs to be a valid resource
	 * @return String URL shorten
	 * @throws ECFException
	 *             In case of container fails, url not valid, fails in connect
	 *             to http
	 */
	public String getUrlShorten(String url) throws ECFException {
		
		if (twitter == null)
			throw new ECFException("Not connected");
		
		// if the url string doenst have the pattern http or https
		// the dzone service complain
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			throw new ECFException(
					"Please note: Our URL check failed. The URL you have entered seems not to be a valid resource.");

		HttpClient client = new HttpClient();
		// Service for shorten URL
		PostMethod post = new PostMethod("http://dzone.it");
		// URL form parameter
		post.addParameter("u", url);

		try {
			client.executeMethod(post);

			String dzoneResponse = post.getResponseBodyAsString();

			String patternStart = "name=\"newurl\" value=\"";
			int ini = dzoneResponse.indexOf(patternStart);

			String patternEnd = "\" onfocus=\"if";
			int end = dzoneResponse.indexOf(patternEnd);

			return dzoneResponse.substring(ini + patternStart.length(), end);

		} catch (HttpException e) {
			throw new ECFException(e);
		} catch (IOException e) {
			throw new ECFException(e);
		} finally {
			post.releaseConnection();
		}

	}

	void sendTwitterMessage(String to, String body) throws ECFException {
		try {
			this.getTwitter().sendDirectMessage(to, body);
		} catch (final TwitterException e) {
			throw new ECFException(e);
		}
	}

	/**
	 * Return a list of Following
	 * 
	 * @return List<IUser>
	 * @throws ECFException
	 */
	public List<IUser> getFollowing() throws ECFException {
		try {

			List<twitter4j.User> friends = getTwitter().getFriends();
			List<IUser> twitterFriends = new ArrayList<IUser>(friends.size());

			Iterator<twitter4j.User> iterator = friends.iterator();
			while (iterator.hasNext()) {
				twitter4j.User user = iterator.next();
				twitterFriends.add(new TwitterUser(user));

			}

			return twitterFriends;

		} catch (final TwitterException e) {
			throw new ECFException("Exception getting twitter friends", e);
		}
	}

	/**
	 * Returns the authenticating user's followers, each with current status
	 * inline.
	 * 
	 * @return List<IUser>
	 * @throws ECFException
	 */
	public List<IUser> getFollowers() throws ECFException {
		try {

			List<twitter4j.User> followers = getTwitter().getFollowers();
			List<IUser> twitterFollowers = new ArrayList<IUser>(followers
					.size());

			Iterator<twitter4j.User> iterator = followers.iterator();
			while (iterator.hasNext()) {
				twitter4j.User user = iterator.next();
				twitterFollowers.add(new TwitterUser(user));

			}

			return twitterFollowers;

		} catch (final TwitterException e) {
			throw new ECFException("Exception getting twitter followers", e);
		}
	}

	/**
	 * 
	 * @return Returns the 20 most recent statuses posted by the authenticating
	 *         user and that user's friends. List of {@link IStatus}
	 * @throws ECFException
	 */
	public List getFriendsTimeline() throws ECFException {
		List timeLine;
		List result = new ArrayList();
		try {
			timeLine = getTwitter().getFriendsTimeline();
			for (Iterator iterator = timeLine.iterator(); iterator.hasNext();) {
				Status status = (Status) iterator.next();
				TwitterUser user = new TwitterUser(status.getUser());
				// FIXME
				result.add(new StatusTwitter(user.getID(), status.getText(),
						status));
			}
			return result;
		} catch (TwitterException e) {
			throw new ECFException("Exception getting friends timeline", e);
		}
	}
	
	/**
	 * Return the list of direct messages sent to the user
	 * @return
	 * @throws ECFException
	 */
	public List<IStatus> getDirectMessages() throws ECFException{
		List<IStatus> result = new ArrayList<IStatus>();
		try {
			List<DirectMessage> messages = getTwitter().getDirectMessages();
			for (Iterator<DirectMessage> iterator = messages.iterator(); iterator.hasNext();) {
				DirectMessage directMessage =  iterator.next();
				TwitterUser user = new TwitterUser(directMessage.getSender());
				result.add(new DirectMessageTwitter(user.getID(), directMessage.getText(), directMessage));
			}
			return result;
		} catch (TwitterException e) {
			throw new ECFException("Exception getting direct Messages", e);
		}
		
	}
	
	/**
	 * 
	 * @return Returns mentions in twitter List of {@link IStatus}
	 * @throws ECFException
	 */
	public List getMentions() throws ECFException {
		List timeLine;
		List result = new ArrayList();
		try {
			timeLine = getTwitter().getMentions();
			for (Iterator iterator = timeLine.iterator(); iterator.hasNext();) {
				Status status = (Status) iterator.next();
				TwitterUser user = new TwitterUser(status.getUser());
				// FIXME
				result.add(new StatusTwitter(user.getID(), status.getText(),
						status, StatusTwitter.MENTION_STATUS_TYPE));
			}
			return result;
		} catch (TwitterException e) {
			throw new ECFException("Exception getting friends timeline", e);
		}
	}
	
	
	

	/**
	 * 
	 * @return Returns the most recent statuses posted in the last 24 hours from
	 *         the authenticating user. List of {@link IStatus}
	 * @throws ECFException
	 */
	public List getUserTimeline() throws ECFException {
		List timeLine;
		List result = new ArrayList();
		try {
			timeLine = getTwitter().getUserTimeline();
			for (Iterator iterator = timeLine.iterator(); iterator.hasNext();) {
				Status status = (Status) iterator.next();
				// result.add(new StatusTwitter(status));
			}
			return result;
		} catch (TwitterException e) {
			throw new ECFException("Exception getting friends timeline", e);
		}

	}

	/**
	 * 
	 * @return Returns the 20 tweets on the specific page. List of
	 *         {@link IStatus}
	 * @throws ECFException
	 */
	public List getFriendsTimeline(int page) throws ECFException {
		List timeLine;
		List result = new ArrayList();
		try {
			timeLine = getTwitter().getFriendsTimeline(new Paging(page));
			for (Iterator iterator = timeLine.iterator(); iterator.hasNext();) {
				Status status = (Status) iterator.next();
				// result.add(new StatusTwitter(status));
			}
			return result;
		} catch (TwitterException e) {
			throw new ECFException("Exception getting friends timeline", e);
		}
	}

	Date lastFriendsTimelineDate;
	List lastFriendsStatuses = new ArrayList();

	public List getTwitterFriendsTimeline() throws ECFException {
		try {
			// XXX this seems to *always* return the list of all statuses in the
			// last 24 hours
			// Even if a 'since' date is provided, it returns all statuses
			// within last 24 hours...strange
			// final List results = (lastFriendsTimelineDate == null) ?
			// this.getTwitter().getFriendsTimeline(targetID.getName()) :
			// this.getTwitter().getFriendsTimeline(targetID.getName(),
			// lastFriendsTimelineDate);
			// XXX in the mean time, we'll simply keep track/hold onto old
			// statuses...and do a manual
			// diff
			final List results = getTwitterFriendsTimelineDiff();
			lastFriendsTimelineDate = new Date();
			return results;
		} catch (final TwitterException e) {
			throw new ECFException("Exception getting friends timeline", e);
		}
	}

	List getTwitterFriendsTimelineDiff() throws TwitterException, ECFException {
		final List twitterList = this.getTwitter().getFriendsTimeline();
		return diffFriendsTimeline(twitterList);
	}

	List diffFriendsTimeline(List twitterList) {
		for (final Iterator i = twitterList.iterator(); i.hasNext();) {
			final Status s = (Status) i.next();
			// if (lastFriendsContains(new StatusTwitter(s))) {
			// i.remove();
			// }
		}
		lastFriendsStatuses.addAll(twitterList);
		return twitterList;
	}

	boolean lastFriendsContains(IStatus newStatus) {
		for (final Iterator i = lastFriendsStatuses.iterator(); i.hasNext();) {
			final Status oldStatus = (Status) i.next();
			if (oldStatus.getId() == newStatus.getId())
				return true;
		}
		return false;
	}

	public TwitterUser[] getTwitterUsersFromFriends() throws ECFException {
		final List<IUser> friends = getFollowing();
		// final List result = new ArrayList();
		//		
		// for (final Iterator<IUser> i = friends.iterator(); i.hasNext();) {
		// //final twitter4j.User twitterUser = (twitter4j.User) i.next();
		// final TwitterUser tu = (TwitterUser)i.next();
		// result.add(tu);
		// }
		return (TwitterUser[]) friends.toArray(new TwitterUser[] {});
	}

	/**
	 * I added this method so that I could get myself as a TwitterUser
	 * 
	 * @return
	 * @throws ECFException
	 */
	public TwitterUser getConnectedUser() throws ECFException {
		try {
			twitter4j.User user = getTwitter().getUserDetail(
					twitter.getUserId());

			TwitterUser twitterUser = new TwitterUser(user);
			return twitterUser;

		} catch (TwitterException e) {
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.presence.IPresenceContainerAdapter#getAccountManager()
	 */
	public IAccountManager getAccountManager() {
		return accountManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.IPresenceContainerAdapter#getChatManager()
	 */
	public IChatManager getChatManager() {
		return chatManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.presence.IPresenceContainerAdapter#getChatRoomManager()
	 */
	public IChatRoomManager getChatRoomManager() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.presence.IPresenceContainerAdapter#getRosterManager()
	 */
	public IRosterManager getRosterManager() {
		return rosterManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.core.IContainer#connect(org.eclipse.ecf.core.identity.ID,
	 * org.eclipse.ecf.core.security.IConnectContext)
	 */
	public void connect(ID targetID, IConnectContext connectContext)
			throws ContainerConnectException {
		fireContainerEvent(new ContainerConnectingEvent(getID(), targetID));
		final String password = getPasswordFromConnectContext(connectContext);
		synchronized (connectLock) {
			if (twitter != null)
				throw new ContainerConnectException("Already connected");
			if (targetID == null)
				throw new ContainerConnectException("targetID cannot be null");
			if (!(targetID instanceof TwitterID))
				throw new ContainerConnectException("targetID of wrong type");
			try {
				this.twitter = new Twitter(targetID.getName(), password);
				this.twitter.setSource("TweetHub");
				this.twitter.setClientURL("http://wiki.eclipse.org/TweetHub");
				this.twitter.verifyCredentials();
				this.targetID = (TwitterID) targetID;
				// Create user
				final User localUser = new User(targetID, this.twitter
						.getUserId()
						+ " [Twitter]");
				// Set local user in chat manager...this creates roster
				rosterManager.setUser(localUser);
				// Then get twitter friends from server
				refreshTwitterFriends();
				// Then get friend's status from server
				refreshTwitterStatuses();
				// Start refresh thread
				startAutoRefresher();
			} catch (final ContainerConnectException e) {
				this.targetID = null;
				throw e;
			} catch (TwitterException e) {
				this.targetID = null;
				throw new ContainerConnectException("Cound not authenticate");
			} catch (final ECFException e) {
				this.targetID = null;
				throw new ContainerConnectException(e);
			}
		}
		fireContainerEvent(new ContainerConnectedEvent(getID(), this.targetID));
	}

	private AutoRefreshThread autoRefreshThread;

	class AutoRefreshThread extends Thread {

		// FIXME make the auto refresh parametrized
		int refreshDelay = 10000;
		boolean done = false;
		Object lock = new Object();

		public AutoRefreshThread(String name) {
			setName(name);
			setDaemon(true);
		}

		public void run() {
			synchronized (lock) {
				while (!done) {
					try {
						lock.wait(refreshDelay);
						if (getTwitter() == null)
							return;
						refreshTwitterFriends();
						refreshTwitterStatuses();
						refreshTwitterMentions();
						refreshTwitterDirectedMessages();
						
					} catch (final ECFException e) {
						// XXX todo...this would be caused by some twitter
						// failure...should probably
						// disconnect or leave up to user
					} catch (final InterruptedException e) {
						// ignore
					}
				}
			}
		}

		public void finish() {
			synchronized (lock) {
				this.done = true;
				lock.notify();
			}
		}
	}

	void startAutoRefresher() {
		if (autoRefreshThread == null) {
			autoRefreshThread = new AutoRefreshThread("Twitter AutoRefresh");
			autoRefreshThread.start();
		}
	}

	void stopAutoRefresher() {
		if (autoRefreshThread != null) {
			autoRefreshThread.finish();
			autoRefreshThread = null;
		}
	}

	void refreshTwitterFriends() throws ECFException {
		final TwitterUser[] twitterUsers = getTwitterUsersFromFriends();
		// Add to roster
		rosterManager.addTwitterFriendsToRoster(twitterUsers);
	}
	
	public void refreshTwitterStatuses() throws ECFException {
		// Then get friend's status messages
		final List statuses = getFriendsTimeline();
		// Add notify message listeners
		chatManager.handleStatusMessages((IStatus[]) statuses
				.toArray(new IStatus[] {}));
	}

	/**
	 * Refresh the direct messages sent to the user, considering the latest
	 * @throws ECFException
	 */
	public void refreshTwitterDirectedMessages() throws ECFException {
		List<IStatus> messages = getDirectMessages();
		chatManager.handleStatusMessages((IStatus[]) messages.toArray(new IStatus[] {}));
	}

	public void refreshTwitterMentions() throws ECFException {
		// get your mentions from twitter
		final List statuses = getMentions();
		// Add notify message listeners
		chatManager.handleStatusMessages((IStatus[]) statuses
				.toArray(new IStatus[] {}));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.IContainer#disconnect()
	 */
	public void disconnect() {
		final ID id = this.targetID;
		fireContainerEvent(new ContainerDisconnectingEvent(getID(), id));
		synchronized (connectLock) {
			stopAutoRefresher();
			lastFriendsStatuses.clear();
			if (twitter != null) {
				twitter = null;
				targetID = null;
			}
		}
		fireContainerEvent(new ContainerDisconnectedEvent(getID(), id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.IContainer#getConnectNamespace()
	 */
	public Namespace getConnectNamespace() {
		return IDFactory.getDefault().getNamespaceByName(TwitterNamespace.NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.IContainer#getConnectedID()
	 */
	public ID getConnectedID() {
		return targetID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIdentifiable#getID()
	 */
	public ID getID() {
		return containerID;
	}

	/**
	 * @param status
	 * @throws ECFException
	 */
	public void sendStatusUpdate(String status) throws ECFException {
		chatManager.getChatMessageSender().sendChatMessage(targetID, status);
	}


	/**
	 * Send a direct message using presence API behind scenes
	 * @param toID. Twitter username that you receive the direct message
	 * @param message. String for direct message
	 * @throws ECFException
	 */
	public void sendDirectMessage(String toID, String message) throws ECFException {
		chatManager.getChatMessageSender().sendChatMessage(IDFactory.getDefault().createID(targetID.getNamespace(), toID), message);
	}

	public IUserSearchManager getUserSearchManager() {
		return new IUserSearchManager() {

			public ICriteria createCriteria() {
				return null;
			}

			public IRestriction createRestriction() {
				return null;
			}

			public String[] getUserPropertiesFields() throws ECFException {
				return null;
			}

			public boolean isEnabled() {
				return false;
			}

			public ISearch search(ICriteria criteria)
					throws UserSearchException {
				return null;
			}

			public void search(ICriteria criteria, IUserSearchListener listener) {

			}

		};
	}

}
