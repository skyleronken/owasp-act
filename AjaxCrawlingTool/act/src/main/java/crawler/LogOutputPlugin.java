package crawler;

import java.util.ArrayList;

import userInterface.OutputModePanel;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.plugin.OnNewStatePlugin;

public class LogOutputPlugin implements OnNewStatePlugin {

	OutputModePanel panel;
	ArrayList<String> urls;
	boolean replaceInput = false;
	
	public LogOutputPlugin(boolean replaceInput, OutputModePanel panel){
		this.replaceInput = replaceInput;
		this.panel = panel;
		this.urls = new ArrayList<String>();
	}
	
	public void onNewState(CrawlSession session) {
		String url = session.getCurrentState().getUrl();
		if(!urls.contains(url)){
			urls.add(url);
			if(replaceInput){
				this.panel.addLineToStandardOutput(url + " --> " + CrawlerRunnable.prepareUrl(url));
			} else {
				this.panel.addLineToStandardOutput(url);
			}
		}
	}

}
