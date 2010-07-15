package org.eclipse.ecf.provider.twitter.ui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

public class TwitterCache {

	/** Singleton instance. */
	private static TwitterCache instance = null;
	
	/** cached images for user */
	private Map<String, Image> mapUserImages = null;
	/** queue of labels waiting for image for user */
	private Map<String, List<Label>> mapUserImagesLoading = null;

	/** private Constructor */
	private TwitterCache() {
	}

	/** Get singleton instance */
	private static TwitterCache getInstance() {
		if (instance == null) {
			instance = new TwitterCache();
			instance.mapUserImages = new HashMap<String, Image>();
			instance.mapUserImagesLoading = new HashMap<String, List<Label>>();
		}
		return instance;
	}

	/**
	 * Get user image from cache
	 * @param userId
	 * @return
	 */
	public static synchronized Image getUserImage(String userId) {
		return getInstance().mapUserImages.get(userId);
	}

	/**
	 * Queue the label for user image setting
	 * @param message
	 * @param label
	 */
	public static synchronized void queueMessageForUserImageLoading(String userId, 
														String imagePath, Label label) {
		
		//System.err.println("Queuing" + userId + "'s Image");
		List<Label> listLabel = getInstance().mapUserImagesLoading.get(userId);
		if (listLabel==null) {
			
			listLabel = new ArrayList<Label>();
			getInstance().mapUserImagesLoading.put(userId, listLabel);
			URL url;
			try {
				url = new URL(imagePath);
				
				goGetImage(userId, url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		listLabel.add(label);
	}

	/**
	 * Do the stream reading job. When done updates queued Labels
	 * @param userId
	 * @param url
	 */
	private static void goGetImage(final String userId, final URL url) {
		new Job("Images") {
			protected org.eclipse.core.runtime.IStatus run(IProgressMonitor monitor) {
				InputStream is=null ;
				try{
					is = url.openStream();
					Image aux = new Image(Display.getCurrent(), is);
					final Image image = new Image(Display.getCurrent(),aux.getImageData().scaledTo(50, 50));
					//Put resized image into the cache
					getInstance().mapUserImages.put(userId, image);
					//Update waiting labels
					updateUserImages(userId);
				}catch (Exception e){
					e.printStackTrace();
				} finally{
					if(is != null)
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				return Status.OK_STATUS;
			}

			private void updateUserImages(final String userId) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable(){
					public void run() {
						//Get list of labels waiting for the image associated to userId
						List<Label> listLabel = getInstance().mapUserImagesLoading.get(userId);
						if (listLabel!=null) {
							//Get image from cache
							Image image = getInstance().mapUserImages.get(userId);
							//Update and redraw each label waiting for image
							for (Label label : listLabel) {
								label.setImage(image);
								//System.err.println("Updating the image for " + userId);
								label.redraw();
							}
							//Empty list of queued labels
							listLabel.clear();
							//Remove key from queue
							getInstance().mapUserImagesLoading.remove(userId);
						}
					}
				});
			}
		}.schedule();
	}

}
