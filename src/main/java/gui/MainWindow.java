package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = -1810393566512302281L;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public MainWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel headerPane = new JPanel();
		contentPane.add(headerPane, BorderLayout.NORTH);

		JLabel lblTicketToRide = new JLabel("Ticket To Ride - GSZ Online Edition");
		headerPane.add(lblTicketToRide);

		JPanel footerPane = new JPanel();
		contentPane.add(footerPane, BorderLayout.SOUTH);

		JPanel leftPane = new JPanel();
		contentPane.add(leftPane, BorderLayout.WEST);

		JPanel rightPane = new JPanel();
		contentPane.add(rightPane, BorderLayout.EAST);

		JPanel centerPane = new JPanel();
		contentPane.add(centerPane, BorderLayout.CENTER);
		centerPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnNewButton = new JButton("Offline game");
		centerPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Online Game");
		centerPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Map Editor");
		btnNewButton_2.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				new MapEditorWindow().setVisible(true);
			}
		});
		centerPane.add(btnNewButton_2);
	}

}
