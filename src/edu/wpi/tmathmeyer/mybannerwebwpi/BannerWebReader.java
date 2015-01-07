package edu.wpi.tmathmeyer.mybannerwebwpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class BannerWebReader
{
	public static final String TAG = "BB+";
	public static final String LOGIN_PAGE = "https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin";
	
	private static BannerWebReader instance = new DeactivatedBannerWebReader();

	
	/**	
	 * get the singleton instance of the virtual web browser
	 */
	public static BannerWebReader getInstance()
	{
		return instance;
	}
	
	public static void deactivate()
	{
		instance = new DeactivatedBannerWebReader();
	}
	
	public abstract BannerWebReader activate(String username, String password) throws ClientProtocolException, IOException, BannerwebException;
	public abstract String sendGetRequest(String URL) throws ClientProtocolException, IOException, BannerwebException;
	
	
	private static class ActivatedBannerWebReader extends BannerWebReader
	{
		private AbstractHttpClient client = new DefaultHttpClient();
		private boolean retry = true;
		private boolean badPassword = false;
		
		public ActivatedBannerWebReader(String username, String password)
        {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", username));
			params.add(new BasicNameValuePair("PIN", password));
			try
			{
				sendGetRequest(LOGIN_PAGE);
				HttpPost post = new HttpPost(LOGIN_PAGE);
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				
				HttpEntity entity = getEntityFrom(post);
				String response = EntityUtils.toString(entity);
				if (response.contains("http-equiv=\"refresh\""))
				{
					retry = false;
				}
				if (response.contains("Social Security"))
				{
					badPassword = true;
				}
			}
			catch(Exception e)
			{
				Log.e(TAG, "failed to log in", e);
			}
        }

		@Override
        public BannerWebReader activate(String username, String password) throws ClientProtocolException, IOException, BannerwebException
        {
	        throw new BannerwebException("can't re-activate");
        }
		
		@Override
		public String sendGetRequest(String URL) throws ClientProtocolException, IOException, BannerwebException
		{
			HttpEntity entity = getEntityFrom(new HttpGet(URL));
			if (entity == null)
			{
				throw new BannerwebException("entity is null");
			}
			return EntityUtils.toString(entity);
		}
		
		private HttpEntity getEntityFrom(HttpUriRequest hur) throws ClientProtocolException, IOException
		{
			HttpResponse response = client.execute(hur);
			if (response == null)
			{
				return null;
			}
			return response.getEntity();
		}
	}
	
	private static class DeactivatedBannerWebReader extends BannerWebReader
	{
		@Override
		public BannerWebReader activate(String username, String password) throws ClientProtocolException, IOException, BannerwebException
		{
			try
			{
				ActivatedBannerWebReader result = new ActivatedBannerWebReader(username, password);
				while (result.retry)
				{
					if (result.badPassword)
					{
						throw new BannerwebException("bad password");
					}
					//try again... bannerweb is shitty like that
					Thread.sleep(1000);
					result = new ActivatedBannerWebReader(username, password);
				}
				instance = result;
				return instance;
			}
			catch(Exception e)
			{
				Log.d(TAG, "couldn't login", e);
				return null;
			}
		}

		@Override
        public String sendGetRequest(String URL) throws ClientProtocolException, IOException, BannerwebException
        {
	        throw new BannerwebException("Cant send request to unactivated reader");
        }
	}
}