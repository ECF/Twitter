/*******************************************************************************
 * Copyright (c) 2008 Marcelo Mayworm. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 	Marcelo Mayworm - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.ecf.provider.twitter.ui.dialogs;

import org.eclipse.ecf.provider.twitter.ui.Messages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UrlShortenDialog extends Dialog {

	private Text url;

	private int result = Window.CANCEL;

	private Button okButton;

	private String urlShorten = ""; //$NON-NLS-1$

	public UrlShortenDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		container.setLayout(gridLayout);

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText(Messages.UrlShortenDialog_0);

		url = new Text(container, SWT.BORDER);
		url.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		applyDialogFont(container);
		return container;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				Messages.UrlShortenDialog_1, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(true);
	}

	public String getUrlShorten() {
		return urlShorten;
	}

	protected Point getInitialSize() {
		return new Point(330, 157);
	}

	public void buttonPressed(int button) {
		result = button;
		if (button == Window.OK) {

			urlShorten = url.getText();

		}
		close();
	}

	public int getResult() {
		return result;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.UrlShortenDialog_2);
	}

}
