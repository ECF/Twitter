import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.internal.provider.twitter.ui.Activator;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.ui.roster.AbstractRosterContributionItem;
import org.eclipse.ecf.provider.twitter.container.TwitterContainer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

public class TwitterUpdateContributionItem extends AbstractRosterContributionItem {

	public TwitterUpdateContributionItem() {
	}

	public TwitterUpdateContributionItem(String id) {
		super(id);
	}

	protected IAction[] makeActions() {
		final IRoster roster = getSelectedRoster();
		if (roster != null) {
			// Roster is selected for twitter
			final IContainer c = getContainerForRoster(roster);
			if (c != null && c instanceof TwitterContainer) {
				return createActionUpdate((TwitterContainer) c);
			}
		}
		return null;
	}

	/**
	 * @param c
	 * @return
	 */
	private IAction[] createActionUpdate(final TwitterContainer c) {
		final Action action = new Action() {
			public void run() {
				final InputDialog id = new InputDialog(null, "Send Twitter Status Update", "Status", "", new IInputValidator() {
					public String isValid(String newText) {
						if (newText != null && (newText.length() < TwitterContainer.MAX_STATUS_LENGTH))
							return null;
						return "Over maximum character limit (" + TwitterContainer.MAX_STATUS_LENGTH + ")";
					}
				});
				id.setBlockOnOpen(true);
				final int result = id.open();
				final String status = id.getValue();
				if (result == Window.OK && status != null) {
					try {
						c.sendStatusUpdate(status);
					} catch (final ECFException e) {
						ErrorDialog.openError(null, "Twitter Status Update Error", "Status update cannot be sent", new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Status update cannot be sent", e));
					}
				}
			}
		};
		action.setText("Send Twitter Status Update...");
		return new IAction[] {action};
	}
}
