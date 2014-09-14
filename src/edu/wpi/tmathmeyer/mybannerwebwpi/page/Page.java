package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class Page extends Fragment {

	private String mUrl;
	private String mTitle;
	public HashMap<String, String> contentMap = new HashMap<String, String>();

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	
	public abstract void loadContent(String html);
	
	public abstract void fillContent();
	
	public String toString() {
		return mTitle;
	}
	
	public static Page newInstance(Class<?> pageType, String title, String url) {
		Log.d("ndtc", "Page.newInstance");
		try {
			Page newPage = (Page) pageType.newInstance();
			newPage.setUrl(url);
			newPage.setTitle(title);
			return newPage;
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
