package org.eclipse.ecf.provider.twitter.ui.logic;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ecf.internal.provider.twitter.ui.wizards.TwitterConnectWizard;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class TwitterLoginHandler implements IHandler {

	
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}


	public void dispose() {
		// TODO Auto-generated method stub

	}


	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		
		TwitterConnectWizard wizard = new TwitterConnectWizard();
		wizard.init(window.getWorkbench(),selection instanceof IStructuredSelection 
					? (IStructuredSelection)selection: StructuredSelection.EMPTY);
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
		
//		TwitterLoginDialog dialog = new TwitterLoginDialog(shell, this);
//		
//		dialog.open();		
		
		return null;
	}
	
	
	
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}


	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}
	
	
	
	
	
//	//TODO move this method somewhere else! 
//	public boolean login(String username, String password)
//	{
//		
//		connectContext = ConnectContextFactory.createPasswordConnectContext(password);
//
//		try {
//			targetID = IDFactory.getDefault().createID(container.getConnectNamespace(), connectID);
//		} catch (final IDCreateException e) {
//			//new IDCreateErrorDialog(null, connectID, e).open();
//			e.printStackTrace();
//			return false;
//		}
//
//		page.saveComboItems();
//
//		final IPresenceContainerAdapter adapter = (IPresenceContainerAdapter) container.getAdapter(IPresenceContainerAdapter.class);
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
//
//		final IChatManager icm = adapter.getChatManager();
//		
////		icms = icm.getChatMessageSender();
////		itms = icm.getTypingMessageSender();
//
////		icm.addMessageListener(new IIMMessageListener() {
////			public void handleMessageEvent(IIMMessageEvent e) {
////				if (e instanceof IChatMessageEvent) {
////					displayMessage((IChatMessageEvent) e);
////				} else if (e instanceof ITypingMessageEvent) {
////					displayTypingNotification((ITypingMessageEvent) e);
////				}
////			}
////		});
////
////		new AsynchContainerConnectAction(container, targetID, connectContext, null, new Runnable() {
////			public void run() {
////				cachePassword(connectID, password);
////			}
////		}).run();
//
//		return true;
//		
//	}

}
