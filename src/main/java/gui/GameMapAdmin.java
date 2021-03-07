package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import asset.Board;
import asset.City;
import asset.Geographic;
import asset.Rail;

public class GameMapAdmin extends GameMap
{
	private static final long serialVersionUID = 614606206646376898L;

	MapEditorWindow editor;
	City newRailNode1 = null;

	public GameMapAdmin(MapEditorWindow editor)
	{
		this.editor = editor;

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				double realX = e.getX() / scaleX;
				double realY = e.getY() / scaleY;

				// Find element that was clicked on
				Geographic clickedGeo = null;
				for (Geographic geo : board.getGeos())
				{
					if (geo.containsPoint(realX, realY))
					{
						clickedGeo = geo;
						break;
					}
				}

				System.out.println("Click on " + clickedGeo);

				switch (editor.getCursorState())
				{
					case Normal:
						if (clickedGeo != null) if (e.getButton() == 1)
							openEditDialogFor(clickedGeo);
						else
							openDeleteDialogFor(clickedGeo);
						break;
					case AddCity:
						EditCityDialog newCityDialog = new EditCityDialog(realX, realY);
						newCityDialog.setVisible(true);
						City newCity = newCityDialog.getCity();
						System.out.println("Got city: " + newCity);
						if (newCity != null) board.addCity(newCity);
						break;
					case AddRailNode1:
						if (clickedGeo != null && clickedGeo instanceof City)
						{
							newRailNode1 = (City) clickedGeo;
							editor.refreshCursorState();
						}
						break;
					case AddRailNode2:
						if (clickedGeo != null && clickedGeo instanceof City)
						{
							if (newRailNode1 == null)
							{
								System.out.println("New rail node1 is null. Corrupt cursor state!");
								return;
							}
							EditRailDialog newRailDialog = new EditRailDialog(newRailNode1, (City) clickedGeo);
							newRailDialog.setVisible(true);
							Rail newRail = newRailDialog.getRail();
							System.out.println("Got rail: " + newRail);
							if (newRail != null)
							{
								board.addRail(newRail);
								newRailNode1 = null;
								editor.refreshCursorState();
							}
						}
						break;
					default:
						break;
				}

				repaint();
			}
		});
	}

	public GameMapAdmin(Board board)
	{
		this.board = board;
	}

	public GameMapAdmin(String mapName)
	{
		super(mapName);
	}

	public void setMapFileName(String mapFileName)
	{
		init(mapFileName);
	}

	public boolean isRailNode1Selected()
	{
		return newRailNode1 != null;
	}

	protected void openEditDialogFor(Geographic geo)
	{
		// TODO this can be replaced via a magnificent Geo-template superclass for edit dialogs
		if (geo instanceof City)
			new EditCityDialog((City) geo).setVisible(true);
		else if (geo instanceof Rail) new EditRailDialog((Rail) geo).setVisible(true);
	}

	protected void openDeleteDialogFor(Geographic clickedGeo)
	{
		if (0 == JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + clickedGeo + "?", "Delete Geo",
		        JOptionPane.YES_NO_OPTION))
		{
			board.removeGeo(clickedGeo);
			repaint();
		}
	}
}
