package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import edu.wpi.tmathmeyer.mybannerwebwpi.R;
import edu.wpi.tmathmeyer.mybannerwebwpi.WebReader;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.AdvisorInfo;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.CalendarSchedule;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.DetailSchedule;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.MailBox;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.MealPlan;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.Page;

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
		addPage(new MailBox("Mail Box Information", "hwwkboxs.P_ViewBoxs", R.layout.mailbox));
		addPage(new MealPlan("Meal Plan Balances", "hwwkcbrd.P_Display", R.layout.meal_plan));
		addPage(new CalendarSchedule("Calendar Schedule", "bwskfshd.P_CrseSchd?start_date_in=[DATE]", R.layout.calendar_schedule));
		addPage(new DetailSchedule("Detail Schedule", "bwskfshd.P_CrseSchdDetl", R.layout.detail_schedule));
		addPage(new AdvisorInfo("Acedemic Advisor Information", "hwwksadv.P_Summary", R.layout.advisor_info));
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

	public static void loadResource(Page page) {
		String pageHtml = WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/" + page.url);
		page.fillContent(pageHtml);
	}

	@Override
	public void run() {
		Content.loadResources();
	}
}
