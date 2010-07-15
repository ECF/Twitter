package org.eclipse.ecf.provider.twitter.ui.logic;

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
import org.eclipse.ecf.provider.twitter.ui.hub.preferences.PreferenceConstants;
import org.eclipse.ecf.provider.twitter.ui.hub.views.Activator;
import org.eclipse.ecf.ui.actions.AsynchContainerConnectAction;
import org.eclipse.ecf.ui.dialogs.IDCreateErrorDialog;
import org.eclipse.ecf.ui.util.PasswordCacheHelper;
import org.eclipse.jface.preference.IPreferenceStore;

public class AutoConnector {

	
	public static void checkAutoLogin()
	{
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		boolean autoLogin  = store.getBoolean(PreferenceConstants.P_AUTO_LOGIN);
		System.err.println("Autologin is " + autoLogin);
		if(autoLogin)
		{
			String username = store.getString(PreferenceConstants.P_USERNAME);
			String password = store.getString(PreferenceConstants.P_PASSWORD);
			AutoConnector.connect(username, password);
		}
	}
	
	private static void connect(final String connectID, final String password)
	{
	
		TwitterController controller = new TwitterController();
		IContainer container = null; 
		ID targetID = null;
		System.err.println("Attempting connect");
		
		IConnectContext connectContext = ConnectContextFactory.createPasswordConnectContext(password);

		try {
			container = ContainerFactory.getDefault().createContainer("ecf.twitter.twitter4j"); //$NON-NLS-1$
		} catch (final ContainerCreateException e) {
			// None
			e.printStackTrace();
		}
		
		try {
			targetID = IDFactory.getDefault().createID(container.getConnectNamespace(), connectID);
		} catch (final IDCreateException e) {
			new IDCreateErrorDialog(null, connectID, e).open();
		}

		final IPresenceContainerAdapter adapter = (IPresenceContainerAdapter) container.getAdapter(IPresenceContainerAdapter.class);

		container.addListener(controller);
		
		final IChatManager icm = adapter.getChatManager();
		icm.addMessageListener(controller);
		
		new AsynchContainerConnectAction(container, targetID, connectContext, null, new Runnable() {
			public void run() {
				cachePassword(connectID, password);
			}
		}).run();
		controller.addContainer(container);
		System.err.println("Done");

	}
	
	protected static void cachePassword(final String connectID, String password) {
		if (password != null && !password.equals("")) { //$NON-NLS-1$
			final PasswordCacheHelper pwStorage = new PasswordCacheHelper(connectID);
			pwStorage.savePassword(password);
		}
	}
}
