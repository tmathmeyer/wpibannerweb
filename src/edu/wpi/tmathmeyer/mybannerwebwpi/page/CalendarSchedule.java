package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import android.util.Log;

public class CalendarSchedule extends Page{

	public CalendarSchedule(String title, String url) {
		super(title, url);
	}

	private String content = null;
	private String html;
	

	@Override
	public String getContent() {
		if (this.content == null)
			this.parse();
		return this.content;
	}

	@Override
	public void setHTML(String html) {
		this.html = html;
	}
	
	private void parse() {
		try{
			String[] lines = html.split("\n");
			String boxnum = null;
			String combo = null;
			
			for(int i = 100; i<lines.length; i++){
				if (lines[i].contains("datadisplaytable")){
					String[] info = new String[4];
					int pos = 0;
					for(int j = i+2; pos<4; j+=3){
						if (lines[j].contains("<B>")) {
							String[] p = lines[j].split("B>");
							info[pos] = p[p.length-2].replaceAll("</", "");
							pos++;
						}
					}
					boxnum = info[0];
					combo = info[1]+" : "+info[2]+" : "+info[3];
				}
			}
			this.content = "Mail Box Number:\n   "+boxnum+"\n\n\nCombination:\n   "+combo;
		}
		catch(Exception e){
			for(StackTraceElement k : e.getStackTrace()){
				Log.d("BB+","String Error!: "+k.toString());
			}
		}
	}
	
	private static String mult(int i, String s){
		Log.d("BB+", i+"");
		if (i<=1)return s;
		if (i%2==1)return s + mult(i-1, s);
		return mult(i/2, s+s);
	}
}
