package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class AdvisorInfo implements Page {

	private String content = null;
	private String html;

	@Override
	public String getContent() {
		if (this.content == null)
			this.parse();
		return this.content;
	}

	@Override
	public void setHTML(String html) {
		this.html = html;
	}

	private void parse() {
		try {
			Document doc = Jsoup.parse(html, "https://bannerweb.wpi.edu/pls/prod/");
			Element body = doc.body();
			Elements div = body.getElementsByClass("https://bannerweb.wpi.edu/pls/prod/");
			for (Element e : div) {
			}
			Log.e("body", body.toString());
		} catch (Exception e) {
			for (StackTraceElement k : e.getStackTrace()) {
				Log.d("BB+", "String Error!: " + k.toString());
			}
		}
	}
}
