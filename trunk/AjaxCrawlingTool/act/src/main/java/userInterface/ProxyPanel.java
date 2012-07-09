package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;

@SuppressWarnings("serial")
public class ProxyPanel extends JPanel {

	private static String[] typeStrings = {"Manual (Firefox Only)","System"};
	private static ProxyType[] typeArray = {ProxyType.MANUAL,ProxyType.SYSTEM_DEFAULT};
	private JPanel proxySettingsPanel;
	private JTextField hostField;
	private JTextField portField;
	private JCheckBox useProxyCheckBox;
	private JComboBox proxyTypeBox;
	private ProxyPanel self;
	
	public ProxyPanel(){
		super(new GridBagLayout());
		self = this;
		GridBagConstraints c = new GridBagConstraints();
		
		useProxyCheckBox = new JCheckBox("Use Proxy");
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Proxy Settings");
		
		proxySettingsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		JLabel typeLabel = new JLabel("Proxy Type: ");
		JLabel hostLabel = new JLabel("Proxy URL: ");
		JLabel portLabel = new JLabel("Proxy Port: ");
		
		proxyTypeBox = new JComboBox(typeStrings);
		
		hostField = new JTextField();
		portField = new JTextField();
		
		hostField.setColumns(30);
		portField.setColumns(6);
		
		proxyTypeBox.setEnabled(false);
		hostField.setEnabled(false);
		portField.setEnabled(false);
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		proxySettingsPanel.add(typeLabel,gc);
		gc.gridx = 1;
		gc.gridy = 0;
		gc.gridwidth = 8;
		gc.fill = GridBagConstraints.HORIZONTAL;
		proxySettingsPanel.add(proxyTypeBox,gc);
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		proxySettingsPanel.add(hostLabel,gc);
		gc.gridx = 1;
		gc.gridy = 1;
		gc.gridwidth = 3;
		proxySettingsPanel.add(hostField,gc);
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		proxySettingsPanel.add(portLabel,gc);
		gc.gridx = 1;
		gc.gridy = 2;
		gc.gridwidth = 3;
		proxySettingsPanel.add(portField,gc);
		
		useProxyCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					self.setProxyEnabled(true);
					if(self.getProxyType() == ProxyType.MANUAL){
						self.promptForBrowserChange();
					}
				} else {
					self.setProxyEnabled(false);
				}
			}});
		
		proxyTypeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				self.setProxyType(self.getProxyType());
			}
		});
		
		proxySettingsPanel.setBorder(title);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(useProxyCheckBox,c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(proxySettingsPanel,c);
	}
	
	public boolean isProxyEnabled(){
		return this.useProxyCheckBox.isSelected();
	}
	
	public void setProxyEnabled(boolean b){
		this.useProxyCheckBox.setSelected(b);
		this.proxyTypeBox.setEnabled(b);
		if(this.getProxyType() == ProxyType.MANUAL){
			this.hostField.setEnabled(b);
			this.portField.setEnabled(b);
		}
	}
	
	public String getHostValue(){
		return this.hostField.getText();
	}
	
	public void setHostValue(String s){
		this.hostField.setText(s);
	}
	
	public Integer getPortValue(){
		return Integer.parseInt(this.portField.getText());
	}
	
	public void setPortValue(int i){
		this.portField.setText(String.valueOf(i));
	}

	public ProxyType getProxyType() {
		return typeArray[proxyTypeBox.getSelectedIndex()];
	}
	
	public void setProxyType(ProxyType type){
		
		for(int i = 0; i < typeArray.length; i++){
			if(typeArray[i] == type){
				proxyTypeBox.setSelectedIndex(i);
			}
		}
		
		if(type == ProxyType.MANUAL){
			this.promptForBrowserChange();
			this.hostField.setEnabled(true);
			this.portField.setEnabled(true);
		} else {
			this.hostField.setEnabled(false);
			this.portField.setEnabled(false);
		}
		
	}
	
	public boolean promptForBrowserChange(){
		
		BasicPanel gui = (BasicPanel)this.getParent().getComponent(0);
		BrowserType bType = gui.getBrowserType();
		
		if(bType != BrowserType.firefox){

			Object[] options = {"Yes","No"};
			int result = JOptionPane.showOptionDialog(null, "Manual proxy configuration only works with Firefox. Switch \n browser choice to Firefox?", "Switch Browser"
					, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(result == 0){ // Option 0 = Yes
				gui.setBrowserType(BrowserType.firefox);
				return true;
			} else { // Option 1 = No
				return false;
			}
		} else {
			return true;
		}
		
	}
	
}
