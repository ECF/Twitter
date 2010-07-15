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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ecf.internal.provider.twitter.ui.Activator;
import org.eclipse.ecf.ui.SharedImages;
import org.eclipse.ecf.ui.util.PasswordCacheHelper;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

final class TwitterConnectWizardPage extends WizardPage {

	private Combo connectText;

	private Text passwordText;

	private String username;

	TwitterConnectWizardPage() {
		super(""); //$NON-NLS-1$
		setTitle("Twitter Connection Wizard");
		setDescription("Specify a Twitter username and password to connect to account");
		setPageComplete(false);
		setImageDescriptor(SharedImages.getImageDescriptor(SharedImages.IMG_CHAT_WIZARD));
	}

	TwitterConnectWizardPage(String username) {
		this();
		this.username = username;
	}

	private void verify() {
		final String text = connectText.getText();
		passwordText.setText(""); //$NON-NLS-1$
		if (text.equals("")) { //$NON-NLS-1$
			updateStatus("A valid username must be specified");
		} else {
			updateStatus(null);
			restorePassword(text);
		}
	}

	private void restorePassword(String username) {
		final PasswordCacheHelper pwStorage = new PasswordCacheHelper(username);
		final String pw = pwStorage.retrievePassword();
		if (pw != null) {
			passwordText.setText(pw);
		}
	}

	public void createControl(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		parent.setLayout(new GridLayout());
		final GridData fillData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		final GridData endData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);

		Label label = new Label(parent, SWT.LEFT);
		label.setText("Username:");

		connectText = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN);
		connectText.setLayoutData(fillData);
		connectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verify();
			}
		});
		connectText.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				verify();
			}

			public void widgetSelected(SelectionEvent e) {
				verify();
			}
		});

		label = new Label(parent, SWT.RIGHT);
		label.setText("<user>");
		label.setLayoutData(endData);

		label = new Label(parent, SWT.LEFT);
		label.setText("Password:");
		passwordText = new Text(parent, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(fillData);
		label = new Label(parent, SWT.RIGHT | SWT.WRAP);
		label.setText("Password required for Twitter accounts");
		label.setLayoutData(endData);

		restoreCombo();

		if (username != null) {
			connectText.setText(username);
			restorePassword(username);
			passwordText.setFocus();
		}
		setControl(parent);
	}

	String getConnectID() {
		return connectText.getText();
	}

	String getPassword() {
		return passwordText.getText();
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private static final String PAGE_SETTINGS = TwitterConnectWizardPage.class.getName();
	private static final int MAX_COMBO_VALUES = 40;
	private static final String COMBO_TEXT_KEY = "connectTextValue"; //$NON-NLS-1$
	private static final String COMBO_BOX_ITEMS_KEY = "comboValues"; //$NON-NLS-1$

	protected void saveComboText() {
		final IDialogSettings pageSettings = getPageSettings();
		if (pageSettings != null)
			pageSettings.put(COMBO_TEXT_KEY, connectText.getText());
	}

	protected void saveComboItems() {
		final IDialogSettings pageSettings = getPageSettings();
		if (pageSettings != null) {
			final String connectTextValue = connectText.getText();
			final List rawItems = Arrays.asList(connectText.getItems());
			// If existing text item is not in combo box then add it
			final List items = new ArrayList();
			if (!rawItems.contains(connectTextValue))
				items.add(connectTextValue);
			items.addAll(rawItems);
			int itemsToSaveLength = items.size();
			if (itemsToSaveLength > MAX_COMBO_VALUES)
				itemsToSaveLength = MAX_COMBO_VALUES;
			final String[] itemsToSave = new String[itemsToSaveLength];
			System.arraycopy(items.toArray(new String[] {}), 0, itemsToSave, 0, itemsToSaveLength);
			pageSettings.put(COMBO_BOX_ITEMS_KEY, itemsToSave);
		}
	}

	public IDialogSettings getDialogSettings() {
		return Activator.getDefault().getDialogSettings();
	}

	private IDialogSettings getPageSettings() {
		IDialogSettings pageSettings = null;
		final IDialogSettings dialogSettings = this.getDialogSettings();
		if (dialogSettings != null) {
			pageSettings = dialogSettings.getSection(PAGE_SETTINGS);
			if (pageSettings == null)
				pageSettings = dialogSettings.addNewSection(PAGE_SETTINGS);
			return pageSettings;
		}
		return null;
	}

	protected void restoreCombo() {
		final IDialogSettings pageSettings = getPageSettings();
		if (pageSettings != null) {
			final String[] items = pageSettings.getArray(COMBO_BOX_ITEMS_KEY);
			if (items != null)
				connectText.setItems(items);
			final String text = pageSettings.get(COMBO_TEXT_KEY);
			if (text != null)
				connectText.setText(text);
		}
	}

}
