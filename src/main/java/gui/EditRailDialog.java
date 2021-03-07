package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import asset.City;
import asset.Rail;
import asset.Rail.RailColor;

public class EditRailDialog extends JDialog
{
	private static final long serialVersionUID = 1832706143640265234L;

	private Rail rail;

	private final JPanel contentPanel = new JPanel();
	JSpinner spinLength;
	JSpinner spinJokers;
	JComboBox<RailColor> cbColor;
	JCheckBox cbTunnel;

	/**
	 * 
	 * @wbp.parser.constructor
	 */
	public EditRailDialog(Rail rail)
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		this.rail = rail;
		initGui();
	}

	public EditRailDialog(City node1, City node2)
	{
		this(new Rail(node1, node2));
	}

	private void initGui()
	{
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel imageLabel = new JLabel("");
		imageLabel.setBounds(322, 11, 100, 100);
		IconManager.setIconToElement(imageLabel, "rail.png");
		contentPanel.add(imageLabel);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 302, 207);
		contentPanel.add(panel);
		panel.setLayout(new FormLayout(
		        new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC, ColumnSpec.decode("6dlu"),
		                ColumnSpec.decode("default:grow"), },
		        new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
		                FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
		                FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
		                FormSpecs.DEFAULT_ROWSPEC, }));
		{
			JLabel lblFrom = new JLabel("From");
			panel.add(lblFrom, "2, 2, right, default");
		}

		JLabel lblNode1Name = new JLabel("");
		panel.add(lblNode1Name, "4, 2");
		lblNode1Name.setText(rail.getN1().getName());
		{
			JLabel lblTo = new JLabel("To");
			panel.add(lblTo, "2, 4, right, default");
		}

		JLabel lblNode2Name = new JLabel("");
		panel.add(lblNode2Name, "4, 4");
		lblNode2Name.setText(rail.getN2().getName());
		{
			JLabel lblLength = new JLabel("Length");
			panel.add(lblLength, "2, 6, right, default");
		}

		spinLength = new JSpinner();
		spinLength.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				if (spinJokers != null)
				{
					int newValue = (int) spinLength.getValue();
					SpinnerNumberModel jokersModel = (SpinnerNumberModel) spinJokers.getModel();
					jokersModel.setMaximum(newValue);
					if ((int) jokersModel.getValue() > newValue) jokersModel.setValue(newValue);
				}
			}
		});
		spinLength.setModel(new SpinnerNumberModel(1, 1, 6, 1));
		spinLength.getModel().setValue(rail.getLength());
		panel.add(spinLength, "4, 6");

		JLabel lblJokers = new JLabel("Jokers");
		panel.add(lblJokers, "2, 8, right, default");

		spinJokers = new JSpinner();
		spinJokers.setModel(new SpinnerNumberModel(0, 0, (int) spinLength.getValue(), 1));
		spinJokers.setValue(rail.getJokersNeeded());
		panel.add(spinJokers, "4, 8");

		JLabel lblColor = new JLabel("Color");
		panel.add(lblColor, "2, 10, right, default");

		cbColor = new JComboBox<>();
		panel.add(cbColor, "4, 10, fill, default");
		for (RailColor color : RailColor.values())
			cbColor.addItem(color);
		cbColor.setSelectedItem(rail.getColor());

		JLabel lblTunnel = new JLabel("Tunnel");
		panel.add(lblTunnel, "2, 12, right, default");

		cbTunnel = new JCheckBox("");
		cbTunnel.setSelected(rail.isTunnel());
		panel.add(cbTunnel, "4, 12");
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						rail.setLength((int) spinLength.getValue());
						rail.setJokersNeeded((int) spinJokers.getValue());
						rail.setColor((RailColor) cbColor.getSelectedItem());
						rail.setTunnel(cbTunnel.isSelected());
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						rail = null;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public Rail getRail()
	{
		return rail;
	}
}
