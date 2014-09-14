package edu.wpi.tmathmeyer.mybannerwebwpi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class WebReader {

	/**
	 * Static Data and Methods
	 */

	private static WebReader instance = null;

	/**
	 * get the singleton instance of the virtual web browser
	 * 
	 */
	public static WebReader getInstance(String username, String password) {
		if (instance == null) {
			instance = new WebReader();
			instance.uname = username;
			instance.password = password;
		}
		return instance;
	}

	/**
	 * force the singleton instance to commit sepuku
	 */
	public static void killInstance() {
		instance = null;
	}

	/**
	 * Instance Data and Methods
	 */

	private AbstractHttpClient client = new DefaultHttpClient();
	private HttpResponse response = null;
	private String HTML = "";
	private final String TAG = "BB+";
	private String uname;
	private String password;

	/**
	 * sends a get request to the given URL
	 */
	public String sendGetRequest(String URL) {
		HttpGet request = new HttpGet(URL);
		Log.d(TAG, URL);
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity == null)
				throw new Exception("bad entity");
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			Log.d(TAG, e.toString());
			return e.toString();
		}
	}

	/**
	 * 
	 * sends a post request to the server (and returns whether the user has
	 * logged in)
	 */
	public boolean sendLoginRequest(String username, String password) {
		try {
			HttpPost post = new HttpPost("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", uname));
			params.add(new BasicNameValuePair("PIN", password));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new Exception("bad entity");
			}
			HTML = EntityUtils.toString(entity);
			if (HTML.contains("http-equiv=\"refresh\"")) {
				// this.sendGetRequest("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu");
				return true;
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
		return false;
	}

	/**
	 * 
	 * attempt to log in by sending a post request with the acct info
	 */
	public boolean initLogin() {
		this.HTML = this.sendGetRequest("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");
		// try loggin in
		return this.sendLoginRequest(this.uname, this.password);
	}
}