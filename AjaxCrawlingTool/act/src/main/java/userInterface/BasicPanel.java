package userInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;

@SuppressWarnings("serial")
public class BasicPanel extends JPanel {

	private JSpinner depthSpinner;
	private JSpinner minuteSpinner;
	private JSpinner stateSpinner;
	private JSpinner eventSpinner;
	private JSpinner reloadSpinner;
	private JTextField urlField;
	private ButtonGroup browserGroup;
	private JRadioButton ieButton;
	private JRadioButton ffButton;
	private JRadioButton chromeButton;
	private JRadioButton httpUnitButton;
	private JCheckBox depthCheckBox;
	private JCheckBox timeCheckBox;
	private JCheckBox statesCheckBox;
	private JCheckBox crawlFramesCheckBox;
	private JCheckBox crawlOnceCheckBox;
	private JCheckBox eventCheckBox;
	private JCheckBox reloadCheckBox;
	
	private BrowserType selectedBrowserType = BrowserType.htmlunit;
	private BasicPanel self;
	
	public BasicPanel(){
		super(new GridBagLayout());
		
		self = this;
		
		GridBagConstraints c = new GridBagConstraints();
		
		//c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel urlLabel = new JLabel("Target URL: ");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		this.add(urlLabel, c);
		
		urlField = new JTextField();
		//urlField.setColumns(20);
		urlField.setText("http://");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 10;
		this.add(urlField, c);
		
		
		// Browser Panel
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Browser Type");
		
		JPanel browserPane = new JPanel();
		ieButton = new JRadioButton("Internet Explorer");
		ieButton.setActionCommand("Internet Explorer");
		
		ffButton = new JRadioButton("Firefox (<=v9)");
		ffButton.setActionCommand("Firefox (<=v9)");
		
		chromeButton = new JRadioButton("Chrome");
		chromeButton.setActionCommand("Chrome");
		
		httpUnitButton = new JRadioButton("HTML Unit");
		httpUnitButton.setActionCommand("HTML Unit");
		httpUnitButton.setSelected(true);
		
		browserGroup = new ButtonGroup();
		browserGroup.add(httpUnitButton);
		browserGroup.add(chromeButton);
		browserGroup.add(ffButton);
		browserGroup.add(ieButton);
		
		httpUnitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				self.setBrowserType(BrowserType.htmlunit);
			}});
		ffButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				self.setBrowserType(BrowserType.firefox);
			}});
		ieButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				self.setBrowserType(BrowserType.ie);
			}});
		chromeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				self.setBrowserType(BrowserType.chrome);
			}});;
			
		browserPane.add(httpUnitButton);
		browserPane.add(chromeButton);
		browserPane.add(ieButton);
		browserPane.add(ffButton);
		browserPane.setBorder(title);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 12;
		this.add(browserPane, c);
		
		//Crawl Restrictions
		title = BorderFactory.createTitledBorder("Duration");
		JPanel durationPanel = new JPanel(new GridLayout(3,2));
		//GridBagConstraints cd = new GridBagConstraints();
		
		depthCheckBox = new JCheckBox("Depth");
		timeCheckBox = new JCheckBox("Time (Minutes)");
		statesCheckBox = new JCheckBox("States");
		
		depthCheckBox.setSelected(false);
		timeCheckBox.setSelected(false);
		statesCheckBox.setSelected(false);

		SpinnerNumberModel depthModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 5);
		SpinnerNumberModel stateModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		
		depthSpinner = new JSpinner(depthModel);
		minuteSpinner = new JSpinner(minuteModel);
		stateSpinner = new JSpinner(stateModel);
		
		depthSpinner.setEnabled(false);
		minuteSpinner.setEnabled(false);
		stateSpinner.setEnabled(false);
		
		Dimension d = depthSpinner.getPreferredSize();  
        d.width = 25;
        
        depthSpinner.setPreferredSize(d);
        minuteSpinner.setPreferredSize(d);
        stateSpinner.setPreferredSize(d);
		
		depthCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					depthSpinner.setEnabled(true);
				} else {
					depthSpinner.setEnabled(false);
					depthSpinner.setValue(0);
				}
			}});
		timeCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					minuteSpinner.setEnabled(true);
				} else {
					minuteSpinner.setEnabled(false);
					minuteSpinner.setValue(0);
				}
			}});
		statesCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					stateSpinner.setEnabled(true);
				} else {
					stateSpinner.setEnabled(false);
					stateSpinner.setValue(0);
				}
			}});
		
		depthCheckBox.setToolTipText("Maximum depth of the DOM tree to be crawled");
		timeCheckBox.setToolTipText("Defined amount of time until crawling stops");
		statesCheckBox.setToolTipText("Amount of times DOM can change state before crawling stops");
		
		durationPanel.add(depthCheckBox);
		durationPanel.add(depthSpinner);
		durationPanel.add(statesCheckBox);
		durationPanel.add(stateSpinner);
		durationPanel.add(timeCheckBox);
		durationPanel.add(minuteSpinner);
		
		durationPanel.setBorder(title);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 3;
		this.add(durationPanel, c);
		
		//Crawl Scope
		title = BorderFactory.createTitledBorder("Scope");
		JPanel scopePanel = new JPanel(new GridLayout(3,1));
		
		crawlFramesCheckBox = new JCheckBox("Crawl Frames");
		crawlOnceCheckBox = new JCheckBox("Crawl Once");
		
		crawlFramesCheckBox.setSelected(true);
		crawlOnceCheckBox.setSelected(false);
		
		crawlFramesCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					CrawlerGUI.REQUEST.setCrawlFrames(true);
				} else {
					CrawlerGUI.REQUEST.setCrawlFrames(false);
				}
			}});
		crawlOnceCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					CrawlerGUI.REQUEST.setCrawlOnce(true);
				} else {
					CrawlerGUI.REQUEST.setCrawlOnce(false);
				}
			}});
		
		crawlFramesCheckBox.setToolTipText("Should crawl iFrames?");
		crawlOnceCheckBox.setToolTipText("Each element should only be exercised once");
		
		scopePanel.add(crawlFramesCheckBox);
		scopePanel.add(crawlOnceCheckBox);
		scopePanel.setBorder(title);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 3;
		this.add(scopePanel, c);
		
		//Delays
		title = BorderFactory.createTitledBorder("Delays");
		JPanel delaysPanel = new JPanel(new GridLayout(3,2));
		
		eventCheckBox = new JCheckBox("Event Delay");
		reloadCheckBox = new JCheckBox("Reload Delay");
		
		eventCheckBox.setSelected(false);
		reloadCheckBox.setSelected(false);
		
		SpinnerNumberModel millisecondModel1 = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 100);
		SpinnerNumberModel millisecondModel2 = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 100);
		
		eventSpinner = new JSpinner(millisecondModel1);
		reloadSpinner = new JSpinner(millisecondModel2);
		
		eventSpinner.setPreferredSize(d);
		reloadSpinner.setPreferredSize(d);
		
		eventSpinner.setEnabled(false);
		reloadSpinner.setEnabled(false);
				
		eventCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					eventSpinner.setEnabled(true);
				} else {
					eventSpinner.setEnabled(false);
					eventSpinner.setValue(0);
				}
			}});
		
		reloadCheckBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					reloadSpinner.setEnabled(true);
				} else {
					reloadSpinner.setEnabled(false);
					reloadSpinner.setValue(0);
				}
			}});
		
		eventCheckBox.setToolTipText("Time to wait after DOM changes");
		reloadCheckBox.setToolTipText("Time to wait after a new page is loaded");
		
		delaysPanel.add(eventCheckBox);
		delaysPanel.add(eventSpinner);
		delaysPanel.add(reloadCheckBox);
		delaysPanel.add(reloadSpinner);
		
		delaysPanel.setBorder(title);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 3;
		this.add(delaysPanel, c);
	}
	
	public String getURL(){
		return this.urlField.getText();
	}
	
	public void setURL(String url){
		this.urlField.setText(url);
	}
	
	public BrowserType getBrowserType(){
		return this.selectedBrowserType;
	}
	
	public void setBrowserType(BrowserType btype){
		this.selectedBrowserType = btype;
		browserGroup.clearSelection();
		switch (btype) {
			case ie: browserGroup.setSelected(ieButton.getModel(), true); break;
			case firefox: browserGroup.setSelected(ffButton.getModel(), true); break;
			case htmlunit: browserGroup.setSelected(httpUnitButton.getModel(), true); break;
			case chrome: browserGroup.setSelected(chromeButton.getModel(), true);break;
		}
			
		
	}
	
	public boolean isDepthEnabled(){
		return this.depthCheckBox.isSelected();
	}
	
	public void setDepthEnabled(boolean b){
		this.depthCheckBox.setSelected(b);
		this.depthSpinner.setEnabled(b);
	}
	
	public boolean isStatesEnabled(){
		return this.statesCheckBox.isSelected();
	}
	
	public void setStatesEnabled(boolean b){
		this.statesCheckBox.setSelected(b);
		this.stateSpinner.setEnabled(b);
	}
	
	public boolean isTimeEnabled(){
		return this.timeCheckBox.isSelected();
	}
	
	public void setTimeEnabled(boolean b){
		this.timeCheckBox.setSelected(b);
		this.minuteSpinner.setEnabled(b);
	}
	
	public Integer getTime(){
		return (Integer) this.minuteSpinner.getValue();
	}
	
	public void setTime(int v){
		this.minuteSpinner.setValue(0);
	}
	
	public Integer getStates(){
		return (Integer) this.stateSpinner.getValue();
	}
	
	public void setStates(int v){
		this.stateSpinner.setValue(0);
	}
	
	public Integer getDepth(){
		return (Integer) this.depthSpinner.getValue();
	}
	
	public void setDepth(int v){
		this.depthSpinner.setValue(0);
	}
	
	public boolean isCrawlOnceEnabled(){
		return this.crawlOnceCheckBox.isSelected();
	}
	
	public void setCrawlOnceEnabled(boolean b){
		this.crawlOnceCheckBox.setSelected(b);
	}
	
	public boolean isCrawlFramesEnabled(){
		return this.crawlFramesCheckBox.isSelected();
	}
	
	public void setCrawlFramesEnabled(boolean b){
		this.crawlFramesCheckBox.setSelected(b);
	}
	
	public boolean isReloadDelayEnabled(){
		return this.reloadCheckBox.isSelected();
	}
	
	public void setReloadDelayEnabled(boolean b){
		this.reloadCheckBox.setSelected(b);
		this.reloadSpinner.setEnabled(b);
	}
	
	public Integer getReloadDelay(){
		return (Integer) this.reloadSpinner.getValue();
	}
	
	public void setReloadDelays(int v){
		this.reloadSpinner.setValue(0);
	}
	
	public boolean isEventDelayEnabled(){
		return this.eventCheckBox.isSelected();
	}
	
	public void setEventDelayEnabled(boolean b){
		this.eventCheckBox.setSelected(b);
		this.eventSpinner.setEnabled(b);
	}
	
	public Integer getEventDelay(){
		return (Integer) this.eventSpinner.getValue();
	}
	
	public void setEventDelay(int v){
		this.eventSpinner.setValue(0);
	}
	
}
