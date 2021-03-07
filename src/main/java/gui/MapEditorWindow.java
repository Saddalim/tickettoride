package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import asset.Board;
import configuration.BoardParser;

public class MapEditorWindow extends JDialog
{
	private static final long serialVersionUID = -9068479132472388565L;

	private static final int ctrlBtnHeight = 25;

	public enum CursorState
	{
		Normal, AddCity, AddRailNode1, AddRailNode2
	}

	private JPanel contentPane;

	Board board = null;
	GameMapAdmin adminMap;
	private JTextField textFieldBoardFileName;
	private JTextField textFieldBoardTitle;

	JToggleButton tglbtnNewCity;
	JToggleButton tglbtnNewRail;

	CursorState cursorState = CursorState.Normal;

	/**
	 * Create the frame.
	 */
	public MapEditorWindow()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 638, 444);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewMap = new JMenuItem("New map");
		mntmNewMap.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String mapName = JOptionPane.showInputDialog(null, "New map name?", "Name your map", JOptionPane.QUESTION_MESSAGE);
				if (mapName != null)
				{
					setBoard(new Board(mapName));
				}
			}
		});
		mntmNewMap.setIcon(new ImageIcon(MapEditorWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		mnFile.add(mntmNewMap);

		JMenuItem mntmSaveMap = new JMenuItem("Save map");
		mntmSaveMap.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (board != null)
				{
					board.setTitle(textFieldBoardTitle.getText());
					board.setFileName(textFieldBoardFileName.getText());
					board.saveToFile();
				}
			}
		});
		mntmSaveMap.setIcon(new ImageIcon(MapEditorWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		mnFile.add(mntmSaveMap);

		JMenuItem mntmOpenMap = new JMenuItem("Open map");
		mntmOpenMap.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String[] savedMaps = BoardParser.getSavedMaps();
				String mapName = (String) JOptionPane.showInputDialog(null, "Name of the map to be loaded?", "Open map",
				        JOptionPane.QUESTION_MESSAGE, null, savedMaps, savedMaps[0]);
				if (mapName != null)
				{
					Board board = BoardParser.loadBoardFromFile(mapName);
					if (board != null) setBoard(board);
				}
			}
		});
		mntmOpenMap.setIcon(new ImageIcon(MapEditorWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		mnFile.add(mntmOpenMap);

		mnFile.addSeparator();

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				dispose();
			}
		});
		mntmExit.setIcon(new ImageIcon(MapEditorWindow.class.getResource("/com/sun/java/swing/plaf/motif/icons/Error.gif")));
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		JButton btnReload = new JButton("Reload");
		btnReload.setBounds(0, 0, ctrlBtnHeight, ctrlBtnHeight);
		IconManager.setIconToElement(btnReload, "reload.png");
		btnReload.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				reloadMap();
			}
		});
		bottomPanel.add(btnReload);

		tglbtnNewCity = new JToggleButton("New city");
		tglbtnNewCity.setBounds(0, 0, ctrlBtnHeight, ctrlBtnHeight);
		IconManager.setIconToElement(tglbtnNewCity, "city.png");
		tglbtnNewCity.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				if (ev.getStateChange() == ItemEvent.SELECTED)
				{
					System.out.println("New city mode enabled");
					tglbtnNewRail.setSelected(false);
				}
				else if (ev.getStateChange() == ItemEvent.DESELECTED)
				{
					System.out.println("New city mode disabled");
				}
				refreshCursorState();
			}
		});
		bottomPanel.add(tglbtnNewCity);

		tglbtnNewRail = new JToggleButton("New rail");
		tglbtnNewRail.setBounds(0, 0, ctrlBtnHeight, ctrlBtnHeight);
		IconManager.setIconToElement(tglbtnNewRail, "rail.png");
		bottomPanel.add(tglbtnNewRail);
		tglbtnNewRail.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				if (ev.getStateChange() == ItemEvent.SELECTED)
				{
					System.out.println("New segment mode enabled");
					tglbtnNewCity.setSelected(false);
				}
				else if (ev.getStateChange() == ItemEvent.DESELECTED)
				{
					System.out.println("New segment mode disabled");
				}
				refreshCursorState();
			}
		});

		JLabel lblInfo = new JLabel("");
		bottomPanel.add(lblInfo);

		JPanel leftPanel = new JPanel();
		contentPane.add(leftPanel, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();
		contentPane.add(rightPanel, BorderLayout.EAST);

		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("Title");
		topPanel.add(lblTitle);

		textFieldBoardTitle = new JTextField();
		topPanel.add(textFieldBoardTitle);
		textFieldBoardTitle.setColumns(20);

		JLabel lblFilename = new JLabel("Filename");
		topPanel.add(lblFilename);

		textFieldBoardFileName = new JTextField();
		topPanel.add(textFieldBoardFileName);
		textFieldBoardFileName.setColumns(15);

		JButton btnSet = new JButton("Set");
		btnSet.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if (board != null)
				{
					board.setTitle(textFieldBoardTitle.getText());
					board.setFileName(textFieldBoardFileName.getText());
					reloadMap();
				}
			}
		});
		topPanel.add(btnSet);

		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		adminMap = new GameMapAdmin(this);
		centerPanel.add(adminMap);
	}

	private void setBoard(Board board)
	{
		System.out.println("Setting board " + board);
		this.board = board;
		adminMap.setBoard(board);
		textFieldBoardTitle.setText(board.getTitle());
		textFieldBoardFileName.setText(board.getFileName());
		reloadMap();
	}

	public CursorState getCursorState()
	{
		return cursorState;
	}

	private void reloadMap()
	{
		if (board != null)
		{
			board.setFileName(textFieldBoardFileName.getText());
			adminMap.setMapFileName(board.getFileName());
		}
	}

	public void refreshCursorState()
	{
		if (tglbtnNewCity.isSelected() && !tglbtnNewRail.isSelected())
			cursorState = CursorState.AddCity;
		else if (!tglbtnNewCity.isSelected() && tglbtnNewRail.isSelected())
			if (adminMap.isRailNode1Selected())
				cursorState = CursorState.AddRailNode2;
			else
				cursorState = CursorState.AddRailNode1;
		else
			cursorState = CursorState.Normal;
		System.out.println("Cursor state change to " + cursorState);
	}

}
