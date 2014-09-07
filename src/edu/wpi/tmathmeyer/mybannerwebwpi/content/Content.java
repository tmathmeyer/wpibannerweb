package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.tmathmeyer.mybannerwebwpi.WebReader;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.MailBox;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.MealPlan;
import edu.wpi.tmathmeyer.mybannerwebwpi.page.Page;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Content implements Runnable{

	/**
	 * An array of items.
	 */
	public static List<Item> ITEMS = new ArrayList<Item>();

	/**
	 * A map of items, by ID.
	 */
	public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

	static {
		addItem(new Item(null, "Addresses and Phones", "bwgkogad.P_SelectAtypView"));
		addItem(new Item(null, "Email Addresses", "bwgkogad.P_SelectEmalView"));
		addItem(new Item(new MailBox(), "Mail Box Information", "hwwkboxs.P_ViewBoxs"));
		addItem(new Item(new MealPlan(), "Meal Plan Balances", "hwwkcbrd.P_Display"));
		addItem(new Item(null, "Emergency Contacts", "bwgkoemr.P_ViewEmrgContacts"));
		
		addItem(new Item(null, "View Holds", "bwskoacc.P_ViewHold"));
		addItem(new Item(null, "Final Grades", "bwskogrd.P_ViewGrde"));
		addItem(new Item(null, "Unofficial Transcript", "bwskotrn.P_ViewTran"));
		addItem(new Item(null, "Degree Evaluation", "bwckcapp.P_DispCurrent"));
		addItem(new Item(null, "Calendar Schedule", "bwskfshd.P_CrseSchd?start_date_in=[DATE]"));
		addItem(new Item(null, "Detail Schedule", "bwskfshd.P_CrseSchdDetl"));
	}

	private static void addItem(Item item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.url, item);
	}
	
	public static void loadResources(){
		for(Item i : ITEMS){
			if (i.parser != null){
				i.parser.setHTML(WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/"+i.url));
				i.HTML = i.parser.getContent();
			}
			else {
				i.HTML = "Feature Coming Soon!\nEmail tjmeyer@wpi.edu if you'd like\nto help implement it";
			}
		}
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class Item {
		public String url;
		public String title;
		public String HTML = "Not availible at this time!";
		public Page parser;

		public Item(Page p, String content, String url) {
			this.url = url;
			this.parser = p;
			this.title = content;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	@Override
	public void run() {
		Content.loadResources();
	}
}
