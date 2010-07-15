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

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.provider.BaseContainerInstantiator;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;

/**
 *
 */
public class TwitterInstantiator extends BaseContainerInstantiator {

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.provider.BaseContainerInstantiator#createInstance(org.eclipse.ecf.core.ContainerTypeDescription, java.lang.Object[])
	 */
	public IContainer createInstance(ContainerTypeDescription description, Object[] parameters) throws ContainerCreateException {
		try {
			if (parameters != null) {
				if (parameters[0] instanceof ID) {
					return new TwitterContainer((ID) parameters[0]);
				} else if (parameters[0] instanceof String) {
					return new TwitterContainer(IDFactory.getDefault().createStringID((String) parameters[0]));
				}
			}
			return new TwitterContainer(IDFactory.getDefault().createGUID());
		} catch (final Exception e) {
			throw new ContainerCreateException("Could not create twitter container", e);
		}
	}
	
	 public String[] getSupportedAdapterTypes(
			ContainerTypeDescription description) {
		return new String[] { IPresenceContainerAdapter.class.getName() };
	}
}
