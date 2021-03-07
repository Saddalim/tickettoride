package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import asset.City;

public class EditCityDialog extends JDialog
{
	private static final long serialVersionUID = 487438547143875542L;

	City city = null;

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;

	/**
	 * 
	 * @wbp.parser.constructor
	 */
	public EditCityDialog(City city)
	{
		this.city = city;
		initGui();
	}

	public EditCityDialog(double x, double y)
	{
		this(new City(x, y));
	}

	private void initGui()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Edit city");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel imageLabel = new JLabel();
			imageLabel.setBounds(324, 5, 100, 100);
			contentPanel.add(imageLabel);
			IconManager.setIconToElement(imageLabel, "city.png");
		}
		{
			JPanel infoPanel = new JPanel();
			infoPanel.setBounds(5, 5, 309, 219);
			contentPanel.add(infoPanel);
			infoPanel
			        .setLayout(new FormLayout(
			                new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.PREF_COLSPEC,
			                        FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("126px"), },
			                new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("20px"), }));
			{
				JLabel lblName = new JLabel("Name");
				infoPanel.add(lblName, "2, 2, left, center");
			}
			{
				textFieldName = new JTextField();
				infoPanel.add(textFieldName, "4, 2, left, top");
				textFieldName.setColumns(15);
				textFieldName.setText(city.getName());
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						String cityName = textFieldName.getText().trim();
						if (cityName.length() > 0)
						{
							city.setName(textFieldName.getText());
							setVisible(false);
						}
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
						city = null;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}

	public City getCity()
	{
		return city;
	}

}
