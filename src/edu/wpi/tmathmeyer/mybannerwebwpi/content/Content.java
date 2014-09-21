package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.tmathmeyer.mybannerwebwpi.WebReader;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.*;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Content implements Runnable {

	public static List<Page> items = new ArrayList<Page>();
	public static Map<String, Page> item_map = new HashMap<String, Page>();

	static {
		addPage(Page.newInstance(MailBox.class, "Mail Box Information", "hwwkboxs.P_ViewBoxs"));
		//addPage((MealPlan)Page.newInstance("Meal Plan Balances", "hwwkcbrd.P_Display"));
		addPage(Page.newInstance(AdvisorInfo.class, "Academic Advisor Information", "hwwksadv.P_Summary"));
		//addPage((CalendarSchedule)Page.newInstance("Calendar Schedule","bwskfshd.P_CrseSchd?start_date_in=[DATE]"));
		//addPage((DetailSchedule)Page.newInstance("Detail Schedule", "bwskfshd.P_CrseSchdDetl"));
	}

	private static void addPage(Page page) {
		items.add(page);
		item_map.put(page.getUrl(), page);
	}

	public static void loadResources() {
		for (Page i : items) {
			loadResource(i);
		}
	}

	public static void loadResource(Page page) {
		String pageHtml = WebReader.getInstance("", "").sendGetRequest(
				"https://bannerweb.wpi.edu/pls/prod/" + page.getUrl());
		page.loadContent(pageHtml);
	}

	@Override
	public void run() {
		Content.loadResources();
	}
}
