package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CreateClickElementDialog extends JDialog{
	
	public static final int SAVE = 0;
	public static final int EDIT = 1;
	public static final int CANCEL = 2;
	public static final int NEW = 3;
	
	private CreateClickElementDialog self;
	private int mode;
	
	private JButton saveButton;
	private JButton cancelButton;
	private JLabel tagLabel;
	private JCheckBox attributeCheckBox;
	private JCheckBox textCheckBox;
	private JCheckBox xPathCheckBox;
	private JCheckBox doClickCheckBox;
	private JTextField tagField;
	private JTextField attributeNameField;
	private JTextField attributeValueField;
	private JTextField textField;
	private JTextField xPathField;
	private JLabel attributeNameLabel;
	private JLabel attributeValueLabel;
	
	
	public CreateClickElementDialog(){
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		self = this;
		this.setTitle("Create/Edit Click Element");
		
		tagLabel = new JLabel("HTML Tag: ");
		tagField = new JTextField(10);
		
		doClickCheckBox = new JCheckBox("Click?");
		
		attributeCheckBox = new JCheckBox("Attribute ");
		attributeNameLabel = new JLabel("Name: ");
		attributeValueLabel = new JLabel("Value: ");
		attributeNameField = new JTextField(20);
		attributeValueField = new JTextField(20);
		
		textCheckBox = new JCheckBox("With Text: ");
		textField = new JTextField(20);
		
		xPathCheckBox = new JCheckBox("In XPath: ");
		xPathField = new JTextField(20);
		
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");
		
		doClickCheckBox.setSelected(true);
		xPathCheckBox.setSelected(false);
		xPathField.setEnabled(false);
		textCheckBox.setSelected(false);
		textField.setEnabled(false);
		attributeCheckBox.setSelected(false);
		attributeNameField.setEnabled(false);
		attributeValueField.setEnabled(false);
		
		xPathCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					xPathField.setEnabled(true);
				} else {
					xPathField.setEnabled(false);
					xPathField.setText(null);
				}
			}});
		attributeCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					attributeNameField.setEnabled(true);
					attributeValueField.setEnabled(true);
				} else {
					attributeNameField.setEnabled(false);
					attributeValueField.setEnabled(false);
					attributeNameField.setText(null);
					attributeValueField.setText(null);
				}
			}});
		textCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					textField.setEnabled(true);
				} else {
					textField.setEnabled(false);
					textField.setText(null);
				}
			}});
		
		saveButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				self.setMode(CreateInputSpecDialog.SAVE);
				self.setModal(false);
				self.setVisible(false);
			}
			
		});
		
		cancelButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				self.setMode(CreateInputSpecDialog.CANCEL);
				//Blank out inputs
				doClickCheckBox.setSelected(true);
				xPathCheckBox.setSelected(false);
				xPathField.setText(null);
				xPathField.setEnabled(false);
				textCheckBox.setSelected(false);
				textField.setText(null);
				textField.setEnabled(false);
				attributeCheckBox.setSelected(false);
				attributeNameField.setText(null);
				attributeNameField.setEnabled(false);
				attributeValueField.setText(null);
				attributeValueField.setEnabled(false);
				
				self.setModal(false);
				self.setVisible(false);
			}
			
		});
		
		c.gridx=3;
		c.gridy=0;
		c.gridwidth=1;
		this.add(doClickCheckBox,c);
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=5;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JSeparator(),c);
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		this.add(tagLabel,c);
		c.gridx=1;
		c.gridy=0;
		c.gridwidth=2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(tagField,c);
		c.gridx=0;
		c.gridy=3;
		c.gridwidth=1;
		this.add(attributeCheckBox,c);
		c.gridx=1;
		c.gridy=3;
		c.gridwidth=1;
		this.add(attributeNameLabel,c);
		c.gridx=2;
		c.gridy=3;
		c.gridwidth=2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(attributeNameField,c);
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=1;
		this.add(attributeValueLabel,c);
		c.gridx=2;
		c.gridy=4;
		c.gridwidth=2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(attributeValueField,c);
		c.gridx=0;
		c.gridy=5;
		c.gridwidth=1;
		this.add(textCheckBox,c);
		c.gridx=1;
		c.gridy=5;
		c.gridwidth=4;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(textField,c);
		c.gridx=0;
		c.gridy=6;
		c.gridwidth=1;
		this.add(xPathCheckBox,c);
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=4;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(xPathField,c);
		c.gridx=1;
		c.gridy=7;
		c.gridwidth=1;
		this.add(saveButton,c);
		c.gridx=2;
		c.gridy=7;
		c.gridwidth=1;
		this.add(cancelButton,c);
		
	}
	
	public void setMode(int i){
		mode = i;
	}
	
	public int getMode(){
		return mode;
	}

	
	//Text value accessors
	public String getTextValue(){
		return this.textField.getText();
	}
	
	public void setTextValue(String text){
		this.textField.setText(text);
	}
	
	public boolean isTextEnabled(){
		if(this.textCheckBox.isSelected()) return true;
		else return false;
	}
	
	public void setTextEnabled(boolean b){
		this.textCheckBox.setSelected(b);
		this.textField.setEnabled(b);
	}
	
	// XPath accessors
	public String getXPath(){
		return this.xPathField.getText();
	}
	
	public void setXPath(String path){
		this.xPathField.setText(path);
	}
	
	public boolean isXPathEnabled(){
		if(this.xPathCheckBox.isSelected()) return true;
		else return false;
	}
	
	public void setxPathEnabled(boolean b){
		this.xPathCheckBox.setSelected(b);
		this.xPathField.setEnabled(b);
	}
	
	// Tag
	public String getHTMLTag(){
		return this.tagField.getText();
	}
	
	public void setHTMLTag(String tag){
		this.tagField.setText(tag);
	}
	
	// Do Click?
	
	public boolean isDoClickSelected(){
		if(this.doClickCheckBox.isSelected()) return true;
		else return false;
	}
	
	public void setDoClick(boolean b){
		this.doClickCheckBox.setEnabled(b);
	}
	
	// Attribute
	
	public void setAttributeName(String name){
		this.attributeNameField.setText(name);
	}
	
	public void setAttributeValue(String value){
		this.attributeValueField.setText(value);
	}
	
	public String getAttributeName(){
		return this.attributeNameField.getText();
	}
	
	public String getAttributeValue(){
		return this.attributeValueField.getText();
	}
	
	public boolean isAttributeEnabled(){
		if(this.attributeCheckBox.isSelected()) return true;
		else return false;
	}
	
	public void setAttributeEnabled(boolean b){
		this.attributeCheckBox.setSelected(b);
		this.attributeValueField.setEnabled(b);
		this.attributeNameField.setEnabled(b);
	}
	
	public void reset(){
		this.setHTMLTag(null);
		this.setDoClick(true);
		this.setAttributeEnabled(false);
		this.setAttributeName(null);
		this.setAttributeValue(null);
		this.setTextEnabled(false);
		this.setTextValue(null);
		this.setxPathEnabled(false);
		this.setXPath(null);
	}
	
}
