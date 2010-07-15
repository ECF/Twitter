package org.eclipse.ecf.provider.twitter.ui.hub.preferences;



import org.eclipse.ecf.provider.twitter.ui.hub.views.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class TweetHubPreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage, SelectionListener{

	
	private Scale dmScale;
	private Scale allScale;
	private Scale mentionScale;
	
	private Label dmLabel;
	private Label allLabel;
	private Label mentionLabel;
	
	
	private int allValue; 
	private int dmValue; 
	private int mentionValue;
	
	public TweetHubPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("TweetHub User Settings");
		
		allValue = getPreferenceStore().getInt(PreferenceConstants.P_ALL);
		mentionValue = getPreferenceStore().getInt(PreferenceConstants.P_MENTIONS);
		dmValue = getPreferenceStore().getInt(PreferenceConstants.P_DM);
	}
	
	private StringFieldEditor passwordEditor;
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_AUTO_LOGIN,
				"&Login on startup",
				getFieldEditorParent()));
		
		addField(
			new StringFieldEditor(PreferenceConstants.P_USERNAME, "&Username:", getFieldEditorParent()));

		passwordEditor =new StringFieldEditor(PreferenceConstants.P_PASSWORD, "&Password:", getFieldEditorParent());
		passwordEditor.getTextControl(this.getFieldEditorParent()).setEchoChar('*');
		addField(passwordEditor);
		
		
		Group g = new Group(getFieldEditorParent(), SWT.NONE);
		g.setText("API Call Balancer:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		g.setLayout(new GridLayout(3, false));
		g.setLayoutData(gd);
		
		
		Label l = new Label(g, SWT.NONE);
		l.setText("Balance the API calls made to Twitter each hour");
		l.setLayoutData(gd);
		
		l = new Label(g, SWT.NONE);
		l.setText("Friends Status:");
		
		allScale = new Scale(g,SWT.NONE);
		allScale.setMinimum(0);
		allScale.setIncrement(5);
		allScale.setMaximum(100);
		allScale.setToolTipText("Set the number of times to poll status");
		allScale.setSelection(allValue);
		allScale.addSelectionListener(this);
			
			
		//l = new Label(g, SWT.NONE);
		//l.setText("Something");
		//addField(new StringFieldEditor("something", "A Label", g));
		
		allLabel = new Label(g, SWT.NONE);
		allLabel.setText(calcTime(allValue));
		
		
		l = new Label(g, SWT.NONE);
		l.setText("Mentions:");
		
		
		mentionScale = new Scale(g,SWT.NONE);
		mentionScale.setMinimum(0);
		mentionScale.setIncrement(5);
		mentionScale.setMaximum(100);
		mentionScale.setSelection(mentionValue);
		mentionScale.setToolTipText("Set the number of times to poll mentions");
		mentionScale.addSelectionListener(this);
		//l = new Label(g, SWT.NONE);
		//l.setText("Something");
		//addField(new StringFieldEditor("something", "A Label", g));
		
		mentionLabel = new Label(g, SWT.NONE);
		mentionLabel.setText(calcTime(mentionValue));
		
		l = new Label(g, SWT.NONE);
		l.setText("Direct Messages:");
		
		dmScale = new Scale(g,SWT.NONE);
		dmScale.setMinimum(0);
		dmScale.setIncrement(5);
		dmScale.setMaximum(100);
		dmScale.setToolTipText("Set the number of times to poll direct messages");
		dmScale.addSelectionListener(this);
		dmScale.setSelection(dmValue);
		//l = new Label(g, SWT.NONE);
		//l.setText("Something");
		//addField(new StringFieldEditor("something", "A Label", g));
		
		dmLabel = new Label(g, SWT.NONE);
		dmLabel.setText(calcTime(dmValue));
		
		

	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		checkTotals((Scale)e.getSource());
	}

	public void widgetSelected(SelectionEvent e) {
		checkTotals((Scale)e.getSource());
	}
	
	
	private void checkTotals(Scale source)
	{
		int allValue = allScale.getSelection();
		int dmValue = dmScale.getSelection();
		int mentionValue = mentionScale.getSelection();
		
		//check our limits 
		if( (allValue + dmValue + mentionValue) > 100)
		{
			setErrorMessage("Call Limit Reached");
		    if(source.equals(dmScale))
		    {
		    	dmScale.setSelection(this.dmValue);
		    }
		    if(source.equals(allScale))
		    {
		    	allScale.setSelection(this.allValue);
		    }
		    if(source.equals(mentionScale))
		    {
		    	mentionScale.setSelection(this.mentionValue);
		    }
		}
		else
		{
			 this.dmValue = dmValue;
			 this.allValue = allValue;
			 this.mentionValue = mentionValue;
			 allLabel.setText(calcTime(allValue));
			 dmLabel.setText(calcTime(dmValue));
			 mentionLabel.setText(calcTime(mentionValue));
			 setErrorMessage(null);
		}
		
	}
	
	private String calcTime(int time)
	{
		if(time == 0)
		{
			return "No calls";
		}
		else
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("Every ");
			if(time > 60)
			{
				buffer.append((60*60)/time);
				buffer.append(" seconds  ");
			}
			else
			{
				buffer.append( (60/time));
				
				if(time==1)
				{
					buffer.append(" minute      ");
				}
				else
				{
					buffer.append(" minutes     ");
				} 
				
			}
			
		
			return buffer.toString();
		}
	}
	
	protected void performApply()
	{
		saveValues();
		
		super.performApply();
	}
	
	
	public boolean performOk()
	{
		saveValues();
		return super.performOk();
	}
	
	
	private void saveValues()
	{
		getPreferenceStore().setValue(PreferenceConstants.P_ALL, allValue);
		getPreferenceStore().setValue(PreferenceConstants.P_DM, dmValue);
		getPreferenceStore().setValue(PreferenceConstants.P_MENTIONS, mentionValue);
	}
	
	
}