package org.eclipse.ecf.internal.provider.twitter;

import java.util.Date;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.presence.im.ChatMessage;
import org.eclipse.ecf.provider.twitter.container.IStatus;
import org.eclipse.ecf.provider.twitter.container.TwitterUser;

import twitter4j.Status;

public class StatusTwitter extends ChatMessage implements IStatus {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4588588139099653930L;
	Status status;
	private int statusMessageType = NORMAL_STATUS_TYPE;
	
	public StatusTwitter(ID id, String message, Status status){
		super(id, message);
		this.status = status;
		
	}
	
	public StatusTwitter(ID id, String message, Status status, int type){
		this(id, message, status);
		statusMessageType = type;
		
	}
	

	public int getStatusMessageType()
	{
		return statusMessageType;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getCreatedAt()
	 */
	public Date getCreatedAt() {
		return status.getCreatedAt();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getId()
	 */
	public long getId() {
		return status.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getInReplyToStatusId()
	 */
	public long getInReplyToStatusId() {
		return status.getInReplyToStatusId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getInReplyToUserId()
	 */
	public int getInReplyToUserId() {
		return status.getInReplyToUserId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getSource()
	 */
	public String getSource() {
		return status.getSource();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getText()
	 */
	public String getText() {
		return status.getText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getUser()
	 */
	public TwitterUser getUser() {
		
		return new TwitterUser(status.getUser());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#isFavorited()
	 */
	public boolean isFavorited() {
		return status.isFavorited();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#isTruncated()
	 */
	public boolean isTruncated() {
		return status.isTruncated();
	}

}
