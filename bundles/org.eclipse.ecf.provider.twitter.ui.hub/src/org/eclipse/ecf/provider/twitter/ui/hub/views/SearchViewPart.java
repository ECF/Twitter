package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import org.eclipse.ecf.presence.search.IResultList;
import org.eclipse.ecf.presence.search.message.MessageSearchException;
import org.eclipse.ecf.provider.twitter.search.ITweetItem;
import org.eclipse.ecf.provider.twitter.ui.logic.TwitterController;
import org.eclipse.ecf.provider.twitter.ui.model.Message;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SearchViewPart extends AbstractMessageViewPart implements Listener {

	public static final String VIEW_ID = "org.eclipse.ecf.provider.twitter.ui.hub.searchView";
	private FormToolkit toolkit; 
	private TwitterController controller;
	
	private ArrayList<MessageComposite> lastResults = new ArrayList<MessageComposite>();
	
	//private Text searchQuery;
	private ControlComposite controls;
	
	public SearchViewPart() {
		super("Twitter Search");
	}

//	@Override
	public void createPartControl(Composite parent) {
			super.createPartControl(parent);
			controls = new ControlComposite(getBody(), SWT.NONE, this, getToolkit());
	}

	public void setController(TwitterController controller)
	{
		this.controller = controller;
	}


	public void handleEvent(Event event) 
	{	
		//Display display = PlatformUI.getWorkbench().getDisplay();
		
		//run a search 
		clearPreviousResults();
		
		String query = controls.getQuery();
		try
		{
			IResultList resultList =   controller.runSearch(query);
			System.err.println("Got " + resultList.getResults().size() + " results");
			
			Iterator<ITweetItem> results = resultList.getResults().iterator();
			
			//start adding message composites here.
			while(results.hasNext())
			{
				ITweetItem res = results.next();
				
				
				displayMessage(extractMessageDetails(res));
				
//				MessageComposite message = new MessageComposite(formComposite, SWT.NONE, res, toolkit);
//				lastResults.add(message);
//				form.reflow(true);
//				form.redraw();
			}
			
			
		}
		catch(MessageSearchException mse)
		{
			//handle error with search 
			mse.printStackTrace();
			
			
		}

	  
	}

	
	
	
	
	private void clearPreviousResults()
	{
		for(MessageComposite msg: lastResults)
		{
			msg.getContainer().dispose();
			msg = null;
		}
		lastResults.clear();
	}

	private Message extractMessageDetails(ITweetItem message)
	{
		Message tweet = new Message();
		/**
		 * Is there a way to seperate the user's real name 
		 * and screenname in the search api.
		 */
		
		if(message.getFromUser() != null)
		{
			tweet.setRealName(message.getFromUser());
			tweet.setUsername(message.getFromUser());
		}
		

		//get the message timestamp
		tweet.setMessageCreation(message.getCreatedAt());
		//user id 
		tweet.setUserID(message.getFromUser());
		//message text
		tweet.setText(message.getText());
		//the path to the user's image.
		tweet.setImagePath(message.getProfileImageUrl());
		
		tweet.setMessageId(message.getId());
		
		return tweet;
	}
	
	/**
	 * Not required for search view.
	 */
	public void update(Observable o, Object arg) {
	
	}
}
