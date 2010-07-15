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

import java.util.Map;

import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IAccountManager;

/**
 *
 */
public class TwitterAccountManager implements IAccountManager {

	public final TwitterContainer container;

	/**
	 * @param twitterContainer
	 */
	public TwitterAccountManager(TwitterContainer twitterContainer) {
		this.container = twitterContainer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#changePassword(java.lang.String)
	 */
	public boolean changePassword(String newpassword) throws ECFException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#createAccount(java.lang.String, java.lang.String, java.util.Map)
	 */
	public boolean createAccount(String username, String password, Map attributes) throws ECFException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#deleteAccount()
	 */
	public boolean deleteAccount() throws ECFException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#getAccountAttribute(java.lang.String)
	 */
	public Object getAccountAttribute(String attributeName) throws ECFException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#getAccountAttributeNames()
	 */
	public String[] getAccountAttributeNames() throws ECFException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#getAccountCreationInstructions()
	 */
	public String getAccountCreationInstructions() throws ECFException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.IAccountManager#isAccountCreationSupported()
	 */
	public boolean isAccountCreationSupported() throws ECFException {
		// TODO Auto-generated method stub
		return false;
	}

}
