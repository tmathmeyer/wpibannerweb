package edu.wpi.tmathmeyer.mybannerwebwpi.page;

public abstract class Page {

	public String url;
	public String title;
	public int layoutId;

	public Page(String title, String url, int layoutId) {
		this.url = url;
		this.layoutId = layoutId;
		this.title = title;
	}

	public abstract void fillContent(String html);

}
