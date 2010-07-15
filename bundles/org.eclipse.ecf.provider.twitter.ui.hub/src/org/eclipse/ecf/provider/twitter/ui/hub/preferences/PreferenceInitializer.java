package org.eclipse.ecf.provider.twitter.ui.hub.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.ecf.provider.twitter.ui.hub.views.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_AUTO_LOGIN, false);
		store.setDefault(PreferenceConstants.P_ALL, 60);
		store.setDefault(PreferenceConstants.P_MENTIONS, 5);
		store.setDefault(PreferenceConstants.P_DM, 5);
		
		
	}

}
