package userInterface;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;

import crawler.CrawlRequest;
import crawler.PageElement;
import crawler.PageInput;

public class Main {
	
	static boolean useGUI = false;
	
	public static void main(String[] args) {
	
		CrawlRequest aRequest = new CrawlRequest();
		
		if(args.length == 0){
			useGUI=true;
			//printHelp();
			//System.exit(1);
		}
		
		for(int i=0; i < args.length; i++){
			
			//Grab current argument being parsed
			String arg = args[i];
			
			//Help Menu
			if(arg.contains("-h")){
				printHelp();
			} else
				
			//If GUI is wanted
			if(arg.contains("-g")){ 
				Main.useGUI = true;
				break;
			} else {
				// Parse command line options
				
				//default output to stdout
				aRequest.setOutputMode(CrawlRequest.STDOUT);
				if(arg.compareTo("--no-output") == 0){
					aRequest.setOutputMode(CrawlRequest.NONE);
				} else
				
				//Set the URL
				if(arg.compareTo("-u") == 0){
					String newUrl = args[i+1];
					if(validate(newUrl,arg)){
						aRequest.setUrl(newUrl);
					}
				} else 
					
				// Output modes
				if(arg.compareTo("--csv") == 0){
					String path = args[i+1];
					if(validate(path,arg)){
						aRequest.setOutputMode(CrawlRequest.CSV);
						aRequest.setOutputFilePath(args[i+1]);
					}
				} else
					
				if(arg.compareTo("--parsable-input") == 0){
					aRequest.setUseParsableInput(true);
				} else
				
				//Set browser type
				if(arg.compareTo("-b") == 0){
					String bType = args[i+1];
					if(validate(bType, arg)){
						if(bType.compareToIgnoreCase("chrome") == 0){
							aRequest.setBrowserType(BrowserType.chrome);
						} else if(bType.compareToIgnoreCase("firefox") == 0){
							aRequest.setBrowserType(BrowserType.firefox);
						} else if(bType.compareToIgnoreCase("ie") == 0){
							aRequest.setBrowserType(BrowserType.ie);
						} else {
							System.out.println("* Browser type not recognized; using HTMLUnit.");
						}
					}
					
				} else
				
				//Set the Proxy Server
				if(arg.compareTo("-p") == 0){
					String newProxy = args[i+1];
					if(validate(newProxy,arg)){
						String[] tempProxy = newProxy.split(":");
						aRequest.setUseProxy(true);
						aRequest.setProxyUrl(tempProxy[0]);
						aRequest.setProxyPort(Integer.parseInt(tempProxy[1]));
						
						// If using a proxy, must use firefox						
						if(aRequest.getBrowserType() == BrowserType.firefox){
							aRequest.setProxyType(ProxyType.MANUAL);
						} else {
							System.out.println("[*] Using system proxy settings with the selected browser. Change to firefox to manually configure. ");
							aRequest.setProxyType(ProxyType.SYSTEM_DEFAULT);
						}
					}
				} else 
					
				//Set Duration
				// : Depth
				
				if(arg.compareTo("--depth") == 0){
					String depthNum = args[i+1];
					if(validateNum(depthNum, arg)){
						aRequest.setMaxDepth(Integer.parseInt(depthNum));
					}
				} else
				
				// : States
				
				if(arg.compareTo("--states") == 0){
					String statesNum = args[i+1];
					if(validateNum(statesNum, arg)){
						aRequest.setMaxStates(Integer.parseInt(statesNum));
					}
				} else
				
				// : Time (Minutes)
				
				if(arg.compareTo("--time") == 0){
					String timeNum = args[i+1];
					if(validateNum(timeNum, arg)){
						aRequest.setMaxDuration(Integer.parseInt(timeNum));
					}
				} else
				
				//Set Delays
				// : Event
				
				if(arg.compareTo("--event-delay") == 0){
					String delayNum = args[i+1];
					if(validateNum(delayNum, arg)){
						aRequest.setEventWaitTime(Integer.parseInt(delayNum));
					}
				} else
				
				// : Reload
				
				if(arg.compareTo("--reload-delay") == 0){
					String delayNum = args[i+1];
					if(validateNum(delayNum, arg)){
						aRequest.setReloadWaitTime(Integer.parseInt(delayNum));
					}
				} else
					
				// Scope
				// : Crawl Frames
				
				if (arg.compareTo("--no-frames") == 0){
					aRequest.setCrawlFrames(false);
				} else 
					
				// : Crawl Once
					
				if (arg.compareTo("--crawl-once") == 0){
					aRequest.setCrawlOnce(true);	
				} else
				
				// No Random Input
					
				if (arg.compareTo("--no-random-input") == 0){
					aRequest.setRandomInput(false);	
				} else
				
				// No Defaults
					
				if (arg.compareTo("--no-defaults") == 0){
					aRequest.setClickDefaults(false);	
				} else
					
				// Advanced
					
				if (arg.compareTo("--inputs") == 0){
					String inputs = args[i+1];
					if(validate(inputs, arg)){
						inputs = inputs.replace("{", "").replace("}", ""); // strip off brackets
						String[] inputsArray = inputs.split(","); // split each input pair
						for(int j = 0; j < inputsArray.length ; j++){
							String[] singleInput = inputsArray[j].split(":"); // split name and value
							PageInput newInput = new PageInput(singleInput[0],singleInput[1]);
							aRequest.addSpecifiedInput(newInput);
						}
					}
				} else
					
				if (arg.compareTo("--define-clicks") == 0){
					String clicks = args[i+1];
					if(validate(clicks, arg)){
						clicks = clicks.replace("{", "").replace("}", "");
						String[] clicksArray = clicks.split(",");
						for(int j = 0; j < clicksArray.length ; j++){
							String[] singleClick = clicksArray[j].split(":");
							PageElement newElement = new PageElement();
							if(!singleClick[0].equals("null"))
								newElement.setTag(singleClick[0]);
							if(!singleClick[1].equals("null"))
								newElement.setAttributeName(singleClick[1]);
							if(!singleClick[2].equals("null"))
								newElement.setAttributeValue(singleClick[2]);
							if(!singleClick[3].equals("null"))
								newElement.setText(singleClick[3]);
							if(!singleClick[4].equals("null"))
								newElement.setXPath(singleClick[4]);
							if(singleClick[5].contains("f"))
								newElement.setDoClick(false);
							
							aRequest.addClickElement(newElement);
						}
					}
				}
				
				
				
			} // End of CLI else statement
			
		} // End of parsing loop
		
		if(useGUI){
			//Instantiate GUI
			buildGUI();
		} else {
			//Run in command line mode
			if(aRequest.getUrl().isEmpty()){
				System.out.println("[*] A url (-u [url]) is required!");
			} else {
				aRequest.crawl();
			}
		}
			
	}
	
	private static boolean validate(String arg, String option){
		if(arg.startsWith("-")){
			System.out.println("[*] '" + arg + "' is not a valid argument for the '" + option + "' option.");
			System.exit(0);
			return false;
		} else {
			return true;
		}
	}
	
	private static boolean validateNum(String arg, String option){
		if(arg.startsWith("-") || Integer.valueOf(arg) == null ){
			System.out.println("[*] '" + arg + "' is not a valid argument for the '" + option + "' option. Should be an integer.");
			System.exit(0);
			return false;
		} else {
			return true;
		}
	}
	
	private static void printHelp(){
		System.out.println("\n" +
				"OWASP AJAX Crawling Tool\n" +
				"\n" +
				"-u [url] : target url \n (REQUIRED)" +
				"-h : This help menu \n" +
				"-g : Run with GUI \n" +
				"-p : Use a proxy server (e.g. 127.0.0.1:8080). System proxy settings should be set to the same when using any browser besides firefox. \n" +
				"-b [chrome/ie/firefox] : Designate a browser other than HTMLUnit.\n" +
				"\n" +
				":::: OUTPUT ::::\n" +
				"\n" +
				"--no-output : turn off output. stdout is on by default for command line.\n" +
				"--csv [filePath] : output results as comma seperated file.\n" +
				"--parsable-input : crawled URLs will be output with a recognizable string as all parameter values: " + CrawlRequest.PARSABLE_STRING +
				"\n" +
				"+++ DURATION +++\n" +
				"\n" +
				"--depth [#]: Maximum depth of DOM tree to be crawled.\n" +
				"--states [#]: Amount of times DOM can change before crawling stops.\n" +
				"--time [#]: Duration of crawl in minutes.\n" +
				"\n" +
				"=== DELAY ===\n" +
				"\n" +
				"--event-delay [#]: Time to wait after DOM changes.\n" +
				"--reload-delay [#]: Time to wait after a new page is loaded.\n" +
				"\n" +
				"*** SCOPE ***\n" +
				"\n" +
				"--no-frames : Do not crawl frames. Default is to do so.\n" +
				"--crawl-once : Exercise each element only once.\n" +
				"\n" +
				"### ADVANCED ###\n" +
				"\n" +
				"--no-defaults : Do not click default elements (anchors and buttons). Default is to do so.\n" +
				"--no-random-input : Do not use random input. Default is to do so.\n"+
				"--inputs {[name/id]:[value],...} : Specific input to be used in a specific field. NO SPACES.\n" +
				"--define-clicks {[tag]:[name]:[value]:[text]:[xpath]:[t/f], ...}: Specify elements to click or not to click. NO SPACES. insert 'null' for empty values\n." +
				"\n" +
				"\n");
	}
	
	@SuppressWarnings("unused")
	private static void buildGUI(){
		if ( System.getProperty( "os.name" ).toLowerCase( ).startsWith( "windows" ) ){
			try {
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				// If Nimbus is not available, you can set the GUI to another look and feel.
			}
		}
		
		//Instantiate frame
		CrawlerGUI gui = new CrawlerGUI();
	}
	
}
