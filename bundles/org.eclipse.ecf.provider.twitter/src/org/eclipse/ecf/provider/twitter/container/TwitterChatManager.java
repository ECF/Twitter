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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.internal.provider.twitter.Activator;
import org.eclipse.ecf.internal.provider.twitter.TwitterMessageChatEvent;
import org.eclipse.ecf.internal.provider.twitter.search.TwitterMessageSearchManager;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.history.IHistory;
import org.eclipse.ecf.presence.history.IHistoryManager;
import org.eclipse.ecf.presence.im.IChat;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.im.IChatMessageSender;
import org.eclipse.ecf.presence.im.ITypingMessageSender;
import org.eclipse.ecf.presence.im.IChatMessage.Type;
import org.eclipse.ecf.presence.search.message.IMessageSearchManager;

/**
 *
 */
public class TwitterChatManager implements IChatManager {

	private final Collection<IIMMessageListener> chatListeners = new ArrayList<IIMMessageListener>();

	private final TwitterContainer container;

	private final IChatMessageSender messageSender = new IChatMessageSender() {

		public void sendChatMessage(ID toID, ID threadID, Type type, String subject, String body, Map properties) throws ECFException {
			sendChatMessage(toID, body);
		}

		public void sendChatMessage(ID toID, String body) throws ECFException {
			if (toID == null || toID.equals(container.getTargetID())) {
				//just update status
				container.sendTwitterUpdate(body);
			} else {
				//send a direct message
				container.sendTwitterMessage(toID.getName(), body);
			}
		}
	};

	IHistoryManager historyManager = new IHistoryManager() {
		public IHistory getHistory(ID targetID, Map options) {
			return null;
		}

		public boolean isActive() {
			return false;
		}

		public void setActive(boolean active) {
		}

		public Object getAdapter(Class adapter) {
			return null;
		}

	};

	void handleStatusMessages(IStatus[] statuses) {
		
		//JS - I'd like to sort these from starting with the oldest message first
		//so I'm going to try running through this array from the end.
			
		//for (int i = 0; i < statuses.length; i++) {
		for (int i = statuses.length -1; i >=0; i--) {
			handleStatusMessage(statuses[i]);
		}
	}

	/**
	 * @param status
	 */
	private void handleStatusMessage(IStatus status) {
		if (status == null)
			return;
//		final User tUser = status.getUser();
//		if (tUser == null)
//			return;
//		final TwitterUser twitterUser = new TwitterUser(tUser);
//		final String chat = status.getText();
		fireMessageListeners(status);
	}

	/**
	 * @param twitterUser
	 * @param chat
	 */
	private void fireMessageListeners(IStatus status) {
		for (final Iterator<IIMMessageListener> i = chatListeners.iterator(); i.hasNext();) {
			final IIMMessageListener l = (IIMMessageListener) i.next();
			l.handleMessageEvent(new TwitterMessageChatEvent(status.getUser().getID(), status));
		}
	}

	public TwitterChatManager(TwitterContainer twitterContainer) {
		this.container = twitterContainer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#addMessageListener(org.eclipse.ecf.presence.IIMMessageListener)
	 */
	public void addMessageListener(IIMMessageListener listener) {
		chatListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#createChat(org.eclipse.ecf.core.identity.ID, org.eclipse.ecf.presence.IIMMessageListener)
	 */
	public IChat createChat(ID targetUser, IIMMessageListener messageListener) throws ECFException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#getChatMessageSender()
	 */
	public IChatMessageSender getChatMessageSender() {
		return messageSender;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#getHistoryManager()
	 */
	public IHistoryManager getHistoryManager() {
		return historyManager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#getTypingMessageSender()
	 */
	public ITypingMessageSender getTypingMessageSender() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#removeMessageListener(org.eclipse.ecf.presence.IIMMessageListener)
	 */
	public void removeMessageListener(IIMMessageListener listener) {
		chatListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ecf.presence.im.IChatManager#getMessageSearchManager()
	 */
	public IMessageSearchManager getMessageSearchManager() {
		
		try {
			return new TwitterMessageSearchManager(this.container.getTwitter());
		} catch (ECFException e) {
			Activator.log("Error creating TwitterMessageSearchManager", e);
		}
		return null;
	}

}
