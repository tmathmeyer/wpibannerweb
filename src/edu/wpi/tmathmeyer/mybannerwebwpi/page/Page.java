package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import edu.wpi.tmathmeyer.mybannerwebwpi.R;

public abstract class Page {

	public static final Page[] PAGES = {new MailBox("Mail Box Information", "hwwkboxs.P_ViewBoxs", R.layout.mailbox),
	new MealPlan("Meal Plan Balances", "hwwkcbrd.P_Display", R.layout.meal_plan),
	new CalendarSchedule("Calendar Schedule", "bwskfshd.P_CrseSchd?start_date_in=[DATE]", R.layout.calendar_schedule),
	new DetailSchedule("Detail Schedule", "bwskfshd.P_CrseSchdDetl", R.layout.detail_schedule),
	new AdvisorInfo("Acedemic Advisor Information", "hwwksadv.P_Summary", R.layout.advisor_info)};
	
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
