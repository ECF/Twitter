package org.eclipse.ecf.provider.twitter.ui.utils;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class ImageUtils {

	
	public static Image loadImage(String img)
	{
		Bundle bundle = Platform.getBundle("org.eclipse.ecf.provider.twitter.ui.hub"); // aka your plugin's id
		IPath imagePath = new Path("icons/" + img);
		URL imageUrl = Platform.find(bundle, imagePath);
		ImageDescriptor desc = ImageDescriptor.createFromURL(imageUrl);
		Image image = desc.createImage();
		return image;
	}
	
}
