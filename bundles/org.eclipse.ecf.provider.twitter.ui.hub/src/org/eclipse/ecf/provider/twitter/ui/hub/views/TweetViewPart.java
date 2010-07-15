package org.eclipse.ecf.provider.twitter.ui.hub.views;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.provider.twitter.ui.Messages;
import org.eclipse.ecf.provider.twitter.ui.dialogs.UrlShortenDialog;
import org.eclipse.ecf.provider.twitter.ui.logic.TwitterController;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TweetViewPart extends ViewPart {

	public static final String VIEW_ID = "org.eclipse.ecf.provider.twitter.ui.hub.tweetView"; //$NON-NLS-1$
	private static final int TWITTER_CHAR_LIMIT = 140;

	private Text tweetTxt;
	private TwitterController controller;

	public TweetViewPart() {
		// TODO Auto-generated constructor stub
	}

	public void setController(TwitterController controller) {
		this.controller = controller;
	}

	@Override
	public void createPartControl(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Form tweetForm = toolkit.createForm(parent);

		tweetForm.setText(Messages.TweetViewPart_What_doing);

		Composite tweet = tweetForm.getBody();
		tweet.setLayout(new GridLayout(2, false));
		tweet.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		tweetTxt = toolkit.createText(tweet, "", SWT.MULTI);// new Text(tweet, //$NON-NLS-1$
															// SWT.BORDER |
															// SWT.MULTI);
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
		System.err.println(Messages.TweetViewPart_Tweeting + text);
		try {
			controller.tweet(text);
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
}
