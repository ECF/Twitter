package org.eclipse.ecf.provider.twitter.ui.model;

import java.util.Date;

/**
 * An independent representation of a twitter message. 
 * 
 * @author jsugrue
 *
 */
public class Message {

	private String realName; 
	private String username; 
	private Date messageCreation;
	private String userID;
	private String text; 
	private String imagePath;
	private long messageId; 
	
	
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getMessageCreation() {
		return messageCreation;
	}
	public void setMessageCreation(Date messageCreation) {
		this.messageCreation = messageCreation;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	} 
	
	
	
}
