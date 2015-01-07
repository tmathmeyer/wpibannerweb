package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import edu.wpi.tmathmeyer.mybannerwebwpi.R;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MealPlan extends Page
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.meal_plan, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

	public void loadContent(String html)
	{
		try
		{
			html = html.substring(html.indexOf("datadisplaytable") + 20);
			html = html.substring(0, html.indexOf("</TABLE"));
			html = Html.fromHtml("<table>" + html + "</table>").toString();
			html = html.replaceAll("AccountBalanceDate Last Used", "");
			String[] lines = html.split("\n");
			String message = "Type                Balance";
			for (int i = 0; i < lines.length - 3; i += 3)
			{
				message += "\n" + lines[i] + mult(20 - lines[i].length(), " ") + lines[i + 2];
			}
			// this.content = message;
		} catch (Exception e)
		{
			for (StackTraceElement k : e.getStackTrace())
			{
				Log.d("BB+", "String Error!: " + k.toString());
			}
		}
	}

	public void fillContent()
	{

	}

	private static String mult(int i, String s)
	{
		Log.d("BB+", i + "");
		if (i <= 1)
			return s;
		if (i % 2 == 1)
			return s + mult(i - 1, s);
		return mult(i / 2, s + s);
	}
}
