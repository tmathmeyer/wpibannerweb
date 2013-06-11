package edu.wpi.tmathmeyer.mybannerwebwpi.content;

import java.lang.reflect.Method;

import android.text.Html;
import android.util.Log;

public class Parser {
	public static String parse(String HTML){
		try{
			String[] title = {HTML.substring(
									HTML.indexOf("<TITLE>")+7,
									HTML.indexOf("</TITLE>")).replaceAll(" ", "")};
			String[] html = {HTML};
			Log.d("BB+",(String) title[0]);
			for(Method M : Parser.class.getMethods()){
				if (M.getName().equals("parse_"+title[0])){
					try{
						Log.d("BB+", (String) M.invoke(null, html));
						return (String)M.invoke(null, html);
					}
					catch(Exception e){
						Log.d("BB+","BadParse!: "+e.toString());
					}
					break;
				}
			}
			return "Feature Coming Soon!\nEmail tjmeyer@wpi.edu if you'd like\nto help implement it";
		}
		catch(Exception e){
			return "Feature Coming Soon!\nEmail tjmeyer@wpi.edu if you'd like\nto help implement it";
		}
	}
	
	public static String parse_MealPlanbalances(String HTML){
		try{
			HTML = HTML.substring(HTML.indexOf("datadisplaytable")+20);
			HTML = HTML.substring(0, HTML.indexOf("</TABLE"));
			HTML = Html.fromHtml("<table>"+HTML+"</table>").toString();
			HTML = HTML.replaceAll("AccountBalanceDate Last Used", "");
			String[] lines = HTML.split("\n");
			String message = "Type              Balance";
			for(int i = 0; i<lines.length-3; i+=3){
				message+="\n"+lines[i]+mult(18-lines[i].length(), " ")+lines[i+2];
			}
			return message;
		}
		catch(Exception e){
			for(StackTraceElement k : e.getStackTrace()){
				Log.d("BB+","String Error!: "+k.toString());
			}
		}
		return "poo";
	}
	public static String mult(int i, String s){
		if (i==1)return s;
		if (i%2==1)return s + mult(i-1, s);
		return mult(i/2, s+s);
	}
}
