package com.tmathmeyer.wpi.bannerweb.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tmathmeyer.wpi.bannerweb.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdvisorInfo extends Page
{

    private static final String PRIMARY_ADVISOR = "Primary Advisor";
    private static final String EMAIL = "Email";
    private static final String DEPARTMENT = "Advisor Department";
    private static final String LOCATION = "Office Location";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.advisor_info, container, false);
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

        Element nameE = body.getElementsContainingOwnText(PRIMARY_ADVISOR).first();
        String name = nameE.nextSibling().toString().trim();
        contentMap.put(PRIMARY_ADVISOR, name);

        Element emailE = body.getElementsByAttributeValueContaining("href", "mailto").first();
        String email = emailE.text().trim();
        contentMap.put(EMAIL, email);

        Element departmentE = body.getElementsContainingOwnText(DEPARTMENT).first();
        String department = departmentE.nextSibling().toString().trim();
        contentMap.put(DEPARTMENT, department);

        Element locationE = body.getElementsContainingOwnText(LOCATION).first();
        String location = locationE.nextSibling().toString().trim();
        contentMap.put(LOCATION, location);
    }

    @Override
    public void fillContent()
    {
        TextView tvPrimaryAdvisor = (TextView) getView().findViewById(R.id.tv_primary_advisor);
        TextView tvEmail = (TextView) getView().findViewById(R.id.tv_email);
        TextView tvAdvisorDept = (TextView) getView().findViewById(R.id.tv_advisor_department);
        TextView tvOfficeLocation = (TextView) getView().findViewById(R.id.tv_office_location);

        tvPrimaryAdvisor.setText("Primary Advisor: " + contentMap.get(PRIMARY_ADVISOR));
        tvEmail.setText("Email: " + contentMap.get(EMAIL));
        tvAdvisorDept.setText("Advisor Department: " + contentMap.get(DEPARTMENT));
        tvOfficeLocation.setText("Office Location: " + contentMap.get(LOCATION));
    }
}