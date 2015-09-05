package com.tmathmeyer.wpi.bannerweb.page;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tmathmeyer.wpi.bannerweb.InfoHub;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AdvisorInfo extends Page
{

    private static final String PRIMARY_ADVISOR = "Primary Advisor";
    private static final String EMAIL = "Email";
    private static final String DEPARTMENT = "Advisor Department";
    private static final String LOCATION = "Office Location";

    private Map<String, String> contentMap = new HashMap<>();
    private LinearLayout cardViewContents = null;
    
    @Override
    public void loadContent(Map<String, String> pages)
    {
        cardViewContents = null;
        
        Document doc = Jsoup.parse(pages.get("advisor"), "https://bannerweb.wpi.edu/pls/prod/");
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
        
        cardViewContents = null;
    }

    @Override
    public View getCardViewContents()
    {
        if (cardViewContents == null)
        {
            cardViewContents = new LinearLayout(InfoHub.getInfoHub());
            cardViewContents.setOrientation(LinearLayout.VERTICAL);
            TextView title = new TextView(InfoHub.getInfoHub());
            title.setText("Academic Advisor");
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80);
            title.setPadding(20, 20, 20, 20);
            title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            cardViewContents.addView(title);
            
            TableLayout cardViewContentsTable = new TableLayout(InfoHub.getInfoHub());
            cardViewContentsTable.setId(1231);
            
            for(Entry<String, String> kvp : contentMap.entrySet())
            {
                TableRow row = new TableRow(InfoHub.getInfoHub());
                TextView key = new TextView(InfoHub.getInfoHub());
                TextView val = new TextView(InfoHub.getInfoHub());
                
                key.setText(kvp.getKey());
                val.setText(kvp.getValue());

                key.setPadding(20, 20, 20, 20);
                val.setPadding(20, 20, 20, 20);
                
                row.addView(key);
                row.addView(val);
                
                cardViewContentsTable.addView(row);
            }
            cardViewContents.addView(cardViewContentsTable);
        }
        return cardViewContents;
    }


    @Override
    @SuppressWarnings("serial")
    public Map<String, String> getUrlsByTag()
    {
        return new HashMap<String, String>(){{
           put("advisor", "https://bannerweb.wpi.edu/pls/prod/hwwksadv.P_Summary");
        }};
    }
}