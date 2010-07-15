package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.provider.twitter.container.TwitterUser;
import org.eclipse.ecf.provider.twitter.ui.utils.ImageUtils;
import org.eclipse.ecf.provider.twitter.ui.utils.TwitterCache;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
/**
 * This class is intended to be the superclass for all view
 * parts that display user information (Following/Followers) 
 * on the hub.
 * 
 * @author jsugrue
 *
 */
public class AbstractUserViewPart extends ViewPart implements  MouseTrackListener, Listener, IHyperlinkListener{

	private Composite formComposite;
	private List<IUser> people;
	private ScrolledForm form;
	private FormToolkit toolkit;
	
	
	private static Image blankUserImg = ImageUtils.loadImage("blankUserImage.png");
	
	private Shell tip = null;
	
	private String formText; 
	
	public AbstractUserViewPart(String formText) {
		// TODO Auto-generated constructor stub
		this.formText = formText;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		
		form.setText(formText);
		
		formComposite = form.getBody();
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		formComposite.setLayout(layout);
	}

	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void addFriends(List<IUser> friends)
	{
		this.people = friends;
		for(int i =0; i < friends.size(); i++)
		{
			addUserToView((TwitterUser)people.get(i));
		}
		form.reflow(true);
		form.redraw();
	}
	
		
	
	private void addUserToView(TwitterUser user)
	{
		
		Composite composite = toolkit.createComposite(formComposite, SWT.NONE );
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		
		Label imageLabel = toolkit.createLabel(composite,"");
		imageLabel.setLayoutData(new TableWrapData(TableWrapData.LEFT,TableWrapData.TOP, 1, 1));
		imageLabel.setData(user);
		imageLabel.addMouseTrackListener(this);
		
		if(user.getProperties().get("image") != null)
		{
			String userID = user.getID().getName();
			String imagePath = (String)user.getProperties().get("image");
			Image userImage = TwitterCache.getUserImage(userID);
			if (userImage!=null) {
				imageLabel.setImage(userImage);
			} else {
				//if not cached => queue label for image
				imageLabel.setImage(blankUserImg);
				TwitterCache.queueMessageForUserImageLoading(userID, imagePath, imageLabel);
			}
		}
		

		
		String username =  user.getName();
		StyledText nameLabel = new StyledText(composite, SWT.WRAP | SWT.MULTI);
		nameLabel.setText(username);
		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = username.length();
		styleRange.fontStyle = SWT.BOLD;
		styleRange.foreground = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_BLACK);
		nameLabel.setStyleRange(styleRange);
		nameLabel.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.TOP, 1, 1));
	}

	
	
	
	
	public void mouseEnter(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof Label)
		{
			Label l = (Label)e.getSource();
			TwitterUser user = (TwitterUser)l.getData();
			
			
			Display display = Display.getCurrent();
			
				if (tip != null  && !tip.isDisposed ()) 
					tip.dispose ();
				
				
				tip = new Shell (l.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL | SWT.BALLOON);
				Color backgroundColor=new Color(tip.getDisplay(), new RGB(245, 245, 245));
				tip.setBackground (backgroundColor);//(SWT.COLOR_INFO_BACKGROUND));
//				FillLayout layout = new FillLayout ();
//				layout.marginWidth = 2;
//				tip.setLayout (layout);
				tip.setLayout(new GridLayout(2, false));
				
				Label imageLabel = new Label(tip, SWT.NONE);
				imageLabel.addListener (SWT.MouseExit, this);
				imageLabel.addListener (SWT.MouseDown, this);
				
				imageLabel.setBackground(backgroundColor);
				imageLabel.setData(user);
				if(user.getProperties().get("image") != null)
				{
					String userID = user.getID().getName();
					String imagePath = (String)user.getProperties().get("image");
					Image userImage = TwitterCache.getUserImage(userID);
					if (userImage!=null) {
						imageLabel.setImage(userImage);
					} else {
						//if not cached => queue label for image
						imageLabel.setImage(blankUserImg);
						TwitterCache.queueMessageForUserImageLoading(userID, imagePath, imageLabel);
					}
				}
				FormText text = new FormText(tip,SWT.WRAP);
				
				text.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
				text.setBackground (backgroundColor);
				
				/**
				 * Add all profile data to this
				 */
				String bio = (String)user.getProperties().get("description");
				String location = (String)user.getProperties().get("location");
				String url = (String)user.getProperties().get("url");
				
				
				StringBuffer textBuffer = new StringBuffer();
				textBuffer.append("<form><p><b>Name:</b> ");
				textBuffer.append(user.getName());
				
				textBuffer.append(" <a href='");
				textBuffer.append("http://www.twitter.com/");
				textBuffer.append((String)user.getProperties().get("screenName"));
				textBuffer.append("'>");
				textBuffer.append("View Profile");
				textBuffer.append("</a></p>");
				
				if(bio != null)
				{
					textBuffer.append("<p><b>Bio:</b> ");
					textBuffer.append(bio);
					textBuffer.append("</p>");
				}
				if(location != null)
				{
					textBuffer.append("<p><b>Location:</b> ");
					textBuffer.append(location);
					textBuffer.append("</p>");
				}
				if(url != null)
				{
					textBuffer.append("<p><b>Web:</b> <a href='");
					textBuffer.append(url);
					textBuffer.append("'>");
					textBuffer.append(url);
					textBuffer.append("</a></p>");
				}
				//add in link to profile 
				
				textBuffer.append("</form>");
				
				
				text.setText(textBuffer.toString(), true, true);
				
				
				
				text.addListener (SWT.MouseExit, this);
				text.addListener (SWT.MouseDown, this);
				text.addHyperlinkListener(this);
				
				
				Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
				Rectangle rect = l.getBounds ();
				Point pt = l.toDisplay (rect.x, rect.y);
				tip.setBounds (pt.x, pt.y, size.x, size.y);
				tip.setVisible (true);
		}
		
	}
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
		if(tip != null && !tip.isDisposed())
			tip.dispose();
		
	}


	public void mouseExit(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseHover(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void linkActivated(HyperlinkEvent e) {
		//open up the link in a browser window.
		String link = (String)e.getHref();
		
		try {
			PlatformUI.getWorkbench().getBrowserSupport().createBrowser("tweetbrowse")
						.openURL(new URL(link));
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void linkEntered(HyperlinkEvent e) {
		
		
	}

	public void linkExited(HyperlinkEvent e) {
		// TODO Auto-generated method stub
		
	}

}
