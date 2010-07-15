/****************************************************************************
 * Copyright (c) 2008 Remy Suen, Composent Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.provider.twitter.ui.wizards;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.ui.MultiRosterView;
import org.eclipse.ecf.provider.twitter.ui.hub.preferences.PreferenceConstants;
import org.eclipse.ecf.provider.twitter.ui.hub.views.Activator;
import org.eclipse.ecf.provider.twitter.ui.logic.TwitterController;
import org.eclipse.ecf.ui.IConnectWizard;
import org.eclipse.ecf.ui.actions.AsynchContainerConnectAction;
import org.eclipse.ecf.ui.dialogs.IDCreateErrorDialog;
import org.eclipse.ecf.ui.util.PasswordCacheHelper;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;


public final class TwitterConnectWizard extends Wizard implements IConnectWizard, INewWizard {

	private TwitterConnectWizardPage page;

	private IContainer container;

	private ID targetID;

	private IConnectContext connectContext;

	private String username;
	
	private TwitterController twitterController;
	

	public TwitterConnectWizard() {
		super();
		twitterController = new TwitterController();
	}

	public TwitterConnectWizard(String username) {
		this();
		this.username = username;
	}

	public void addPages() {
		page = new TwitterConnectWizardPage(username);
		addPage(page);
	}

	public void init(IWorkbench workbench, IContainer container) {
		this.container = container;
		this.workbench = workbench;
		setWindowTitle("New Twitter Connection");
	}

	private void openView() {
		try {
			final MultiRosterView view = (MultiRosterView) workbench.getActiveWorkbenchWindow().getActivePage().showView(MultiRosterView.VIEW_ID);
			view.addContainer(container);
		} catch (final PartInitException e) {
			e.printStackTrace();
		}
	}

	
	private IWorkbench workbench;
	
	public boolean performFinish() {

		final String connectID = page.getConnectID();
		final String password = page.getPassword();
		final boolean autoConnect= page.isAutoConnectSelected();
		
		//save to preferences 
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setValue(PreferenceConstants.P_AUTO_LOGIN, autoConnect);
		store.setValue(PreferenceConstants.P_PASSWORD, password);
		store.setValue(PreferenceConstants.P_USERNAME,connectID);
		

		page.saveComboText();

		connectContext = ConnectContextFactory.createPasswordConnectContext(password);

		try {
			targetID = IDFactory.getDefault().createID(container.getConnectNamespace(), connectID);
		} catch (final IDCreateException e) {
			new IDCreateErrorDialog(null, connectID, e).open();
			return false;
		}

		page.saveComboItems();
		
		
		final IPresenceContainerAdapter adapter = (IPresenceContainerAdapter) container.getAdapter(IPresenceContainerAdapter.class);
//		container.addListener(new IContainerListener() {
//			public void handleEvent(IContainerEvent event) {
//				if (event instanceof IContainerConnectedEvent) {
//					Display.getDefault().asyncExec(new Runnable() {
//						public void run() {
//							openView();
//						}
//					});
//				}
//			}
//		});

		container.addListener(twitterController);
		final IChatManager icm = adapter.getChatManager();
//		icms = icm.getChatMessageSender();
//		itms = icm.getTypingMessageSender();

		icm.addMessageListener(twitterController);
		
		
		
		//		icm.addMessageListener(new IIMMessageListener() {
//			public void handleMessageEvent(IIMMessageEvent e) {
//				if (e instanceof IChatMessageEvent) {
//					displayMessage((IChatMessageEvent) e);
//				} else if (e instanceof ITypingMessageEvent) {
//					displayTypingNotification((ITypingMessageEvent) e);
//				}
//			}
//		});

		new AsynchContainerConnectAction(container, targetID, connectContext, null, new Runnable() {
			public void run() {
				cachePassword(connectID, password);
			}
		}).run();
		twitterController.addContainer(container);

		return true;
	}

	protected void cachePassword(final String connectID, String password) {
		if (password != null && !password.equals("")) { //$NON-NLS-1$
			final PasswordCacheHelper pwStorage = new PasswordCacheHelper(connectID);
			pwStorage.savePassword(password);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// nothing to do
		this.workbench = workbench;

		try {
			this.container = ContainerFactory.getDefault().createContainer("ecf.twitter.twitter4j"); //$NON-NLS-1$
		} catch (final ContainerCreateException e) {
			// None
		}

		setWindowTitle("New Twitter Connection");
	}

}
