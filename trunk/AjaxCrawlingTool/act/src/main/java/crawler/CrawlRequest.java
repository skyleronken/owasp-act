package crawler;

import java.io.Serializable;
import java.util.ArrayList;
import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;

@SuppressWarnings("serial")
public class CrawlRequest implements Serializable{
	
	// Class Variables
	public static final int NONE = 0;
	public static final int STDOUT = 1;
	public static final int DB = 2;
	public static final int CSV = 3;
	public static final String PARSABLE_STRING = "{REPLACE}";
	
	public static final String[] DEFAULT_ELEMENTS = {"div","span"};
	
	// Instance Variables
	
	private ArrayList<PageElement> clickElements;
	private ArrayList<PageInput> specifiedInputs;
	private boolean testInvariants = false;
	
	public CrawlerRunnable aCrawler;
	
	//Basics
	private BrowserType browserType = BrowserType.htmlunit;
	private String url = "";
	private int depth = 0;
	private int duration = 0;
	private int states = 0;
	private int eventWaitTime = 0;
	private int reloadWaitTime = 0;
	private boolean crawlOnce = false;
	private boolean crawlFrames = true;
	
	//Output
	private int outputMode = 0;
	private String filePath = "";
	private boolean replaceableInput = false;
	
	//Clicks
	private boolean clickDefaults = true;
	
	//Inputs 
	private boolean randomInput = true;
		
	//Proxy
	private boolean useProxy = false;
	private String proxyUrl = "";
	private int proxyPort = 0;
	private ProxyType proxyType = ProxyType.MANUAL;
	private boolean useGUI;

	// Constructors
	
	public CrawlRequest(){
		
		//Default elements
		clickElements = new ArrayList<PageElement>();
		for(String element : DEFAULT_ELEMENTS){
			clickElements.add(new PageElement(element));
		}
		
		specifiedInputs = new ArrayList<PageInput>();
	}
	
	public CrawlRequest(String url){
		this();
		this.url = url;
	}
	
	// URL Accessors
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	// Browser Type Accessors
	
	public void setBrowserType(BrowserType type){
		this.browserType = type;
	}
	
	public BrowserType getBrowserType(){
		return this.browserType;
	}
	
	// Click Elements Accessors
	
	public void setClickElements(ArrayList<PageElement> elements){
		this.clickElements = elements;
	}
	
	public ArrayList<PageElement> getClickElements(){
		return clickElements;
	}
	
	public void addClickElement(PageElement element){
		clickElements.add(element);
	}
	
	public void addClickElement(String element){
		PageElement newElement = new PageElement();
		clickElements.add(newElement);
	}
	
	public void removeClickElement(int index){
		clickElements.remove(index);
	}
	
	// Depth Accessors
	
	public int getMaxDepth() {
		return depth;
	}

	public void setMaxDepth(int depth) {
		this.depth = depth;
	}
	
	// Duration Accessors

	public int getMaxDuration() {
		return duration;
	}

	public void setMaxDuration(int duration) {
		this.duration = duration;
	}

	// States Accessors
	
	public int getMaxStates() {
		return states;
	}

	public void setMaxStates(int states) {
		this.states = states;
	}
	
	// Click Defaults Accessors
	
	public boolean isClickDefaults() {
		return clickDefaults;
	}

	public void setClickDefaults(boolean clickDefaults) {
		this.clickDefaults = clickDefaults;
	}

	// Crawl Frames Accessors
	
	public boolean isCrawlFrames() {
		return crawlFrames;
	}

	public void setCrawlFrames(boolean crawlFrames) {
		this.crawlFrames = crawlFrames;
	}
	
	// Random Input Accessors
	
	public boolean isRandomInput() {
		return randomInput;
	}

	public void setRandomInput(boolean randomInput) {
		this.randomInput = randomInput;
	}

	// Test Invariants Accessors
	
	public boolean isTestInvariants() {
		return testInvariants;
	}

	public void setTestInvariants(boolean testInvariants) {
		this.testInvariants = testInvariants;
	}

	// Event Wait Time Accessors
	
	public int getEventWaitTime() {
		return eventWaitTime;
	}

	public void setEventWaitTime(int eventWaitTime) {
		this.eventWaitTime = eventWaitTime;
	}

	// Reload Wait Time Accessors
	
	public int getReloadWaitTime() {
		return reloadWaitTime;
	}

	public void setReloadWaitTime(int reloadWaitTime) {
		this.reloadWaitTime = reloadWaitTime;
	}

	//Use Proxy Accessors
	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	//Proxy URL Accessors
	
	public String getProxyUrl() {
		return proxyUrl;
	}

	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}

	//Proxy Port Accessors
	
	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	//Proxy Type Accessors
	
	public ProxyType getProxyType(){
		return this.proxyType;
	}
	
	public void setProxyType(ProxyType pType){
		this.proxyType = pType;
	}
		
	//Specified Inputs Accessors
	
	public ArrayList<PageInput> getSpecifiedInputs() {
		return specifiedInputs;
	}

	public void setSpecifiedInputs(ArrayList<PageInput> specifiedInputs) {
		this.specifiedInputs = specifiedInputs;
	}
	
	public void addSpecifiedInput(PageInput newInput){
		this.specifiedInputs.add(newInput);
	}
	
	public void removeSpecifiedInput(int index){
		this.specifiedInputs.remove(index);
	}

	//Crawl Once Accessors
	
	public void setCrawlOnce(boolean b){
		this.crawlOnce = b;
	}
	
	public boolean isCrawlOnce(){
		return this.crawlOnce;
	}
	
	//Output mode accessors
	
	public void setOutputMode(int i){
		this.outputMode = i;
	}
	
	public int getOutputMode(){
		return this.outputMode;
	}
	
	public void setOutputFilePath(String path){
		this.filePath = path;
	}
	
	public String getOutputFilePath(){
		return this.filePath;
	}
	
	// Utility Methods

	
	public Thread crawl(){
		
		aCrawler = new CrawlerRunnable(this);
		
		Thread aThread = new Thread(aCrawler);
		try {
			aThread.start();
			return aThread;
		} catch (Exception e) {
			return null;
		}
	}

	public void setUseGUI(boolean b) {
		this.useGUI = b;
	}

	public boolean getUseGUI(){
		return this.useGUI;
	}
	
	public boolean getUseParsableInput(){
		return this.replaceableInput;
	}
	
	public void setUseParsableInput(boolean b){
		this.replaceableInput = b;
	}
	
}
