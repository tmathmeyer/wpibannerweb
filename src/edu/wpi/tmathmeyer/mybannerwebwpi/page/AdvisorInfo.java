package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
		Document doc = Jsoup.parse(html, "https://bannerweb.wpi.edu/pls/prod/");
		Element body = doc.body();
		Element pagebodydiv = body.getElementsByClass("pagebodydiv").first();
		Elements bolds = pagebodydiv.getElementsByTag("b");
		for (Element bold : bolds) {
			Node node = bold.nextSibling();
			String key = bold.text();
			String sibling = bold.nextElementSibling().text();
			String val = node.toString();
			if (key.contains("Primary Advisor")) {
				Log.e("keyval", key + " , " + val);
			} else if (key.contains("Email")) {
				Log.e("keyval", key + " , " + sibling);
			} else if (key.contains("Advisor Department")) {
				Log.e("keyval", key + " , " + val);
			} else if (key.contains("Office Location")) {
				Log.e("keyval", key + " , " + val);
			}
		}
	}
}