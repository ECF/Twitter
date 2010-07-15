package org.eclipse.ecf.provider.twitter.ui.hub.views;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.provider.twitter.container.TwitterUser;
import org.eclipse.ecf.provider.twitter.ui.Messages;
import org.eclipse.ecf.provider.twitter.ui.dialogs.UrlShortenDialog;
import org.eclipse.ecf.provider.twitter.ui.logic.TwitterController;
import org.eclipse.ecf.provider.twitter.ui.utils.ImageUtils;
import org.eclipse.ecf.provider.twitter.ui.utils.TwitterCache;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SendDirectMessagePart extends ViewPart implements SelectionListener{

	public static final String VIEW_ID = "org.eclipse.ecf.provider.twitter.ui.hub.sendDM"; //$NON-NLS-1$
	private static final int TWITTER_CHAR_LIMIT = 140;

	private Text tweetTxt; 
	private TwitterController controller;
	private Combo combo;
	private static Image blankUserImg = ImageUtils.loadImage("blankUserImage.png");
	private List<IUser> following;
	private TwitterUser selectedUser;
	
	private Label userImgLabel;
	
	public SendDirectMessagePart() {
		// TODO Auto-generated constructor stub
	}

	public void setController(TwitterController controller) {
		this.controller = controller;
	}

	
	public void addFollowing(List<IUser> following)
	{
		this.following = following;
		int index = 0;
		for(IUser follower: following)
		{
			TwitterUser user = (TwitterUser)follower;
			combo.add(user.getName());
			index++;
		}
	}
	
	
	
	
	/**
	 * Note that if the user isn't in your list of followers this isn't going 
	 * to work right now. 
	 * 
	 * @param username
	 */
	public void sendDMTo(String username)
	{
	System.err.println("sending dm to " + username);
		//find out if the person is in the list 
		String[] items = combo.getItems();
		for(int i = 0; i < items.length; i++)
		{
			if(items[i].equals(username))
			{
				combo.select(i);
				listSelect();
			}
		}
		
		
	}
	
	@Override
	public void createPartControl(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Form tweetForm = toolkit.createForm(parent);

		tweetForm.setText("Send Direct Message");

		Composite tweet = tweetForm.getBody();
		tweet.setLayout(new GridLayout(5, false));
		tweet.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData gdTop = new GridData();
		gdTop.verticalAlignment = GridData.BEGINNING;
		userImgLabel = new Label(tweet, SWT.NONE);
		userImgLabel.setImage(blankUserImg);
		
		userImgLabel.setLayoutData(gdTop);
		

		combo = new Combo(tweet, SWT.BORDER);
		combo.addSelectionListener(this);
		combo.setLayoutData(gdTop);
	
		tweetTxt = toolkit.createText(tweet, "", SWT.MULTI);
		final Button tweeter = toolkit.createButton(tweet, " ", SWT.NONE); //$NON-NLS-1$
		final StyledText charLimitLbl = new StyledText(tweet, SWT.NONE);
		charLimitLbl.setText("" + TWITTER_CHAR_LIMIT); //$NON-NLS-1$

		tweetTxt.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					sendTweet(tweetTxt.getText());
				}
				charLimitLbl.setText("" //$NON-NLS-1$
						+ (TWITTER_CHAR_LIMIT - tweetTxt.getCharCount()));
				if (tweetTxt.getCharCount() > TWITTER_CHAR_LIMIT) {
					// charLimitLbl.s))
					StyleRange styleRange = new StyleRange();
					styleRange.start = 0;
					styleRange.length = charLimitLbl.getText().length();
					styleRange.fontStyle = SWT.BOLD;
					styleRange.foreground = PlatformUI.getWorkbench()
							.getDisplay().getSystemColor(SWT.COLOR_RED);
					charLimitLbl.setStyleRange(styleRange);
				}

			}

			public void keyPressed(KeyEvent e) {
			}
		});

		Menu menu = new Menu(tweetTxt.getShell(), SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.TweetViewPart_Url_shorten);
		tweetTxt.setMenu(menu);

		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				UrlShortenDialog urlDialog = new UrlShortenDialog(getViewSite()
						.getShell());
				urlDialog.open();
				if (urlDialog.getResult() == Window.OK) {
					try {
						String shorten = controller.getUrlShorten(urlDialog.getUrlShorten());
						tweetTxt.append(shorten);
					} catch (ECFException e1) {
						Status status = new Status(IStatus.ERROR,
								"org.eclipse.ecf.twitter.ui.hub", 0, e1.getMessage(), null); //$NON-NLS-1$
						ErrorDialog.openError(tweetTxt.getShell(),
								"Error trying shorten URL", e1.getMessage(), status);
						tweetTxt.append(urlDialog.getUrlShorten());
					}					
				}
			}
		});

		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalSpan = 2;
		gd.verticalAlignment = SWT.FILL;
		tweetTxt.setLayoutData(gd);

		tweeter.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				sendTweet(tweetTxt.getText());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// highlight when > 140 characters
		// Text charLimitLbl = toolkit.createText(tweet, "140");
		// charLimitLbl.

		Image img = AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ecf.provider.twitter.ui.hub", "icons/write.png") //$NON-NLS-1$ //$NON-NLS-2$
				.createImage();
		tweeter.setToolTipText(Messages.TweetViewPart_Tweet_this);
		tweeter.setImage(img);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		tweeter.setLayoutData(gd);

		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		gd.horizontalAlignment = SWT.FILL;
		charLimitLbl.setLayoutData(gd);
		
		

	}

	private void sendTweet(String text) {
		// TODO: send off this tweet
		
		System.err.println("Sending direct message containing " + text + " to " + selectedUser.getID().getName());
		if(selectedUser != null)
		{
		
			try {
				controller.sendDirectMessage(selectedUser.getID().getName(), text);
				// clear text
				tweetTxt.setText(""); //$NON-NLS-1$
			} catch (ECFException e) {
				// handle exception
				Status status = new Status(IStatus.ERROR,
						"org.eclipse.ecf.twitter.ui.hub", 0, e.getMessage(), null); //$NON-NLS-1$
				ErrorDialog.openError(this.getSite().getShell(),
						Messages.TweetViewPart_13, e.getMessage(), status);
			}
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * Set the text in the tweet text box.
	 * 
	 * @param tweetText
	 */
	public void setTweetText(String tweetText) {
		tweetTxt.setText(tweetText);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		listSelect();
		
	}

	public void widgetSelected(SelectionEvent e) {
		listSelect();
	}
	
	private void listSelect()
	{
		int selected = combo.getSelectionIndex();
		
		selectedUser = (TwitterUser)following.get(selected);
		
		String userId = selectedUser.getID().getName();
		String imagePath = (String)selectedUser.getProperties().get("image");
		Image userImage = TwitterCache.getUserImage(userId);
		userImgLabel.setToolTipText(userId);
		if (userImage!=null) {
			userImgLabel.setImage(userImage);
		} else {
			//if not cached => queue label for image
			userImgLabel.setImage(blankUserImg);
			TwitterCache.queueMessageForUserImageLoading(userId, imagePath, userImgLabel);
		}
		
		
		
	}

	
}
