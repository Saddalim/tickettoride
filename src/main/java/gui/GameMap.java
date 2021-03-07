package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import asset.Board;
import asset.City;
import asset.Geographic;
import asset.Rail;

public class GameMap extends JLabel implements ActionListener
{
	private static final long serialVersionUID = -7095706151752736096L;

	private BufferedImage mapImg = null;
	private Image resizedImg = null;

	protected double scaleX = 1.0;
	protected double scaleY = 1.0;

	private Timer recalculateTimer = new Timer(20, this);

	protected Board board;

	public GameMap()
	{
		recalculateTimer.setRepeats(false);

		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent arg0)
			{
				recalculateTimer.restart();
			}
		});
	}

	public GameMap(String mapFileName)
	{
		this();
		init(mapFileName);
	}

	protected void init(String mapFileName)
	{
		File fBoardFile = new File(mapFileName + ".jpg");
		try
		{
			mapImg = ImageIO.read(fBoardFile);
			System.out.println("Loaded map image file " + fBoardFile.getAbsolutePath());

		}
		catch (IIOException e)
		{
			System.out.println("Cannot read file " + fBoardFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		resizeBoard();
	}

	public void setBoard(Board board)
	{
		this.board = board;
	}

	private void resizeBoard()
	{
		if (mapImg != null)
		{
			resizedImg = mapImg.getScaledInstance(Math.max(getWidth(), 1), Math.max(getHeight(), 1), Image.SCALE_SMOOTH);
			setIcon(new ImageIcon(resizedImg));

			scaleX = (double) getWidth() / (double) mapImg.getWidth();
			scaleY = (double) getHeight() / (double) mapImg.getHeight();
		}
	}

	// Resize timer hit
	public void actionPerformed(ActionEvent arg0)
	{
		resizeBoard();
	}

	protected Geographic getGeoAtPos(double x, double y)
	{
		if (board == null) return null;

		// TODO include z-order
		for (Geographic geo : board.getGeos())
		{
			if (geo.containsPoint(x, y))
			{
				return geo;
			}
		}

		return null;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		if (board != null)
		{
			Graphics2D g2d = (Graphics2D) g;

			// Prioritize cities to be alway on top
			for (Rail rail : board.getRails())
			{
				g2d.scale(scaleX, scaleY);
				rail.draw(g2d);
				g2d.scale(1.0 / scaleX, 1.0 / scaleY);
			}
			for (City city : board.getCities())
			{
				g2d.scale(scaleX, scaleY);
				city.draw(g2d);
				g2d.scale(1.0 / scaleX, 1.0 / scaleY);
			}
		}
	}
}
