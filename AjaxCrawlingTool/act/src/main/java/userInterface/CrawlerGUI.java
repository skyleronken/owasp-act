package userInterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;
import crawler.CrawlRequest;
import crawler.PageElement;
import crawler.PageInput;

@SuppressWarnings("serial")
public class CrawlerGUI extends JFrame implements ClipboardOwner {

	public static final int MAIN_HEIGHT = 515;
    public static final int MAIN_WIDTH = 800;
    public static CrawlRequest REQUEST;
    public static CrawlerGUI GUI;
    
    private JMenuBar menuBar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenu crawl = new JMenu("Crawl");
    private JMenu help = new JMenu("Help");
    
    private JMenuItem menuItemExit   = new JMenuItem("Exit");
    private JMenuItem menuItemSave    = new JMenuItem("Save");
    private JMenuItem menuItemOpen	 = new JMenuItem("Open");
    private JMenuItem menuItemExport  = new JMenuItem("Export As Command");
    private JMenuItem menuItemNew  = new JMenuItem("New");
    private JMenuItem menuItemStart    = new JMenuItem("Start Crawl");
    private JMenuItem menuItemAbout = new JMenuItem("About");
    private JMenuItem menuItemHelp = new JMenuItem("Help");
    private JMenuItem menuItemStop = new JMenuItem("Stop Crawl");
    
	private Thread currentCrawl;
    protected BasicPanel basicPanel;
    protected ClickConfigPanel clickPanel;
    protected InputSpecPanel inputPanel;
    protected ProxyPanel proxyPanel;
    protected OutputModePanel outputModePanel;
    protected JScrollPane outputScrollPane;
    
    private JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
	private CrawlerGUI self;
	
    final JFileChooser fc = new JFileChooser();
    
    public CrawlerGUI(){
    	self = this;
    	CrawlerGUI.GUI = this;
    	
    	fc.addChoosableFileFilter(new ACTFilter());
    	
    	CrawlerGUI.REQUEST = new CrawlRequest();
    	
    	this.setTitle("AJAX Crawling Tool");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
        buildMenu();
        
        this.newCrawler();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void buildMenu(){
    	
    	menuBar.add(file);
    	menuBar.add(crawl);
    	menuBar.add(help);
    	
    	file.setMnemonic('F');
        crawl.setMnemonic('C');
        help.setMnemonic('H');
        
        file.add(menuItemNew);
        file.add(menuItemSave);
        file.add(menuItemExport);
        file.add(menuItemOpen);
        file.addSeparator();
        file.add(menuItemExit);
        
        menuItemNew.setMnemonic('N');
        menuItemSave.setMnemonic('S');
        menuItemExit.setMnemonic('E');
        
        crawl.add(menuItemStart);
        crawl.add(menuItemStop);
        //crawl.addSeparator();
        //crawl.add(menuItemExport);
        
        menuItemStart.setMnemonic('S');
        menuItemExport.setMnemonic('E');
        
        help.add(menuItemAbout);
        //help.add(menuItemHelp);
        
        menuItemAbout.setMnemonic('A');
        
        menuItemStop.setEnabled(false);
        this.setJMenuBar(menuBar);
    	
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuItemExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                        System.exit(0);
                }
        });
        
        menuItemAbout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "AJAX Crawling Tool \n\n Created by Skyler Onken" +
						"\n\n Contributions by: LDS ICS, OnPoint Development Group LLC \n\n Copyright 2011" +
						"\n\n Credits to the Crawljax Project and Selenium" +
						"\n\n http://securityreliks.securegossip.com", "About ACT",JOptionPane.PLAIN_MESSAGE);
			}        	
        });
        
        menuItemHelp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Definitions:\n" +
						""
						, "ACT Help",JOptionPane.PLAIN_MESSAGE);
			}        	
        });
        
        menuItemStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				packageCrawlRequest();
				currentCrawl = CrawlerGUI.REQUEST.crawl();
				tabs.setSelectedComponent(outputScrollPane);
				menuItemStart.setEnabled(false);
				menuItemStop.setEnabled(true);
				

				Thread watcher = new Thread(new Runnable(){

					public void run() {
						try {
							self.currentCrawl.join();
							JOptionPane.showMessageDialog(self, "Crawling has finished","Crawling Complete",JOptionPane.PLAIN_MESSAGE);
							menuItemStart.setEnabled(true);
							menuItemStop.setEnabled(false);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				watcher.start();

			}        	
        });
        
        menuItemStop.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				menuItemStart.setEnabled(true);
				menuItemStop.setEnabled(false);
				REQUEST.aCrawler.controller.terminate(true);
				currentCrawl.stop(); //Depricated. Check google code site for proposd changes
				
			}
        });
        
        menuItemNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				self.newCrawler();
			}

        });
        
        menuItemSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				packageCrawlRequest();
			    int returnVal = fc.showDialog(self,"Save");
			    
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();

		            FileOutputStream fos = null;
		            ObjectOutputStream oos;
		            
		            try {
						fos = new FileOutputStream(file);
					} catch (FileNotFoundException e2) {
						String newFile = fc.getSelectedFile().getName()+".act";
						file = new File(newFile);
						try {
							fos = new FileOutputStream(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					} 
					
					try {
						oos = new ObjectOutputStream(fos);
						oos.writeObject(REQUEST);
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
		            
			    }
			    
			}
        	
        });
        
        menuItemExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				packageCrawlRequest();
				CrawlRequest request = CrawlerGUI.REQUEST;
				StringBuilder command = new StringBuilder("java -jar act.jar");
				command.append(" -u " + request.getUrl());
				
				//Export browser
				BrowserType bType = request.getBrowserType();
				if(bType == BrowserType.chrome) { command.append(" -b chrome");} else
				if(bType == BrowserType.ie) { command.append(" -b ie");} else
				if(bType == BrowserType.firefox) { command.append(" -b firefox");}
				
				//Output
				if(request.getOutputMode() == CrawlRequest.CSV){ command.append(" --csv " + request.getOutputFilePath()); }
				
				if(request.getUseParsableInput()){ command.append(" --parsable-input"); }
				
				//Proxy
				if(request.isUseProxy()){ command.append(" -p "+request.getProxyUrl()+":"+request.getProxyPort()); }
				
				//Duration
				if(request.getMaxDepth() > 0){ command.append(" --depth " + request.getMaxDepth()); }
				if(request.getMaxDuration() > 0){ command.append(" --time " + request.getMaxDuration()); }
				if(request.getMaxStates() > 0){ command.append(" --states " + request.getMaxStates()); }
				
				//Delay
				if(request.getReloadWaitTime() > 0){ command.append(" --reload-delay " + request.getReloadWaitTime()); }
				if(request.getEventWaitTime() > 0){ command.append(" --event-delay " + request.getEventWaitTime()); }
				
				//Scope
				if(!request.isCrawlFrames()){ command.append(" --no-frames"); }
				if(request.isCrawlOnce()){ command.append(" --crawl-once"); }
				
				//Advanced
				if(!request.isClickDefaults()){ command.append(" --no-defaults"); }
				if(!request.isRandomInput()){ command.append(" --no-random-input"); }
				
				//Input Elements
				if(request.getSpecifiedInputs().size() > 0){
					command.append(" --inputs {");
			    	for(int i = 0; i < request.getSpecifiedInputs().size();i++){
			    		PageInput input = request.getSpecifiedInputs().get(i);
			    		command.append(input.getFieldName() + ":" + input.getValue() + ",");
			    	}
			    	command.deleteCharAt(command.lastIndexOf(",")); // delete last comma
			    	command.append("}");
				}
				
				//Click Elements
				if(request.getClickElements().size() > CrawlRequest.DEFAULT_ELEMENTS.length){
					command.append(" --define-clicks {");
			    	for(int i = CrawlRequest.DEFAULT_ELEMENTS.length; i < request.getClickElements().size();i++){
			    		PageElement input = request.getClickElements().get(i);
			    		
			    		if(input.getTag().isEmpty()){command.append("null");}else{command.append(input.getTag());}
			    		if(input.getAttributeName().isEmpty()){command.append(":null");}else{command.append(":"+input.getAttributeName());}
			    		if(input.getAttributeValue().isEmpty()){command.append(":null");}else{command.append(":"+input.getAttributeValue());}
			    		if(input.getText().isEmpty()){command.append(":null");}else{command.append(":"+input.getText());}
			    		if(input.getXPath().isEmpty()){command.append(":null");}else{command.append(":"+input.getXPath());}
			    		if(input.doClick()){command.append(":true");} else {command.append(":false");}
			    		
			    		command.append(",");
			    	}
			    	command.deleteCharAt(command.lastIndexOf(",")); // delete last comma
			    	command.append("}");
				}
				
				self.setClipboardContents(command.toString());
				String alertString = "The following command has been copied to your clipboard:";
				
				JTextArea textArea = new JTextArea(alertString + "\n\n");
				textArea.setColumns(40);
				textArea.setLineWrap( true );
				textArea.setWrapStyleWord( false );
				textArea.append(command.toString());
				textArea.setSize(textArea.getPreferredSize().width, 1);
				textArea.setBackground(UIManager.getColor("Panel.background"));
				JOptionPane.showMessageDialog(null, textArea,"Command Export", JOptionPane.PLAIN_MESSAGE);
			}        	
        });
        
        menuItemOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showDialog(self,"Open");
			    
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();

		            try {
						FileInputStream fos = new FileInputStream(file);
			            ObjectInputStream oos = new ObjectInputStream(fos);
						CrawlRequest request = (CrawlRequest)oos.readObject();
						oos.close();
						loadCrawlRequest(request);
					} catch (FileNotFoundException e2) {
						JOptionPane.showMessageDialog(self, "File Not Found");
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e3) {
						e3.printStackTrace();
					}
		            
			    }
			}
        	
        });
      
        this.setResizable(true);
        Dimension size = new Dimension(675,300);
        this.setPreferredSize(size);
        
    }
    
    protected void newCrawler() {
    	this.remove(tabs);
    	
    	tabs = new JTabbedPane();
    	
    	REQUEST=new CrawlRequest();
    	REQUEST.setUseGUI(true);
    	
    	basicPanel = new BasicPanel();
        clickPanel = new ClickConfigPanel();
        inputPanel = new InputSpecPanel();
        proxyPanel = new ProxyPanel();
        outputModePanel = new OutputModePanel();
        
        tabs.addTab("Basic Configurations",basicPanel);
        tabs.addTab("Whitelist & Blacklist",clickPanel);
        tabs.addTab("Input Specifications",inputPanel);
        tabs.addTab("Proxy Settings",proxyPanel);
        
        outputScrollPane = new JScrollPane(outputModePanel);
        Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
        outputScrollPane.setViewportBorder( border );
        outputScrollPane.setBorder( border );
        tabs.addTab("Output", outputScrollPane);
        
        this.add(tabs);
        this.pack();
        this.repaint();
	}

	protected void loadCrawlRequest(CrawlRequest request) {
		self.newCrawler();
    	REQUEST = request;
    	
    	basicPanel.setURL(REQUEST.getUrl());
    	basicPanel.setBrowserType(REQUEST.getBrowserType());
    	basicPanel.setCrawlFramesEnabled(REQUEST.isCrawlFrames());
    	basicPanel.setCrawlOnceEnabled(REQUEST.isCrawlOnce());
    	clickPanel.setDefaultClicksEnabled(REQUEST.isClickDefaults());
    	inputPanel.setRandomInputEnabled(REQUEST.isRandomInput());
    	outputModePanel.setOutputMode(REQUEST.getOutputMode());
    	outputModePanel.setOutputFilePath(REQUEST.getOutputFilePath());
    	outputModePanel.setParsableInput(REQUEST.getUseParsableInput());
    	
    	if(REQUEST.getMaxDepth() > 0) {
    		basicPanel.setDepthEnabled(true);
    		basicPanel.setDepth(REQUEST.getMaxDepth());
    	}
    	if(REQUEST.getMaxStates() > 0) {
    		basicPanel.setStatesEnabled(true);
    		basicPanel.setStates(REQUEST.getMaxStates());
    	}
    	if(REQUEST.getMaxDuration() > 0) {
    		basicPanel.setTimeEnabled(true);
    		basicPanel.setTime(REQUEST.getMaxDuration());
    	}
    	if(REQUEST.getReloadWaitTime() > 0) {
    		basicPanel.setReloadDelayEnabled(true);
    		basicPanel.setReloadDelays(REQUEST.getReloadWaitTime()); 
    	}
    	if(REQUEST.getEventWaitTime() > 0) {
    		basicPanel.setEventDelayEnabled(true);
    		basicPanel.setEventDelay(REQUEST.getEventWaitTime());
    	}
    	if(REQUEST.isUseProxy()){
    		proxyPanel.setProxyEnabled(true);
    		proxyPanel.setProxyType(REQUEST.getProxyType());
    		proxyPanel.setPortValue(REQUEST.getProxyPort());
    		proxyPanel.setHostValue(REQUEST.getProxyUrl());
    	}
    	
    	// Export the Input Specifications
    	for(int i = 0; i < REQUEST.getSpecifiedInputs().size();i++){
    		PageInput input = REQUEST.getSpecifiedInputs().get(i);
    		inputPanel.model.addRow(input.getFieldName(), input.getValue());
    	}
    	
    	// Export the Click Elements
    	for(int i = CrawlRequest.DEFAULT_ELEMENTS.length; i < REQUEST.getClickElements().size();i++){
    		PageElement input = REQUEST.getClickElements().get(i);
    		clickPanel.model.addRow(input.getTag(),input.getAttributeName(),input.getAttributeValue(),input.getText(),input.getXPath(),input.doClick());
    	}
    	
	}
	
	private void packageCrawlRequest(){
    	REQUEST.setUrl(basicPanel.getURL());
    	REQUEST.setBrowserType(basicPanel.getBrowserType());
    	if(basicPanel.isDepthEnabled()) { REQUEST.setMaxDepth(basicPanel.getDepth()); }
    	if(basicPanel.isStatesEnabled()) { REQUEST.setMaxStates(basicPanel.getStates()); }
    	if(basicPanel.isTimeEnabled()) { REQUEST.setMaxDuration(basicPanel.getTime()); }
    	REQUEST.setCrawlFrames(basicPanel.isCrawlFramesEnabled());
    	REQUEST.setCrawlOnce(basicPanel.isCrawlOnceEnabled());
    	if(basicPanel.isReloadDelayEnabled()) { REQUEST.setReloadWaitTime(basicPanel.getReloadDelay()); }
    	if(basicPanel.isEventDelayEnabled()) { REQUEST.setEventWaitTime(basicPanel.getEventDelay()); }
    	REQUEST.setClickDefaults(clickPanel.isDefaultClicksEnabled());
    	REQUEST.setRandomInput(inputPanel.isRandomInputEnabled());
    	REQUEST.setUseProxy(proxyPanel.isProxyEnabled());
    	REQUEST.setOutputMode(outputModePanel.getOutputMode());
    	REQUEST.setOutputFilePath(outputModePanel.getOutputFilePath());
    	REQUEST.setUseParsableInput(outputModePanel.getParsableInput());
    	if(proxyPanel.isProxyEnabled()){
    		ProxyType type = proxyPanel.getProxyType();
    		REQUEST.setProxyType(type);
    		if(type == ProxyType.MANUAL){
    			REQUEST.setProxyPort(proxyPanel.getPortValue());
    			REQUEST.setProxyUrl(proxyPanel.getHostValue());
    		}
    	}
    }
	//Clipboard stuff
	
	public void setClipboardContents( String aString ){
	    StringSelection stringSelection = new StringSelection( aString );
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents( stringSelection, this );
	 }

	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		
	}
	
	public OutputModePanel getOutputPanel(){
		return this.outputModePanel;
	}
    
    
}

class ACTFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        String filename = file.getName();
        return filename.endsWith(".act");
    }
    public String getDescription() {
        return "*.act";
    }
}
