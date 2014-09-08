package edu.wpi.tmathmeyer.mybannerwebwpi.page;

public class Page {

	public String url;
	public String title;
	public String content = "Not availible at this time!";
	//layout

	public Page(String title, String url) {
		this.url = url;
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getContent() {
		return this.content;
	}

	public void setHTML(String html) {
	}
}
