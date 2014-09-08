package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import android.text.Html;
import android.util.Log;

public class MealPlan extends Page {

	public MealPlan(String title, String url) {
		super(title, url);
	}

	private void parse() {
		try {
			html = html.substring(html.indexOf("datadisplaytable") + 20);
			html = html.substring(0, html.indexOf("</TABLE"));
			html = Html.fromHtml("<table>" + html + "</table>").toString();
			html = html.replaceAll("AccountBalanceDate Last Used", "");
			String[] lines = html.split("\n");
			String message = "Type                Balance";
			for (int i = 0; i < lines.length - 3; i += 3) {
				message += "\n" + lines[i] + mult(20 - lines[i].length(), " ") + lines[i + 2];
			}
			//this.content = message;
		} catch (Exception e) {
			for (StackTraceElement k : e.getStackTrace()) {
				Log.d("BB+", "String Error!: " + k.toString());
			}
		}
	}

	private static String mult(int i, String s) {
		Log.d("BB+", i + "");
		if (i <= 1)
			return s;
		if (i % 2 == 1)
			return s + mult(i - 1, s);
		return mult(i / 2, s + s);
	}
}
