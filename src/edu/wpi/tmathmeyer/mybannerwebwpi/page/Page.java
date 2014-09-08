package edu.wpi.tmathmeyer.mybannerwebwpi.page;

public class Page {

	public String url;
	public String html;
	public String title;
	public int layoutId;

	public Page(String title, String url) {
		this.url = url;
		this.title = title;
	}	
	
	@Override
	public String toString() {
		return title;
	}

}
