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

	/**
	 * An array of items.
	 */
	public static List<Page> items = new ArrayList<Page>();

	/**
	 * A map of items, by ID.
	 */
	public static Map<String, Page> item_map = new HashMap<String, Page>();

	/**
	 * Current list of agreed-upon items -mailbox (DONE) -meal plan (DONE)
	 * -calendar schedule ( ) -detail schedule ( ) -advisor information ( )
	 */

	static {
		addPage(new MailBox("Mail Box Information", "hwwkboxs.P_ViewBoxs"));
		addPage(new MealPlan("Meal Plan Balances", "hwwkcbrd.P_Display"));
		addPage(new CalendarSchedule( "Calendar Schedule", "bwskfshd.P_CrseSchd?start_date_in=[DATE]"));
		addPage(new DetailSchedule( "Detail Schedule", "bwskfshd.P_CrseSchdDetl"));
		addPage(new AdvisorInfo("Acedemic Advisor Information", "hwwksadv.P_Summary"));
	}

	private static void addPage(Page page) {
		items.add(page);
		item_map.put(page.url, page);
	}

	public static void loadResources() {
		for (Page i : items) {
			loadResource(i);
		}

	}

	public static void loadResource(Page p) {
		if (p != null) {
			p.setHTML(WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/" + p.url));
			p.content = p.getContent();
		} else {
			p.content = "Feature Coming Soon!\nEmail tjmeyer@wpi.edu if you'd like\nto help implement it";
		}
	}

	@Override
	public void run() {
		Content.loadResources();
	}
}
