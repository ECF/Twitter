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

import org.eclipse.ecf.presence.search.ICriterion;
import org.eclipse.ecf.presence.search.IRestriction;

public class TwitterRestriction implements IRestriction {

	public ICriterion and(ICriterion left, ICriterion right) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICriterion eq(String field, String value) {
		return new TwitterSimpleCriterion(value);
	}

	public ICriterion eq(String field, String value, boolean ignoreCase) {
		return new TwitterSimpleCriterion(value);
	}

	public ICriterion ne(String field, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICriterion ne(String field, String value, boolean ignoreCase) {
		// TODO Auto-generated method stub
		return null;
	}

}
