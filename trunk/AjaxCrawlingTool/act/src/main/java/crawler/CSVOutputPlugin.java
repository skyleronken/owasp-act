package crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.plugin.OnNewStatePlugin;

public class CSVOutputPlugin implements OnNewStatePlugin {

	String filePath;
	FileWriter fWriter;
	PrintWriter pWriter;
	ArrayList<String> urls;
	boolean replaceInput = false;
	
	public CSVOutputPlugin(boolean replaceInput, String path) throws IOException{
		this.replaceInput = replaceInput;
		this.filePath = path;
		this.urls = new ArrayList<String>();
		fWriter = new FileWriter(filePath, false);
		pWriter = new PrintWriter(fWriter);
		pWriter.print("");
		pWriter.close();
	}
	
	public void onNewState(CrawlSession session) {
		String url = session.getCurrentState().getUrl();
		if(!urls.contains(url)){
			urls.add(url);
			try {
				fWriter = new FileWriter(filePath, true);
				pWriter = new PrintWriter(fWriter);
				if(replaceInput){
					url = CrawlerRunnable.prepareUrl(url);
				}
				pWriter.printf("%s" + "%n",url);
				pWriter.close();
			} catch (IOException e) {
				
			}
		}
	}

}
