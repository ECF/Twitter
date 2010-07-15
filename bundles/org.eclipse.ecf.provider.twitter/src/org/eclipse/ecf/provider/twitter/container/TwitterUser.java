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

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.provider.twitter.identity.TwitterID;

/**
 *
 */
public class TwitterUser implements IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5057059512091233851L;
	private final twitter4j.User twitterUser;
	private final TwitterID id;

	Properties props = new Properties();

	public TwitterUser(twitter4j.User user) {
		this.twitterUser = user;
		this.id = new TwitterID(twitterUser.getScreenName());
		final URL url = twitterUser.getURL();
		if (url != null)
			props.put("url", url.toExternalForm());
		final String desc = twitterUser.getDescription();
		if (desc != null && !desc.equals(""))
			props.put("description", twitterUser.getDescription());
		final URL image = twitterUser.getProfileImageURL();
		if(image !=null)
			props.put("image", image.toExternalForm());
		final String location = twitterUser.getLocation();
		if(location !=null)
			props.put("location", location);
		final String screenName = twitterUser.getScreenName();
		if(screenName !=null)
			props.put("screenName", screenName);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.user.IUser#getName()
	 */
	public String getName() {
		return twitterUser.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.user.IUser#getNickname()
	 */
	public String getNickname() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.user.IUser#getProperties()
	 */
	public Map getProperties() {
		return props;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.identity.IIdentifiable#getID()
	 */
	public ID getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

}
