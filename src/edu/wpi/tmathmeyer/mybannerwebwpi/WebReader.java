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

import edu.wpi.tmathmeyer.mybannerwebwpi.content.Content;

import android.util.Log;

public class WebReader implements Runnable{
	private AbstractHttpClient client = new DefaultHttpClient();
	private HttpResponse response = null;
	public String HTML = "";
	final String TAG = "BB+";
	private String uname;
	private String password;
	
	static WebReader instance = null;
	
	public static WebReader getInstance(String username, String password){
		if (instance == null){
			instance = new WebReader();
			instance.uname = username;
			instance.password = password;
		}
		return instance;
	}
	
	public static void killInstance(){
		instance = null;
	}
	
	
	public void sendGetRequest(String URL){
		HttpGet request = new HttpGet(URL);
		Log.d(TAG,URL);
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if(entity == null)
				throw new Exception("bad entity");
			HTML =  EntityUtils.toString(entity);
		}
		catch(Exception e){
			Log.d(TAG, e.toString());
		}
	}
	
	public boolean sendPostRequest(String username, String password){
		try {
			HttpPost post = new HttpPost("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", uname));
			params.add(new BasicNameValuePair("PIN", password));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if(entity == null)
				throw new Exception("bad entity");
			HTML =  EntityUtils.toString(entity);
			if (HTML.contains("http-equiv=\"refresh\"")){
				this.sendGetRequest("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu");
				return true;
			}
		}
		catch(Exception e){
			Log.d(TAG, e.toString());
		}
		return false;
	}
	
	public boolean initLogin(){
		this.sendGetRequest("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");
		if (this.sendPostRequest(this.uname, this.password)){
			new Content();
			return true;
		}
		return false;
	}


	@Override
	public void run() {
		//this.sendGetRequest("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");
		//this.sendPostRequest(this.uname, this.password);
	}
}