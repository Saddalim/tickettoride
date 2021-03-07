package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BoardWindow extends JFrame
{
	private static final long serialVersionUID = 4035831791383782607L;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public BoardWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuGrpFile = new JMenu("File");
		menuBar.add(menuGrpFile);

		JMenuItem menuExit = new JMenuItem("Exit");
		menuGrpFile.add(menuExit);

		JMenu menuGrpAdmin = new JMenu("Admin");
		menuBar.add(menuGrpAdmin);

		JMenuItem menuAdminEdit = new JMenuItem("Edit current map");
		menuGrpAdmin.add(menuAdminEdit);

		JMenuItem menuAdminNew = new JMenuItem("Create new map");
		menuGrpAdmin.add(menuAdminNew);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel playersPanel = new JPanel();
		contentPane.add(playersPanel, BorderLayout.SOUTH);

		JPanel decksPanel = new JPanel();
		contentPane.add(decksPanel, BorderLayout.NORTH);

		JPanel leftPanel = new JPanel();
		contentPane.add(leftPanel, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();
		contentPane.add(rightPanel, BorderLayout.EAST);

		JPanel boardPanel = new JPanel();
		contentPane.add(boardPanel, BorderLayout.CENTER);
	}

}
