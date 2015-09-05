package com.tmathmeyer.wpi.bannerweb.page;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tmathmeyer.wpi.bannerweb.InfoHub;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MailBox extends Page
{

    private static final String BOX = "Box Number";
    private static final String COMBO = "Combination";
    
    private Map<String, String> contentMap = new HashMap<>();
    private LinearLayout cardViewContents = null;
    

    @Override
    @SuppressWarnings("serial")
    public Map<String, String> getUrlsByTag()
    {
        return new HashMap<String, String>(){{
           put("mailbox", "https://bannerweb.wpi.edu/pls/prod/hwwkboxs.P_ViewBoxs");
        }};
    }

    @Override
    public void loadContent(Map<String, String> pages)
    {
        Document doc = Jsoup.parse(pages.get("mailbox"), "https://bannerweb.wpi.edu/pls/prod/");
        Element body = doc.body();

        Element boxE1 = body.getElementsContainingOwnText("You have been assigned").first();
        Element boxE2 = boxE1.getElementsByTag("B").first();
        String box = boxE2.text().trim();
        contentMap.put(BOX, box);

        Elements steps = body.getElementsContainingOwnText("Rotate the knob");

        Elements step1 = steps.get(0).getElementsByTag("B");
        String num1 = step1.get(1).text().trim();

        Elements step2 = steps.get(1).getElementsByTag("B");
        String num2 = step2.get(1).text().trim();

        Elements step3 = steps.get(2).getElementsByTag("B");
        String num3 = step3.get(1).text().trim();
        
        contentMap.put(COMBO, num1 + " " + num2 + " " + num3);
        
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
            title.setText("Mailbox Information");
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
}
