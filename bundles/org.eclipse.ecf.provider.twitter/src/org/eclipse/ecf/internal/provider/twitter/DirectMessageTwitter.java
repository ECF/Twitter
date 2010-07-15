package org.eclipse.ecf.internal.provider.twitter;

import java.util.Date;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.presence.im.ChatMessage;
import org.eclipse.ecf.provider.twitter.container.IStatus;
import org.eclipse.ecf.provider.twitter.container.TwitterUser;

import twitter4j.DirectMessage;

public class DirectMessageTwitter extends ChatMessage implements IStatus {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4588588139099653930L;
	DirectMessage directMessage;
	private int statusMessageType = DIRECT_MESSAGE_TYPE;
	
	public DirectMessageTwitter(ID id, String message, DirectMessage directMessage){
		super(id, message);
		this.directMessage = directMessage;
		
	}
	
	public DirectMessageTwitter(ID id, String message, DirectMessage directMessage, int type){
		this(id, message, directMessage);
		statusMessageType = type;
		
	}
	

	public int getStatusMessageType(){
		return statusMessageType;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getCreatedAt()
	 */
	public Date getCreatedAt() {
		return directMessage.getCreatedAt();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getId()
	 */
	public long getId() {
		return directMessage.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getInReplyToStatusId()
	 */
	public long getInReplyToStatusId() {
		return directMessage.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getInReplyToUserId()
	 */
	public int getInReplyToUserId() {
		return directMessage.getRecipient().getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getSource()
	 */
	public String getSource() {
		return directMessage.getSenderScreenName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getText()
	 */
	public String getText() {
		return directMessage.getText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#getUser()
	 */
	public TwitterUser getUser() {
		
		return new TwitterUser(directMessage.getSender());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#isFavorited()
	 */
	public boolean isFavorited() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.twitter.IStatus#isTruncated()
	 */
	public boolean isTruncated() {
		return false;
	}

}
