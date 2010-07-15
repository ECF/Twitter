package org.eclipse.ecf.internal.provider.twitter;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.presence.im.ChatMessageEvent;
import org.eclipse.ecf.presence.im.IChatMessage;
import org.eclipse.ecf.provider.twitter.container.ITwitterChatMessageEvent;

public class TwitterMessageChatEvent extends ChatMessageEvent implements ITwitterChatMessageEvent{

	public TwitterMessageChatEvent(ID fromId, IChatMessage message) {
		super(fromId, message);
	}

}
