package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.ecf.provider.twitter.container.IStatus;
import org.eclipse.ecf.provider.twitter.ui.model.Message;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

/**
 * This class is intended to be the superclass of all view 
 * parts that display messages. 
 * (Messages, Direct,Mentions,Search,Sent) 
 * @author jhome
 *
 */
public abstract class AbstractMessageViewPart extends ViewPart implements Observer, IHyperlinkListener {
	
	
	private ArrayList<Long> previousMessages;

	//for sorting 
	private Date newestMessageDate;
	private Date oldestMessageDate;
	
	private MessageComposite oldestMessage = null;
	private MessageComposite newestMessage = null;
	
	protected Composite formComposite;
	protected ScrolledForm form; 
	protected FormToolkit toolkit;
	
	private String formName;

	public AbstractMessageViewPart(String formName) {
		
		previousMessages = new ArrayList<Long>();
		this.formName = formName;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		toolkit = new FormToolkit(parent.getDisplay());
		//FraGuid
		//form = toolkit.createScrolledForm(parent);
		form = new ScrolledForm(parent, SWT.V_SCROLL |  Window.getDefaultOrientation());
		form.setExpandVertical(true);
		form.setBackground(toolkit.getColors().getBackground());
		form.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		form.setFont(JFaceResources.getHeaderFont());

		form.setText(formName);
		//FraGuid
		//GridLayout layout = new GridLayout();
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		form.getBody().setLayout(layout);
		formComposite = form.getBody();
	}

	protected Composite getBody()
	{
		return formComposite;
	}
	
	protected FormToolkit getToolkit()
	{
		return toolkit;
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		form.setFocus();
	}
	public void dispose()
	{
		toolkit.dispose();
		super.dispose();
	}
	
	private boolean checkRepeat(long id)
	{
		if(previousMessages.contains(id))
		{
			return true;
		}
		previousMessages.add(id);
		return false;
		
	}

	/**
	 * Updates the view with the latest message
	 * Determines if it's a repeat, and where it should appear in the timeline.
	 */
	public abstract void update(Observable o, Object arg);

	
	public void displayMessage(Message message)
	{
		
		boolean seenAlready  = checkRepeat(message.getMessageId());
		
		//either add to the top of the latest, or at the bottom. 
		//bottom is default.
		boolean addToTop = true;
		//check if we've seen this message already (by id?) 
		//if so drop it.
		SimpleDateFormat format = new SimpleDateFormat("hh:mm a MMM d");
		
		if(!seenAlready)
		{
			if(newestMessageDate == null && oldestMessageDate == null)
			{
				newestMessageDate = message.getMessageCreation();
				oldestMessageDate = message.getMessageCreation();
				
			}
			else
			{
				Calendar incoming = GregorianCalendar.getInstance();
				incoming.setTime(message.getMessageCreation());
				
				Calendar newest = GregorianCalendar.getInstance();
				newest.setTime(newestMessageDate);
				if(incoming.after(newest))
				{
					newestMessageDate = message.getMessageCreation();
					addToTop = true;
				}
				Calendar oldest = GregorianCalendar.getInstance();
				oldest.setTime(oldestMessageDate);
				
				if(incoming.before(oldest))	
				{
					oldestMessageDate = message.getMessageCreation();
					addToTop = false;
				}
			}
			
			
			MessageComposite messageComposite;
			if(addToTop)
			{
				messageComposite = new MessageComposite(formComposite,SWT.NONE, message, toolkit, addToTop, newestMessage);
				
				newestMessage = messageComposite;
				if(oldestMessage == null)
				{
					oldestMessage = messageComposite;
				}
			}
			else
			{
				messageComposite = new MessageComposite(formComposite,SWT.NONE, message, toolkit, addToTop,oldestMessage);
				oldestMessage = messageComposite;
			}
			
			
			form.reflow(true);
			form.redraw();
		}

	}
	
	
	public void linkActivated(HyperlinkEvent e) {
		//open up the link in a browser window.
		String link = (String)e.getHref();
		
		try {
			PlatformUI.getWorkbench().getBrowserSupport()
						.getExternalBrowser().openURL(new URL(link));
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
	}

	public void linkEntered(HyperlinkEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void linkExited(HyperlinkEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Extract the details for display from this message.
	 * 
	 * @param message
	 */
	protected Message extractMessageDetails(IStatus message)
	{
		Message tweet = new Message();
		
		tweet.setMessageId(message.getId());
		/**
		 * Get the user's screenname, and real name
		 * (if possible)
		 */
		if(message.getUser() != null)
		{
			tweet.setRealName(message.getUser().getName());
		}
		if(message.getUser().getProperties().get("screenName") != null)
		{
			tweet.setUsername((String)message.getUser().getProperties().get("screenName"));
		}
		//get the message timestamp
		tweet.setMessageCreation(message.getCreatedAt());
		//user id 
		tweet.setUserID( message.getUser().getID().getName());
		//message text
		tweet.setText(message.getText());
		//the path to the user's image.
		tweet.setImagePath((String)message.getUser().getProperties().get("image"));
	
		return tweet;
	}

	
	

}
