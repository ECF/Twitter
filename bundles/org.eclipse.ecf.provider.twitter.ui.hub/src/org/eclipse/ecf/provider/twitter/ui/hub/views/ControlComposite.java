package org.eclipse.ecf.provider.twitter.ui.hub.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ControlComposite 
{

	private Text queryText; 
	
	private FormToolkit toolkit; 
	private Composite composite; 
	private Composite border;
	
	private Listener listener; 
	
	
	public ControlComposite(Composite parent, int style, Listener listener, FormToolkit toolkit)
	{
	
		this.listener = listener;
		//FraGuid
		border= new Composite(parent, SWT.NONE);
		border.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		TableWrapLayout layout=new TableWrapLayout();
		border.setLayout(layout);
		layout.leftMargin=1;
		layout.rightMargin=1;
		layout.topMargin=1;
		layout.bottomMargin=1;
		toolkit.adapt(border);
		composite = toolkit.createComposite(border, style );
		
		this.toolkit = toolkit;
		createContents();
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1);
		composite.setLayoutData(td);
	}
		
	public Composite getContainer()
	{
		return border;
	}
		
	private void createContents()
	{
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;//was 2
		composite.setLayout(layout);
		TableWrapData td=new TableWrapData();
		td.grabHorizontal = true;
		td.align = TableWrapData.FILL;
		
		//searchBtn.setLayoutData(td);
		
		queryText = toolkit.createText(composite, "", SWT.NONE);
		queryText.setLayoutData(td);
		Button searchBtn = toolkit.createButton(composite, "Search", SWT.None);
		searchBtn.addListener(SWT.Selection, listener);
		
				
	}

	public String getQuery()
	{
		return queryText.getText();
	}




	
}
