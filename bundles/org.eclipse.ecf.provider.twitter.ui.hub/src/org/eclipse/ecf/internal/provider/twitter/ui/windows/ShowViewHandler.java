package org.eclipse.ecf.internal.provider.twitter.ui.windows;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ecf.provider.twitter.ui.hub.views.DirectMessagesViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.FollowersViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.FriendsViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.MessagesViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.ReplyViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.TrendViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.TweetViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ShowViewHandler implements IHandler {

	private static final String TweetView  = "org.eclipse.ecf.provider.twitter.ui.hub.showTweetWindow";
	private static final String MessageView = "org.eclipse.ecf.provider.twitter.ui.hub.showMessages";
	private static final String MentionsView="org.eclipse.ecf.provider.twitter.ui.hub.showMentions";
	private static final String DirectView="org.eclipse.ecf.provider.twitter.ui.hub.showDirect";
	private static final String FollowingView = "org.eclipse.ecf.provider.twitter.ui.hub.showFollowing";
	private static final String FollowerView ="org.eclipse.ecf.provider.twitter.ui.hub.showFollowers";
	private static final String TrendView ="org.eclipse.ecf.provider.twitter.ui.hub.showTrends";
    
	
	
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stubs

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbench workbench = PlatformUI.getWorkbench();
		
		String viewToRestore = determineViewToRestore(event.getCommand().getId());
		if(viewToRestore != null)
		{
			try {
				workbench.getActiveWorkbenchWindow().getActivePage().showView(viewToRestore);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	private String determineViewToRestore(String commandId)
	{
		if(commandId.equals(TweetView))
		{
			return TweetViewPart.VIEW_ID;
		}
		if(commandId.equals(MessageView))
		{
			return MessagesViewPart.VIEW_ID;
		}
		if(commandId.equals(MentionsView))
		{
			return ReplyViewPart.VIEW_ID;
		}
		if(commandId.equals(DirectView))
		{
			return DirectMessagesViewPart.VIEW_ID;
		}
		if(commandId.equals(FollowingView))
		{
			return FriendsViewPart.VIEW_ID;
		}
		if(commandId.equals(FollowerView))
		{
			return FollowersViewPart.VIEW_ID;
		}
		if(commandId.equals(TrendView))
		{
			return TrendViewPart.VIEW_ID;
		}
		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
