package org.eclipse.ecf.provider.twitter.container;

import java.util.Date;

import org.eclipse.ecf.presence.im.IChatMessage;

public interface IStatus extends IChatMessage{
	
	
	public static final int NORMAL_STATUS_TYPE = 0;
	public static final int MENTION_STATUS_TYPE = 1;
	public static final int DIRECT_MESSAGE_TYPE = 2;
	public static final int SENT_MESSAGE_TYPE = 3;

	/**
	 * @return
	 */
	public abstract int getStatusMessageType();
	
	/**
	 * @return
	 */
	public abstract Date getCreatedAt();

	/**
	 * @return
	 */
	public abstract long getId();

	/**
	 * @return
	 */
	public abstract long getInReplyToStatusId();

	/**
	 * @return
	 */
	public abstract int getInReplyToUserId();

	/**
	 * @return
	 */
	public abstract String getSource();

	/**
	 * @return
	 */
	public abstract String getText();

	/**
	 * @return
	 */
	public abstract TwitterUser getUser();

	/**
	 * @return
	 */
	public abstract boolean isFavorited();

	/**
	 * @return
	 */
	public abstract boolean isTruncated();


}
