package org.eclipse.ecf.internal.provider.twitter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.util.LogHelper;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.ecf.provider.twitter"; //$NON-NLS-1$
	// The shared instance.
	private static Activator plugin;
	private BundleContext context = null;

	private ServiceTracker logServiceTracker = null;

	
	/**
	 * The constructor.
	 */
	public Activator() {
		super();
		plugin = this;
	}
	
	/**
	 * This method is called upon plug-in activation
	 * @param context 
	 * @throws Exception 
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
	}

	/**
	 * This method is called when the plug-in is stopped
	 * @param context 
	 * @throws Exception 
	 */
	public void stop(BundleContext context) throws Exception {
		if (logServiceTracker != null) {
			logServiceTracker.close();
			logServiceTracker = null;
		}

		this.context = null;
		plugin = null;
	}
	
	protected LogService getLogService() {
		if (logServiceTracker == null) {
			logServiceTracker = new ServiceTracker(this.context, LogService.class.getName(), null);
			logServiceTracker.open();
		}
		return (LogService) logServiceTracker.getService();
	}

	public void log(IStatus status) {
		final LogService logService = getLogService();
		if (logService != null) {
			logService.log(LogHelper.getLogCode(status), LogHelper.getLogMessage(status), status.getException());
		}
	}

	/**
	 * Returns the shared instance.
	 * @return default instance of xmpp plugin.
	 */
	public synchronized static Activator getDefault() {
		if (plugin == null) {
			plugin = new Activator();
		}
		return plugin;
	}

	public static void log(String message) {
		getDefault().log(new Status(IStatus.OK, PLUGIN_ID, IStatus.OK, message, null));
	}

	public static void log(String message, Throwable e) {
		getDefault().log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, e));
	}
}
