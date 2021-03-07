package com.saddcomp.tickettoride;

import java.awt.EventQueue;

import gui.MainWindow;

/**
 * Hello world!
 *
 */
public class App
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
