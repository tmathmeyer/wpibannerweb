package com.tmathmeyer.wpi.bannerweb.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tmathmeyer.wpi.bannerweb.HTTPBrowser;
import com.tmathmeyer.wpi.bannerweb.InfoHub;
import com.tmathmeyer.wpi.bannerweb.util.Tuple;
import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class Transcript extends Page
{
    private List<Tuple<String, Map<String, String>>> contentMap = null;
    private Map<String, String> headers = new HashMap<>();
    private LinearLayout cardViewContents = null;

    {
        headers.put("TRANSFER CREDIT ACCEPTED BY INSTITUTION      -Top-", "Transfer Credit");
    }

    @Override
    @SuppressWarnings("serial")
    public Map<String, String> getUrlsByTag()
    {
        return new HashMap<String, String>() {
            {
                put("transcript_landing", "https://bannerweb.wpi.edu/pls/prod/bwskotrn.P_ViewTermTran");
                put("transcript_post", "https://bannerweb.wpi.edu/pls/prod/bwskotrn.P_ViewTran");
            }
        };
    }

    @Override
    @SuppressWarnings("serial")
    public Page update() throws IOException
    {
        contentMap = new LinkedList<>();
        Map<String, String> urls = getUrlsByTag();
        final String landing = urls.get("transcript_landing");
        final String post = urls.get("transcript_post");
        HTTPBrowser.getInstance().getPage(landing); // pretend to view the page

        loadContent(new HashMap<String, String>() {
            {
                put("transcript_data", HTTPBrowser.getInstance().basicPost(post, new ArrayList<NameValuePair>() {
                    {
                        add(new BasicNameValuePair("levl", ""));
                        add(new BasicNameValuePair("tprt", "01"));
                    }
                }));
            }
        });
        cardViewContents = null;

        return this;
    }

    /**
     * 
     * @return
     */
    private final Map<String, String> castMap()
    {
        return new HashMap<String, String>();
    }

    @Override
    public void loadContent(Map<String, String> pages)
    {
        Document doc = Jsoup.parse(pages.get("transcript_data"), "https://bannerweb.wpi.edu/pls/prod/");
        Element body = doc.body();
        Element datadisplaytable = body.getElementsByClass("datadisplaytable").first();

        Elements entries = datadisplaytable.getElementsByTag("TR");

        String header = null;
        int columnCount = 0;
        int classPosition = -1;
        int gradePosition = -1;
        Tuple<String, Map<String, String>> nextHeader = null;

        for (Element tr : entries)
        {
            if (tr.children().size() > 0)
            {
                String rawHeader = isHeader(tr.children().first().text());
                if (rawHeader != null)
                {
                    header = rawHeader;
                    classPosition = -1;
                    gradePosition = -1;
                    if (nextHeader != null)
                    {
                        contentMap.add(nextHeader);
                    }
                    nextHeader = new Tuple<>(header, castMap());
                }
                else
                {
                    if (tr.getElementsByTag("TH").size() >= 6)
                    {
                        columnCount = tr.children().size();
                        int incr = -1;
                        for (Element th : tr.children())
                        {
                            incr++;
                            switch (th.text())
                            {
                                case "Title":
                                    classPosition = incr;
                                    break;
                                case "Grade":
                                    gradePosition = incr;
                                    break;
                            }
                        }
                    }
                    else
                    {
                        if (classPosition >= 0 && gradePosition >= 0 && header != null)
                        {
                            if (tr.children().size() == columnCount)
                            {
                                nextHeader.getY().put(tr.children().get(classPosition).text(),
                                        tr.children().get(gradePosition).text());
                            }
                        }
                    }
                }
            }
        }
        
        if (nextHeader != null)
        {
            contentMap.add(nextHeader);
        }
    }

    private String isHeader(String title)
    {
        String result = headers.get(title);
        if (result != null)
        {
            return result;
        }
        if (title.contains("Term: "))
        {
            return title.replace("Term: ", "");
        }
        return null;
    }

    private String toCamelCase(String s)
    {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts)
        {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    private String toProperCase(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    public View getCardViewContents()
    {
        if (cardViewContents == null)
        {
            cardViewContents = new LinearLayout(InfoHub.getInfoHub());
            cardViewContents.setOrientation(LinearLayout.VERTICAL);
    
            TextView title = new TextView(InfoHub.getInfoHub());
            title.setText("Transcript");
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80);
            title.setPadding(20, 20, 20, 20);
            title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            cardViewContents.addView(title);
    
            for (Tuple<String, Map<String, String>> semester : contentMap)
            {
                if (semester.getY().size() > 0)
                {
                    TextView semTitle = new TextView(InfoHub.getInfoHub());
                    TableLayout cardViewContentsTable = new TableLayout(InfoHub.getInfoHub());
    
                    semTitle.setText(semester.getX());
                    semTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
                    semTitle.setPadding(10, 10, 10, 10);
                    semTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    cardViewContentsTable.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    cardViewContents.addView(semTitle);
    
                    for (Entry<String, String> kvp : semester.getY().entrySet())
                    {
                        TableRow row = new TableRow(InfoHub.getInfoHub());
                        TextView key = new TextView(InfoHub.getInfoHub());
                        TextView val = new TextView(InfoHub.getInfoHub());
    
                        key.setText(toCamelCase(kvp.getKey()));
                        val.setText(kvp.getValue());
    
                        key.setPadding(20, 20, 20, 20);
                        val.setPadding(20, 20, 20, 20);
    
                        row.addView(key);
                        row.addView(val);
    
                        cardViewContentsTable.addView(row);
                    }
                    cardViewContents.addView(cardViewContentsTable);
                }
            }
        }
        return cardViewContents;
    }
}
