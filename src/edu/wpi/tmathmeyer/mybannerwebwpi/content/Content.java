package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import edu.wpi.tmathmeyer.mybannerwebwpi.WebReader;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.Page;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Content implements Runnable {

	public static void loadResources() {
		for (Page i : Page.items) {
			loadResource(i);
		}
	}

	public static void loadResource(Page page) {
		String pageHtml = WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/" + page.url);
		page.fillContent(pageHtml);
	}

	@Override
	public void run() {
		Content.loadResources();
	}
}
