package userInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import crawler.CrawlRequest;

@SuppressWarnings("serial")
public class OutputModePanel extends JPanel{
	
	private static String[] OUTPUT_MODES = {"Standard", "Flat/CSV"};//, "SQLite" };
	private static int[] OUTPUT_MODES_ARRAY = {CrawlRequest.STDOUT,CrawlRequest.CSV};//, CrawlRequest.DB };
	
	private OutputModePanel self;
	private JComboBox outputModeCombo;
	private JPanel outputModePanel;
	private JLabel typeLabel = new JLabel("Output Mode:");
	private JTextArea textArea;
	private JFileChooser fileChooser;
	private JButton saveLocationButton;
	private JTextField saveLocationField;
	private JPanel outputConfigPanel;
	private JButton clearButton;
	private JScrollPane scrollPane;
	private JCheckBox replaceTextBox;
	private int stateCounter = 0;
	
	public OutputModePanel(){
		super(new GridBagLayout());
		
		self = this;
		
		outputModePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		outputConfigPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Log");
		outputModePanel.setBorder(title);
		
		title = BorderFactory.createTitledBorder("Details");
		outputConfigPanel.setBorder(title);
		outputConfigPanel.setPreferredSize(new Dimension(400, 60));
		
		clearButton = new JButton("Clear Log");
		Dimension size = clearButton.getPreferredSize ();
		size.height = 20;
		size.width = 50;
		clearButton.setPreferredSize (size);
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				self.textArea.setText("");
				self.stateCounter = 0;
			}
		});
		
		outputModeCombo = new JComboBox(OUTPUT_MODES);
		outputModeCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int outputMode = OUTPUT_MODES_ARRAY[cb.getSelectedIndex()];
				switch(outputMode){
				
					case CrawlRequest.STDOUT: 	self.setOutputStandard();
												break;
					case CrawlRequest.CSV:  	self.setOutputCSV();
												break;
					case CrawlRequest.DB: 		self.setOutputSQLite();
												break;
					default: 					break;
				
				}
			}			
		});
		
		textArea = new JTextArea(10, 50);
		scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		replaceTextBox = new JCheckBox("Parsable Parameters");
		replaceTextBox.setToolTipText("Will replace URL parameter input with a recognizable parameter: " + CrawlRequest.PARSABLE_STRING);
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridheight = 5;
		gc.fill = GridBagConstraints.BOTH;
		outputModePanel.add(scrollPane,gc);	
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;	
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(typeLabel,c);
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(outputModeCombo,c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 5;
		this.add(replaceTextBox,c);
		c.gridx = 5;
		c.gridy = 0;
		c.gridheight = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(outputConfigPanel,c);
		c.gridx = 0;
		c.gridy = 15;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(clearButton,c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(outputModePanel,c);
		
		saveLocationField = new JTextField(20);
		saveLocationButton = new JButton("Save To...");
		fileChooser = new JFileChooser();
		saveLocationButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showDialog(self,"Choose File");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            String file = fileChooser.getSelectedFile().getAbsolutePath();
		            self.setOutputFilePath(file);
			    }
			}
		});
	}

	protected void setOutputStandard(){
		outputConfigPanel.removeAll();
		this.repaint();
		this.revalidate();
	}
	
	protected void setOutputCSV(){
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx=0;
		gc2.gridy=0;
		outputConfigPanel.add(saveLocationButton,gc2);
		gc2.gridx = 1;
		gc2.gridy = 0;
		gc2.anchor = GridBagConstraints.FIRST_LINE_START;
		outputConfigPanel.add(saveLocationField,gc2);
		this.repaint();
		this.revalidate();
	}
	
	protected void setOutputSQLite(){
		
	}
	
	public void addLineToStandardOutput(String line){
		JScrollBar vbar = self.scrollPane.getVerticalScrollBar();
		boolean autoScroll = ((vbar.getValue() + vbar.getVisibleAmount()) == vbar.getMaximum());
		self.textArea.append("["+ ++self.stateCounter + "] " + line + "\n");
		if( autoScroll ) self.textArea.setCaretPosition( self.textArea.getDocument().getLength() );
	}

	public int getOutputMode() {
		return OUTPUT_MODES_ARRAY[self.outputModeCombo.getSelectedIndex()];
	}

	public void setOutputMode(int outputMode) {
		self.outputModeCombo.setSelectedIndex(outputMode);
		switch(outputMode){
		case CrawlRequest.CSV: 		self.setOutputCSV();
									break;
		case CrawlRequest.STDOUT: 	self.setOutputStandard();
									break;
		case CrawlRequest.DB:		self.setOutputSQLite();
									break;
		}
	}
	
	public String getOutputFilePath(){
		return self.saveLocationField.getText();
	}
	
	public void setOutputFilePath(String path) {
		String pathToSet = path;
		if(!pathToSet.endsWith(".csv")){
			pathToSet = pathToSet.concat(".csv");
		}
		self.saveLocationField.setText(pathToSet);
	}
	
	public void setParsableInput(boolean b){
		self.replaceTextBox.setSelected(b);
	}
	
	public boolean getParsableInput(){
		return self.replaceTextBox.isSelected();
	}
	
}
