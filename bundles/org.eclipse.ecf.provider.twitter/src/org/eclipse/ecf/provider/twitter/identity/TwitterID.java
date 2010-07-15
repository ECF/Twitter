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

package org.eclipse.ecf.provider.twitter.identity;

import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.identity.StringID;

/**
 *
 */
public class TwitterID extends StringID {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8386117505195661776L;
	private static final String TWITTER_SERVICE = "http://twitter.com/";

	/**
	 * @param n
	 * @param s
	 */
	protected TwitterID(Namespace n, String s) {
		super(n, s);
	}

	public TwitterID(String s) {
		super(IDFactory.getDefault().getNamespaceByName(TwitterNamespace.NAME), s);
	}

	public String getUsername() {
		return getName();
	}

	public String getTwitterURL() {
		return TWITTER_SERVICE + "/" + getUsername();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.identity.StringID#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof TwitterID) {
			final TwitterID other = (TwitterID) o;
			return getName().equals(other.getName());
		}
		return false;
	}
}
