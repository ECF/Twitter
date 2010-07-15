/*******************************************************************************
 * Copyright (c) 2008 Marcelo Mayworm. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 	Marcelo Mayworm - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.ecf.provider.twitter.search;

import org.eclipse.ecf.presence.search.IResultList;

/**
 * 
 * @since 2.0
 *
 */
public interface IResultTweetList extends IResultList{

	/**
	 * 
	 * @return the number of results per page. Default 15
	 */
	public int getResultsPerPage();
	
	/**
	 * 
	 * @return Total of {@link ITweetItem} found
	 */
	public int getTotal();
	
	/**
	 * 
	 * @return Completed in
	 */
	public double getCompletedIn();
	
	/**
	 * 
	 * @return The max id
	 */
	public long getMaxId(); 
	
	/**
	 * 
	 * @return from the authenticating user
	 */
	public long getSinceId();
	
}
