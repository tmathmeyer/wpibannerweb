
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

public class MealPlan extends Page
{   
    private Map<String, String> contentMap = new HashMap<>();
    private LinearLayout cardViewContents = null;
    

    @Override
    @SuppressWarnings("serial")
    public Map<String, String> getUrlsByTag()
    {
        return new HashMap<String, String>(){{
           put("card_balances", "https://bannerweb.wpi.edu/pls/prod/hwwkcbrd.P_Display");
        }};
    }

    @Override
    public void loadContent(Map<String, String> pages)
    {
        Document doc = Jsoup.parse(pages.get("card_balances"), "https://bannerweb.wpi.edu/pls/prod/");
        
        Element displayTable = doc.getElementsByClass("datadisplaytable").get(0);
        Elements tableEntries = displayTable.getElementsByTag("TR");
        
        for(Element e : tableEntries)
        {
            Elements tableRow = e.getElementsByClass("dddefault");
            if (tableRow.size() >= 2)
            {
                String account = tableRow.get(0).text();
                String balance = tableRow.get(1).text();
                
                contentMap.put(account, balance);
            }
        }
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
            title.setText("ID Card Balances");
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
