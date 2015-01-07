package edu.wpi.tmathmeyer.mybannerwebwpi.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.wpi.tmathmeyer.mybannerwebwpi.R;

public class MailBox extends Page
{

	private static final String BOX = "Box Number";
	private static final String DIR1 = "Direction 1";
	private static final String NUM1 = "Number 1";
	private static final String DIR2 = "Direction 2";
	private static final String NUM2 = "Number 2";
	private static final String DIR3 = "Direction 3";
	private static final String NUM3 = "Number 3";
	private static final String DIR4 = "Direction 4";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.mailbox, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		fillContent();
	}

	@Override
	public void loadContent(String html)
	{
		Document doc = Jsoup.parse(html, "https://bannerweb.wpi.edu/pls/prod/");
		Element body = doc.body();

		Element boxE1 = body.getElementsContainingOwnText("You have been assigned").first();
		Element boxE2 = boxE1.getElementsByTag("B").first();
		String box = boxE2.text().trim();
		contentMap.put(BOX, box);

		Elements steps = body.getElementsContainingOwnText("Rotate the knob");

		Elements step1 = steps.get(0).getElementsByTag("B");
		String dir1 = step1.get(0).text().trim();
		String num1 = step1.get(1).text().trim();
		contentMap.put(DIR1, dir1);
		contentMap.put(NUM1, num1);

		Elements step2 = steps.get(1).getElementsByTag("B");
		String dir2 = step2.get(0).text().trim();
		String num2 = step2.get(1).text().trim();
		contentMap.put(DIR2, dir2);
		contentMap.put(NUM2, num2);

		Elements step3 = steps.get(2).getElementsByTag("B");
		String dir3 = step3.get(0).text().trim();
		String num3 = step3.get(1).text().trim();
		contentMap.put(DIR3, dir3);
		contentMap.put(NUM3, num3);

		Elements step4 = steps.get(3).getElementsByTag("B");
		String dir4 = step4.get(0).text().trim();
		contentMap.put(DIR4, dir4);
	}

	@Override
	public void fillContent()
	{
		TextView tvBoxNumber = (TextView) getView().findViewById(R.id.tv_box_number);
		TextView tvStep1 = (TextView) getView().findViewById(R.id.tv_step1);
		TextView tvStep2 = (TextView) getView().findViewById(R.id.tv_step2);
		TextView tvStep3 = (TextView) getView().findViewById(R.id.tv_step3);
		TextView tvStep4 = (TextView) getView().findViewById(R.id.tv_step4);

		tvBoxNumber.setText(contentMap.get(BOX));
		tvStep1.setText(contentMap.get(DIR1) + " " + contentMap.get(NUM1));
		tvStep2.setText(contentMap.get(DIR2) + " " + contentMap.get(NUM2));
		tvStep3.setText(contentMap.get(DIR3) + " " + contentMap.get(NUM3));
		tvStep4.setText(contentMap.get(DIR4));
	}
}