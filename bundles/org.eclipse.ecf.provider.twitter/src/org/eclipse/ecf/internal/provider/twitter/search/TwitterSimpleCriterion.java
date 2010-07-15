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

import org.eclipse.ecf.presence.search.SimpleCriterion;

public class TwitterSimpleCriterion extends SimpleCriterion {

	public TwitterSimpleCriterion(String value) {
		super("", value, "");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ecf.presence.search.ICriterion#toExpression()
	 */
	public String toExpression() {
		return value;
	}


}
