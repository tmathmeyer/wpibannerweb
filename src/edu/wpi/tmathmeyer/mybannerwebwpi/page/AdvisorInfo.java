package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.wpi.tmathmeyer.mybannerwebwpi.R;

public class AdvisorInfo extends Page {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.advisor_info, container, false);
	}
	
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fillContent();
	}
	
	@Override
	public void loadContent(String html) {
		Log.d("ndtc", "Advisor.loadContent");
		Document doc = Jsoup.parse(html, "https://bannerweb.wpi.edu/pls/prod/");
		Element body = doc.body();
		Element pagebodydiv = body.getElementsByClass("pagebodydiv").first();
		Elements bolds = pagebodydiv.getElementsByTag("b");
		for (Element bold : bolds) {
			Log.d("bold", bold.toString());
			Node node = bold.nextSibling();
			String key = bold.text();
			String sibling = bold.nextElementSibling().text();
			String val = node.toString();
			if (key.contains("Primary Advisor")) {
				contentMap.put("Primary Advisor", val);
			} else if (key.contains("Email")) {
				contentMap.put("Email", val);
			} else if (key.contains("Advisor Department")) {
				contentMap.put("Advisor Department", val);
			} else if (key.contains("Office Location")) {
				contentMap.put("Office Location", val);
			}
		}
	}
	
	@Override 
	public void fillContent() {
		TextView tvPrimaryAdvisor = (TextView) getView().findViewById(R.id.tv_primary_advisor);
		TextView tvEmail = (TextView) getView().findViewById(R.id.tv_email);
		TextView tvAdvisorDept = (TextView) getView().findViewById(R.id.tv_advisor_department);
		TextView tvOfficeLocation = (TextView) getView().findViewById(R.id.tv_office_location);
		
		tvPrimaryAdvisor.setText("Primary Advisor: " + contentMap.get("Primary Advisor"));
		tvEmail.setText("Email: " + contentMap.get("Email"));
		tvAdvisorDept.setText("Advisor Department: " + contentMap.get("Advisor Department"));
		tvOfficeLocation.setText("Office Location: " + contentMap.get("Office Location"));
	}
}