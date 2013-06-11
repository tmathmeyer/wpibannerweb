package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.tmathmeyer.mybannerwebwpi.WebReader;

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
		addItem(new Item("Addresses and Phones", "bwgkogad.P_SelectAtypView"));
		addItem(new Item("Email Addresses", "bwgkogad.P_SelectEmalView"));
		addItem(new Item("Mail Box Information", "hwwkboxs.P_ViewBoxs"));
		addItem(new Item("Meal Plan Balances", "hwwkcbrd.P_Display"));
		addItem(new Item("Emergency Contacts", "bwgkoemr.P_ViewEmrgContacts"));
		
		addItem(new Item("View Holds", "bwskoacc.P_ViewHold"));
		addItem(new Item("Final Grades", "bwskogrd.P_ViewGrde"));
		addItem(new Item("Unofficial Transcript", "bwskotrn.P_ViewTran"));
		addItem(new Item("Degree Evaluation", "bwckcapp.P_DispCurrent"));
		addItem(new Item("Calendar Schedule", "bwskfshd.P_CrseSchd?start_date_in=[DATE]"));
		addItem(new Item("Detail Schedule", "bwskfshd.P_CrseSchdDetl"));
	}

	private static void addItem(Item item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
	
	public static void loadResources(){
		for(Item i : ITEMS){
			WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/"+i.id);
			i.HTML = WebReader.getInstance("", "").HTML;
		}
		for(Item i : ITEMS){
			i.HTML = Parser.parse(i.HTML);
		}
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class Item {
		public String id;
		public String title;
		public String HTML = "Not availible at this time!";

		public Item(String content, String url) {
			this.id = url;
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
