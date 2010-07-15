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

import java.util.Date;

import org.eclipse.ecf.presence.search.IResult;

/**
 * 
 *@since 2.0
 */
public interface ITweetItem extends IResult{

	/**
	 * @return
	 */
	public Date getCreatedAt();

	/**
	 * @return
	 */
	public String getFromUser();

	/**
	 * @return
	 */
	public int getFromUserId();

	/**
	 * @return
	 */
	public long getId();

	/**
	 * @return
	 */
	public String getIsoLanguageCode();

	/**
	 * @return
	 */
	public String getProfileImageUrl();

	/**
	 * @return
	 */
	public String getSource();

	/**
	 * @return
	 */
	public String getText();

	/**
	 * @return
	 */
	public String getToUser();

	/**
	 * @return
	 */
	public int getToUserId();

}
