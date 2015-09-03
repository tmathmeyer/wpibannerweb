package com.tmathmeyer.wpi.bannerweb;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

public class HTTPBrowser
{
    public static final String HOME_PAGE = "https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_WWWLogin";
    public static final String LOGIN_PAGE = "https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin";

    private static HTTPBrowser browser;

    public static HTTPBrowser getInstance()
    {
        if (browser == null)
        {
            browser = new HTTPBrowser();
        }
        return browser;
    }

    private String user = null;
    private String pass = null;
    private String ssid = null;
    private String last_page = null;

    public HTTPBrowser setCredentials(String user, String pass)
    {
        this.user = user;
        this.pass = pass;

        return this;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
            {
                first = false;
            } else
            {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    static String IOToString(java.io.InputStream is)
    {
        @SuppressWarnings("resource")
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        s.close();
        return result;
    }

    public boolean logIn() throws ClientProtocolException, IOException
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", user));
        params.add(new BasicNameValuePair("PIN", pass));

        String result = getPage(LOGIN_PAGE, HOME_PAGE, "POST", params);

        return result.contains("Welcome");
    }

    public boolean logOut()
    {
        InfoHub.getInfoHub().deleteFile("usr");
        InfoHub.getInfoHub().deleteFile("pwd");
        return true;
    }

    public String getPage(String URL) throws IOException
    {
        return getPage(URL, last_page, "GET", null);
    }

    public String getPage(String URL, String referer, String method, List<NameValuePair> params) throws IOException
    {
        last_page = URL;
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.addRequestProperty("Referer", referer);
        conn.addRequestProperty("Cookie", "TESTID=set; SESSID=" + ssid);

        if (params != null)
        {
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
        }

        InputStream in = new BufferedInputStream(conn.getInputStream());
        String response = IOToString(in);

        if (response.contains("Welcome"))
        {
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            if (cookies != null)
            {
                for (String cookie : cookies)
                {
                    HttpCookie cook = HttpCookie.parse(cookie).get(0);
                    if (cook.getName().equals("SESSID"))
                    {
                        ssid = cook.getValue();
                    }
                }
            }
        }

        return response;
    }
}