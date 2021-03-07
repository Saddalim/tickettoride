package gui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class IconManager
{

	private static ImageIcon getIcon(String iconName, int width, int height)
	{
		File imageFile = new File(IconManager.class.getClassLoader().getResource(iconName).getFile());
		try
		{
			return new ImageIcon(ImageIO.read(imageFile).getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void setIconToElement(AbstractButton component, String iconName)
	{
		component.setIcon(getIcon(iconName, component.getWidth(), component.getHeight()));
	}

	public static void setIconToElement(JLabel label, String iconName)
	{
		label.setIcon(getIcon(iconName, label.getWidth(), label.getHeight()));
	}
}
