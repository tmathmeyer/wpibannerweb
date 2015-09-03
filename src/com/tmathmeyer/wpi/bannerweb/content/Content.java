package com.tmathmeyer.wpi.bannerweb.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.tmathmeyer.wpi.bannerweb.BannerwebException;
import com.tmathmeyer.wpi.bannerweb.ConnectionManager;
import com.tmathmeyer.wpi.bannerweb.page.AdvisorInfo;
import com.tmathmeyer.wpi.bannerweb.page.CalendarSchedule;
import com.tmathmeyer.wpi.bannerweb.page.MailBox;
import com.tmathmeyer.wpi.bannerweb.page.Page;

import android.util.Log;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Content implements Runnable
{

	public static List<Page> items = new ArrayList<Page>();
	public static Map<String, Page> item_map = new HashMap<String, Page>();

	static
	{
		addPage(Page.newInstance(MailBox.class, "Mail Box Information", "hwwkboxs.P_ViewBoxs"));
		// addPage(Page.newInstance(MealPlan.class, "Meal Plan Balances",
		// "hwwkcbrd.P_Display"));
		addPage(Page.newInstance(AdvisorInfo.class, "Academic Advisor Information", "hwwksadv.P_Summary"));
		addPage(Page.newInstance(CalendarSchedule.class, "Calendar Schedule",
		        "bwskfshd.P_CrseSchd?start_date_in=[DATE]"));
		// addPage((DetailSchedule)Page.newInstance("Detail Schedule",
		// "bwskfshd.P_CrseSchdDetl"));
	}

	private static void addPage(Page page)
	{
		items.add(page);
		item_map.put(page.getUrl(), page);
	}

	public static void loadResources() throws ClientProtocolException, IOException, BannerwebException
	{
		for (Page i : items)
		{
			loadResource(i);
		}
	}

	public static void loadResource(Page page) throws ClientProtocolException, IOException, BannerwebException
	{
		String pageHtml = ConnectionManager.getInstance().getPage("https://bannerweb.wpi.edu/pls/prod/" + page.getUrl());
		page.loadContent(pageHtml);
	}

	@Override
	public void run()
	{
		try
        {
	        Content.loadResources();
        }
		catch (Exception e)
        {
        	Log.e("BB+", e.toString());
	        e.printStackTrace();
        }
	}
}
