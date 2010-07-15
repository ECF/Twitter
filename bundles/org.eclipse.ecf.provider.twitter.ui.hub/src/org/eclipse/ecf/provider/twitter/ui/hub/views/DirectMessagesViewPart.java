package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.util.Observable;

import org.eclipse.ecf.provider.twitter.container.IStatus;
import org.eclipse.ecf.provider.twitter.ui.model.Message;


public class DirectMessagesViewPart extends AbstractMessageViewPart {

	public static final String VIEW_ID = "org.eclipse.ecf.provider.twitter.ui.hub.directMessagesView";
	
	public DirectMessagesViewPart() {
		super("Direct Messages");
	}

	/**
	 * Updates the view with the latest message
	 * Determines if it's a repeat, and where it should appear in the timeline.
	 */
	public void update(Observable o, Object arg) {
		IStatus message = (IStatus)arg;
		if(message.getStatusMessageType() == IStatus.DIRECT_MESSAGE_TYPE)
		{
			Message tweet = super.extractMessageDetails(message);
			displayMessage(tweet);
		}
	}
}
