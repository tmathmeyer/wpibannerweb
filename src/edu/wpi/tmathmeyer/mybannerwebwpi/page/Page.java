package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.tmathmeyer.mybannerwebwpi.R;

public abstract class Page {


	public static List<Page> items = new ArrayList<Page>();
	public static Map<String,Page> item_map = new HashMap<String,Page>();
	
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

	
	public String url;
	public String title;
	public int layoutId;

	public Page(String title, String url, int layoutId) {
		this.url = url;
		this.layoutId = layoutId;
		this.title = title;
	}

	public abstract void fillContent(String html);

	public String toString() {
		return title;
	}

}
