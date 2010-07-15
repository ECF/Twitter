package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.util.Observable;

import org.eclipse.ecf.provider.twitter.container.IStatus;
import org.eclipse.ecf.provider.twitter.ui.model.Message;


/**
 * To keep our view parts properly modular, we extend the MessageViewPart to do the real heavy lifting for this class.
 * 
 * @author jsugrue
 *
 */
public class MessagesViewPart extends AbstractMessageViewPart{
	
	
	public static final String VIEW_ID = "org.eclipse.ecf.provider.twitter.ui.hub.messagesView";
	//for sorting 
	public MessagesViewPart() {
		
		super("Your Messages");
	
	}
	
	
	
	
	
	/**
	 * Updates the view with the latest message
	 * Determines if it's a repeat, and where it should appear in the timeline.
	 */
	public void update(Observable o, Object arg) {
		IStatus message = (IStatus)arg;
		if(message.getStatusMessageType() == IStatus.NORMAL_STATUS_TYPE)
		{
			Message tweet = super.extractMessageDetails(message);
			displayMessage(tweet);
		}
	}
	
	
	
	
	
}
