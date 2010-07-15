package org.eclipse.ecf.provider.twitter.ui.hub.product;

import org.eclipse.ecf.provider.twitter.ui.hub.views.DirectMessagesViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.FollowersViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.FriendsViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.MessagesViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.ReplyViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.SearchViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.SendDirectMessagePart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.TrendViewPart;
import org.eclipse.ecf.provider.twitter.ui.hub.views.TweetViewPart;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "org.eclipse.ecf.provider.twitter.ui.hub.perspective";
	
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		String editorArea = layout.getEditorArea();

		IFolderLayout sendMessages = layout.createFolder("senderFolder", IPageLayout.TOP, 0.2f,editorArea);		
		sendMessages.addView(TweetViewPart.VIEW_ID);
		sendMessages.addView(SendDirectMessagePart.VIEW_ID);
		
		
		IFolderLayout friends = layout.createFolder("friendsFolder", IPageLayout.BOTTOM, 0.26f,TweetViewPart.VIEW_ID);
		friends.addView(FriendsViewPart.VIEW_ID);
		friends.addView(FollowersViewPart.VIEW_ID);
		
		//layout.addView(FriendsViewPart.VIEW_ID, IPageLayout.BOTTOM, 0.2f,TweetViewPart.VIEW_ID);
		
		
		
//		layout.addView(MessagesViewPart.VIEW_ID, IPageLayout.RIGHT, 0.25f,FriendsViewPart.VIEW_ID);//	
//		layout.addView(ReplyViewPart.VIEW_ID, IPageLayout.RIGHT, 0.5f, MessagesViewPart.VIEW_ID);
//		layout.addView(DirectMessagesViewPart.VIEW_ID, IPageLayout.RIGHT, 0.75f, ReplyViewPart.VIEW_ID);
//		
		//new
		IFolderLayout messages = layout.createFolder("messageFolder", IPageLayout.RIGHT, 0.25f, "friendsFolder"); //FriendsViewPart.VIEW_ID);
		messages.addView(MessagesViewPart.VIEW_ID);//	
		messages.addView(ReplyViewPart.VIEW_ID);
		messages.addView(DirectMessagesViewPart.VIEW_ID);
		messages.addView(SearchViewPart.VIEW_ID);
		messages.addView(TrendViewPart.VIEW_ID);
		

		//			
//		
		//System.err.println("Perspective opened...");
		/** 
		 * Still to add.
		 */
		//Search
		//Favourites
		//Groups
		//TODO: add a preferences page. 
		layout.setEditorAreaVisible(false);
	}
	
	

}
