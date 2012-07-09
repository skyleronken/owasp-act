package crawler;

import java.io.IOException;
import java.util.Iterator;
import javax.naming.ConfigurationException;

import userInterface.CrawlerGUI;

import com.crawljax.core.CrawljaxController;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawlElement;
import com.crawljax.core.configuration.CrawlSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;

public class CrawlerRunnable implements Runnable{

	public static final String REPLACE_VALUE = "TESTHERE";
	
    private CrawlRequest request;
    public CrawljaxController controller;
    
	//Default Constructor
	public CrawlerRunnable(CrawlRequest request){
		this.request = request;
	}
	
	public void run() {
		if(request == null){
            return;
		} else {
            //Process request
            try {
               CrawljaxConfiguration config = configCrawler(request);
               controller = new CrawljaxController(config);
               controller.run();
            } catch (ConfigurationException e) {
                    e.printStackTrace();
            } catch (CrawljaxException e) {
                    e.printStackTrace();
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
		
	}

	//Thread to configure all crawling parameters and settings
	private CrawljaxConfiguration configCrawler(CrawlRequest request) throws Exception{
		
		//Create a specification and configuration
        CrawlSpecification crawlerSpec = new CrawlSpecification(request.getUrl());
        CrawljaxConfiguration crawlerConfig = new CrawljaxConfiguration();
        
        //Configure the clicks and frames
		configureClicks(crawlerSpec);
		
		//Configure Invariants
		configureInvariants(crawlerSpec);
		
		//Configure the input specification
		configureInputSpecification(crawlerSpec);
		
		//Configure runtime/depth/states
		crawlerSpec.setDepth(request.getMaxDepth()); //Default 0
		crawlerSpec.setMaximumStates(request.getMaxStates()); //Default 0
		configureMaxRuntime(crawlerSpec);
		crawlerSpec.setClickOnce(request.isCrawlOnce());
		
		//Configure Wait Times
		configureEventWait(crawlerSpec);
		configureReloadWait(crawlerSpec);
		
		//Set Browser
        crawlerConfig.setBrowser(request.getBrowserType()); //Use HTMLUnit for Speed
        
        //Setup Proxy
        if(request.isUseProxy()){
        	configureProxy(crawlerConfig);
        	//crawlerConfig.addPlugin(new UseProxyPlugin());
        }
        
        //Combine the specifications
        crawlerConfig.setCrawlSpecification(crawlerSpec);
        
        //Add output plugins
        configureOutputPlugins(crawlerConfig);
        
        return crawlerConfig;
	}

	private void configureOutputPlugins(CrawljaxConfiguration crawlerConfig) {
		
		boolean replaceInput = request.getUseParsableInput();

		if(request.getUseGUI()){
			crawlerConfig.addPlugin(new LogOutputPlugin(replaceInput, CrawlerGUI.GUI.getOutputPanel()));
		}
		
		switch(request.getOutputMode()){
		
			case CrawlRequest.STDOUT: 	crawlerConfig.addPlugin(new StandardOutputPlugin(replaceInput));
										break;
										
			case CrawlRequest.DB:		break;
			
			case CrawlRequest.CSV:		try {
											crawlerConfig.addPlugin(new CSVOutputPlugin(replaceInput,request.getOutputFilePath()));
										} catch (IOException e) {
											String err = "[?] Could not write to file!";
											if(request.getUseGUI()){
												CrawlerGUI.GUI.getOutputPanel().addLineToStandardOutput(err);
											} else {
												System.out.println(err);
											}
										}
										break;
			
			default:					break;
		}
	}

	private void configureProxy(CrawljaxConfiguration crawlerConfig) {
		ProxyConfiguration proxy = new ProxyConfiguration();
		proxy.setType(request.getProxyType());
		if(request.getProxyType() == ProxyType.MANUAL){
			proxy.setHostname(request.getProxyUrl());
			proxy.setPort(request.getProxyPort());
		}
		crawlerConfig.setProxyConfiguration(proxy);
	}

	private void configureInvariants(CrawlSpecification crawlerSpec) {
		crawlerSpec.setTestInvariantsWhileCrawling(request.isTestInvariants());
		
		//TODO invariants stuff here
	}

	private void configureReloadWait(CrawlSpecification crawlerSpec) {
		int wait = request.getReloadWaitTime();//In milliseconds
		if(wait > 0){
			crawlerSpec.setWaitTimeAfterReloadUrl(wait);
		}
	}

	private void configureEventWait(CrawlSpecification crawlerSpec) {
		int wait = request.getEventWaitTime();//In milliseconds
		if(wait > 0){
			crawlerSpec.setWaitTimeAfterEvent(wait);
		}
	}

	private void configureClicks(CrawlSpecification spec){
		//Set the specification of which HTML elements to click
		if(request.isClickDefaults()){ spec.clickDefaultElements(); } // Buttons and anchors
		if(!request.isCrawlFrames()){ spec.disableCrawlFrames(); }
		
        Iterator<PageElement> itr = request.getClickElements().iterator();
        while(itr.hasNext()){
        	PageElement element = itr.next();
        	
        	//This handles whether or not the element is configured to be clicked, or not to be clicked
        	CrawlElement rule;
        	if(element.doClick()){
        		rule = spec.click(element.getTag());
        	} else {
        		rule = spec.dontClick(element.getTag());
        	}
			
			//XPath
			if(!element.getXPath().isEmpty()){
				rule.underXPath(element.getXPath());
			}
			
			//Attribute and value
			if(!element.getAttributeName().isEmpty()){
				rule.withAttribute(element.getAttributeName(), element.getAttributeValue());
			}
			
			//Text
			if(!element.getText().isEmpty()){
				rule.withText(element.getText());
			}
        }
	}

	private void configureInputSpecification(CrawlSpecification spec){
		spec.setRandomInputInForms(request.isRandomInput());
		
		InputSpecification inputSpec = new InputSpecification();
		
		Iterator<PageInput> itr = request.getSpecifiedInputs().iterator();
		while(itr.hasNext()){
			PageInput input = itr.next();
			String value = input.getValue();
			if(value.contentEquals("true")){
				inputSpec.field(input.getFieldName()).setValue(true);
			} else if (value.contentEquals("false")){
				inputSpec.field(input.getFieldName()).setValue(false);
			} else {
				inputSpec.field(input.getFieldName()).setValue(value);
			}
			
		}
		
		spec.setInputSpecification(inputSpec);
	}
	
	private void configureMaxRuntime(CrawlSpecification crawlerSpec) {
		int runtime = request.getMaxDuration(); // In minutes
		if(runtime > 0){
			crawlerSpec.setMaximumRuntime(runtime*60);
		}
		
	}
	
	public static String prepareUrl(String url){
		if(url.contains("?")){
	        ModularUrl modUrl = new ModularUrl(url);
	        if (modUrl.getParams() != null){
	                for(int i=0; i < modUrl.getParams().size(); i++){
	                        modUrl.getParams().get(i).setValue(CrawlRequest.PARSABLE_STRING);
	                }
	        }
	        return modUrl.getCompleteUrl();
		} else {
			return url;
		}
}
	
}
