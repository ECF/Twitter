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

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.IPresenceListener;
import org.eclipse.ecf.presence.IPresenceSender;
import org.eclipse.ecf.presence.Presence;
import org.eclipse.ecf.presence.IPresence.Mode;
import org.eclipse.ecf.presence.IPresence.Type;
import org.eclipse.ecf.presence.roster.AbstractRosterManager;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.roster.IRosterItem;
import org.eclipse.ecf.presence.roster.IRosterSubscriptionSender;
import org.eclipse.ecf.presence.roster.RosterEntry;
import org.eclipse.ecf.presence.roster.RosterGroup;

/**
 *
 */
public class TwitterRosterManager extends AbstractRosterManager {

	private final TwitterContainer container;

	private final IPresenceSender presenceSender = new IPresenceSender() {

		public void sendPresenceUpdate(ID targetID, IPresence presence) throws ECFException {
			// TODO Auto-generated method stub

		}

	};

	void modifyTwitterFriends(boolean add, TwitterUser[] friends) {
		if (add) {
			addTwitterFriendsToRoster(friends);
		} else {
			removeTwitterFriendsFromRoster(friends);
		}
		this.notifyRosterUpdate(this.roster);
	}

	/**
	 * @param friends
	 */
	private void removeTwitterFriendsFromRoster(TwitterUser[] friends) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param friends
	 */
	void addTwitterFriendsToRoster(TwitterUser[] friends) {
		for (int i = 0; i < friends.length; i++) {
			new RosterEntry(group, friends[i], new Presence(Type.AVAILABLE, "", Mode.AVAILABLE, friends[i].getProperties()));
		}
	}

	/**
	 * @param twitterContainer
	 */
	public TwitterRosterManager(TwitterContainer twitterContainer) {
		this.container = twitterContainer;
		this.roster = new org.eclipse.ecf.presence.roster.Roster(container);
	}

	public void notifySubscriptionListener(ID fromID, IPresence presence) {
		this.fireSubscriptionListener(fromID, presence.getType());
	}

	public void notifyRosterUpdate(IRosterItem changedItem) {
		fireRosterUpdate(changedItem);
	}

	public void notifyRosterAdd(IRosterEntry entry) {
		fireRosterAdd(entry);
	}

	public void notifyRosterRemove(IRosterEntry entry) {
		fireRosterRemove(entry);
	}

	public void disconnect() {
		getRoster().getItems().clear();
		super.disconnect();
		fireRosterUpdate(roster);
	}

	RosterGroup group;

	public void setUser(IUser user) {
		final org.eclipse.ecf.presence.roster.Roster roster = (org.eclipse.ecf.presence.roster.Roster) getRoster();
		roster.setUser(user);
		group = new RosterGroup(roster, "Friends");
		roster.addItem(group);
		notifyRosterUpdate(roster);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.roster.AbstractRosterManager#getPresenceSender()
	 */
	public IPresenceSender getPresenceSender() {
		return presenceSender;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.roster.AbstractRosterManager#getRosterSubscriptionSender()
	 */
	public IRosterSubscriptionSender getRosterSubscriptionSender() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.roster.IRosterManager#addPresenceListener(org.eclipse.ecf.presence.IPresenceListener)
	 */
	public void addPresenceListener(IPresenceListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.roster.IRosterManager#removePresenceListener(org.eclipse.ecf.presence.IPresenceListener)
	 */
	public void removePresenceListener(IPresenceListener listener) {
		// TODO Auto-generated method stub

	}

}
