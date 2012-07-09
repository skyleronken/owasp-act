package userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import crawler.PageInput;

@SuppressWarnings("serial")
public class InputSpecPanel extends JPanel {

	protected JTable table;
	private JScrollPane scroller;
	private static Vector<String> columnNames;
	protected InputSpecTable model;
	private CreateInputSpecDialog dialog;
	private JCheckBox randomInput;
	
	static {
        columnNames = new Vector<String>();
        columnNames.add("Field Name");
        columnNames.add("Field Value");
	}
	
	public InputSpecPanel(){
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		dialog = new CreateInputSpecDialog();
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
        dialog.setLocationRelativeTo(this);
		
		model = new InputSpecTable(new Vector<String[]>(),columnNames);
		table = new JTable(model);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		scroller = new JScrollPane(table);
		
		Dimension d = scroller.getPreferredSize();
		d.height = 150;
		scroller.setPreferredSize(d);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
	
		JButton addButton = new JButton("New");
		addButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				dialog.setModal(true);
				dialog.setVisible(true);
				
				if(dialog.getMode() == CreateInputSpecDialog.SAVE){
					if(!dialog.getFieldName().isEmpty()){
	
						PageInput newInput = new PageInput();
						newInput.setFieldName(dialog.getFieldName());
						newInput.setValue(dialog.getFieldValue());
						
						CrawlerGUI.REQUEST.addSpecifiedInput(newInput);
						model.addRow(newInput.getFieldName(),newInput.getValue());
						
						dialog.setFieldName(null);
						dialog.setFieldValue(null);
					} else {
						JOptionPane.showMessageDialog(null, "Failed to add: Field name is required.");
					}
				}
				
				dialog.reset();
			}
		});
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if(index >= 0){
					model.removeRow(index);
					CrawlerGUI.REQUEST.removeSpecifiedInput(index);
				}
			}			
		});
		
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if(index >= 0){
					
					//Get the element to edit
					PageInput inputToEdit = CrawlerGUI.REQUEST.getSpecifiedInputs().get(index);
					
					//Populate the edit box
					dialog.setFieldName(inputToEdit.getFieldName());
					dialog.setFieldValue(inputToEdit.getValue());
					
					//Show the Edit box
					dialog.setModal(true);
					dialog.setVisible(true);
					
					if(dialog.getMode() == CreateInputSpecDialog.SAVE){
					//Edit
						if(!dialog.getFieldName().isEmpty()){
							inputToEdit.setFieldName(dialog.getFieldName());
							inputToEdit.setValue(dialog.getFieldValue());
							model.setValueAt(dialog.getFieldName(), index, 0);
							model.setValueAt(dialog.getFieldValue(), index, 1);
						} else {
							JOptionPane.showMessageDialog(null, "Failed to edit: Field name cannot be empty.");
						}
					} 
					
					dialog.reset();
					
				}
			}			
		});
		
		randomInput = new JCheckBox("Use Random Input");
		randomInput.setToolTipText("Input fields will be filled with random text");
		randomInput.setSelected(true);
		
		c.gridx=0;
		c.gridy=0;
		this.add(randomInput,c);
		c.gridx=0;
		c.gridy=1;
		c.gridwidth = 12;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JSeparator(),c);
		c.gridx=9;
		c.gridy=2;
		this.add(addButton,c);
		c.gridx=9;
		c.gridy=3;
		this.add(editButton,c);
		c.gridx=9;
		c.gridy=4;
		this.add(deleteButton,c);
		c.gridx=0;
		c.gridy=2;
		c.gridwidth=8;
		c.gridheight=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		this.add(scroller,c);
	}
	
	public boolean isRandomInputEnabled(){
		return this.randomInput.isSelected();
	}
	
	public void setRandomInputEnabled(boolean b){
		this.randomInput.setSelected(b);
	}
	
}
