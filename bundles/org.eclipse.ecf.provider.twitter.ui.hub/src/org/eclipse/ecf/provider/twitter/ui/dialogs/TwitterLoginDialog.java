package org.eclipse.ecf.provider.twitter.ui.dialogs;

import org.eclipse.ecf.provider.twitter.ui.logic.TwitterLoginHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class TwitterLoginDialog extends Dialog {
	
	
	private TwitterLoginHandler loginHandler;
	private Text usernameTxt;
	private Text passwordTxt;
	

	public TwitterLoginDialog(Shell parentShell, TwitterLoginHandler handler) {
		super(parentShell);
		loginHandler = handler;
	}
	
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite)super.createDialogArea(parent);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		
		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name:");
		nameLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		
		
		usernameTxt = new Text(container,SWT.BORDER);
		usernameTxt.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		
		final Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setText("Password:");
		passwordLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		
		
		passwordTxt = new Text(container,SWT.BORDER);
		passwordTxt.setEchoChar('*');
		passwordTxt.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		return container;
	}
	
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Login to Twitter");
	}
	
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, "Login", false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}


	protected void buttonPressed(int id)
	{
		if(id == IDialogConstants.OK_ID)
		{
			System.err.println("OK Clicked");
			//loginHandler.login(usernameTxt.getText(), passwordTxt.getText());
		}
		super.buttonPressed(id);
	}
}
