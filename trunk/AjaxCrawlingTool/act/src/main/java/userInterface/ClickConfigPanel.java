package userInterface;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import crawler.CrawlRequest;
import crawler.PageElement;

@SuppressWarnings("serial")
public class ClickConfigPanel extends JPanel {

	protected JTable table;
	private JScrollPane scroller;
	private static Vector<String> columnNames;
	protected ClickElementTable model;
	private CreateClickElementDialog dialog;
	private JCheckBox defaultClicks;
	
	static {
        columnNames = new Vector<String>();
        columnNames.add("Tag");
        columnNames.add("Attribute Name");
        columnNames.add("Attribute Value");
        columnNames.add("Text");
        columnNames.add("XPath");
        columnNames.add("Click?");
	}
	
	public ClickConfigPanel(){
		super(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		model = new ClickElementTable(new Vector<Object[]>(),columnNames);
		table = new JTable(model);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		scroller = new JScrollPane(table);
		
		dialog = new CreateClickElementDialog();
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
        dialog.setLocationRelativeTo(this);
		
		Dimension d = scroller.getPreferredSize();
		d.height = 150;
		scroller.setPreferredSize(d);
		
		TableColumn col1 = table.getColumnModel().getColumn(1);
		TableColumn col2 = table.getColumnModel().getColumn(2);
		int width = col1.getWidth() + 30;
		col1.setPreferredWidth(width);
		col2.setPreferredWidth(width);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.setFillsViewportHeight(true);
	
		JButton addButton = new JButton("New");
		JButton deleteButton = new JButton("Delete");
		JButton editButton = new JButton("Edit");
		addButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				dialog.setModal(true);
				dialog.setVisible(true);
				
				if(dialog.getMode() == CreateInputSpecDialog.SAVE){
					if(!dialog.getHTMLTag().isEmpty()){
						
						PageElement newElement = new PageElement();
						newElement.setDoClick(dialog.isDoClickSelected());
						newElement.setTag(dialog.getHTMLTag());
						
						if(dialog.isXPathEnabled() && !dialog.getXPath().isEmpty()){ 
							newElement.setXPath(dialog.getXPath()); 
						}
						
						if(dialog.isAttributeEnabled() && !dialog.getAttributeName().isEmpty() && !dialog.getAttributeValue().isEmpty()){
							newElement.setAttributeName(dialog.getAttributeName());
							newElement.setAttributeValue(dialog.getAttributeValue());
						}
						
						if(dialog.isTextEnabled() && !dialog.getTextValue().isEmpty()){
							newElement.setText(dialog.getTextValue());
						}
						
						CrawlerGUI.REQUEST.addClickElement(newElement);
						model.addRow(newElement.getTag(),newElement.getAttributeName(),newElement.getAttributeValue(),newElement.getText(),newElement.getXPath(),new Boolean(newElement.doClick()));
						
						
					} else {
						JOptionPane.showMessageDialog(null, "Failed to add: Tag name is required.");
					}
				}
				
				dialog.reset();
				
			}
		});
		
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow()+CrawlRequest.DEFAULT_ELEMENTS.length;
				int unadjustedIndex = table.getSelectedRow();
				if(index >= 0){
					model.removeRow(unadjustedIndex);
					CrawlerGUI.REQUEST.getClickElements().remove(index);
				}
			}			
		});
		
		editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow()+CrawlRequest.DEFAULT_ELEMENTS.length;
				int unadjustedIndex = table.getSelectedRow();
				if(index >= 0){
					
					//Get the element to edit
					PageElement inputToEdit = CrawlerGUI.REQUEST.getClickElements().get(index);
					
					//Populate the edit box
					dialog.setHTMLTag(inputToEdit.getTag());
					dialog.setDoClick(inputToEdit.doClick());
					
					if(!inputToEdit.getText().isEmpty()){
						dialog.setTextEnabled(true);
						dialog.setTextValue(inputToEdit.getText());
					}
					
					if(!inputToEdit.getXPath().isEmpty()){
						dialog.setxPathEnabled(true);
						dialog.setXPath(inputToEdit.getXPath());
					}
					
					if(!inputToEdit.getAttributeName().isEmpty() && !inputToEdit.getAttributeValue().isEmpty()){
						dialog.setAttributeEnabled(true);
						dialog.setAttributeName(inputToEdit.getAttributeName());
						dialog.setAttributeValue(inputToEdit.getAttributeValue());
					}
					
					//Show the Edit box
					dialog.setModal(true);
					dialog.setVisible(true);
					
					if(dialog.getMode() == CreateInputSpecDialog.SAVE){
					//Edit
						if(!dialog.getHTMLTag().isEmpty()){
							inputToEdit.setTag(dialog.getHTMLTag());
							inputToEdit.setDoClick(dialog.isDoClickSelected());
							inputToEdit.setAttributeName(dialog.getAttributeName());
							inputToEdit.setAttributeValue(dialog.getAttributeValue());
							inputToEdit.setText(dialog.getTextValue());
							inputToEdit.setXPath(dialog.getXPath());
							model.setValueAt(dialog.getHTMLTag(), unadjustedIndex, 0);
							model.setValueAt(dialog.getAttributeName(), unadjustedIndex, 1);
							model.setValueAt(dialog.getAttributeValue(), unadjustedIndex, 2);
							model.setValueAt(dialog.getTextValue(), unadjustedIndex, 3);
							model.setValueAt(dialog.getXPath(), unadjustedIndex, 4);
							model.setValueAt(new Boolean(dialog.isDoClickSelected()), unadjustedIndex, 5);
						} else {
							JOptionPane.showMessageDialog(null, "Failed to edit: Tag name cannot be empty.");
						}
					}
					
					dialog.reset();
				}
				
			}			
		});
		
		defaultClicks = new JCheckBox("Click Default Elements (all anchors and buttons)");
		defaultClicks.setSelected(true);
		
		c.gridx=0;
		c.gridy=0;
		this.add(defaultClicks,c);
		c.gridx=0;
		c.gridy=1;
		c.gridwidth = 12;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JSeparator(),c);
		c.gridx=11;
		c.gridy=2;
		this.add(addButton,c);
		c.gridx=11;
		c.gridy=3;
		this.add(editButton,c);
		c.gridx=11;
		c.gridy=4;
		this.add(deleteButton,c);
		c.gridx=0;
		c.gridy=2;
		c.gridwidth=10;
		c.gridheight=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		this.add(scroller,c);
		
		
		// Uses check box in the last column
		table.getColumnModel().
        getColumn(5).setCellRenderer(
  				new TableCellRenderer() {
                              // the method gives the component  like whome the cell must be rendered
                              public Component getTableCellRendererComponent(
  							JTable table, Object value, boolean isSelected,
  							boolean isFocused, int row, int col) {
  						boolean marked = (Boolean) value;
  						JCheckBox rendererComponent = new JCheckBox();
  						if (marked) {
  							rendererComponent.setSelected(true);
  						}
  						return rendererComponent;
  					}
  				});
	}
	
	
	public boolean isDefaultClicksEnabled(){
		return this.defaultClicks.isSelected();
	}
	
	public void setDefaultClicksEnabled(boolean b){
		this.defaultClicks.setSelected(b);
	}
	
}
