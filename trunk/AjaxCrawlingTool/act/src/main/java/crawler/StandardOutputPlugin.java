package crawler;

import java.util.ArrayList;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.plugin.OnNewStatePlugin;

public class StandardOutputPlugin implements OnNewStatePlugin {

	ArrayList<String> urls;
	boolean replaceInput = false;
	
	public StandardOutputPlugin(boolean replaceInput) { 
		this.replaceInput = replaceInput;
		this.urls = new ArrayList<String>();
	}
	
	public void onNewState(CrawlSession session) {
		String url = session.getCurrentState().getUrl();
		if(!urls.contains(url)){
			urls.add(url);
			if(replaceInput){
				url = CrawlerRunnable.prepareUrl(url);
			}
			System.out.println(url);
			
		}
	}

}
