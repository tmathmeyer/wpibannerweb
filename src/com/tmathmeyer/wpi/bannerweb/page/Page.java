package com.tmathmeyer.wpi.bannerweb.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tmathmeyer.wpi.bannerweb.HTTPBrowser;

import android.view.View;

public abstract class Page
{
    public abstract View getCardViewContents();
    public abstract void loadContent(Map<String, String> htmlPages);
    public abstract Map<String, String> getUrlsByTag();
    
    private static Set<Page> pages = null;
    
    public Page update() throws IOException
    {
        Map<String, String> result = new HashMap<>();
        for(Entry<String, String> kvp : getUrlsByTag().entrySet())
        {
            result.put(kvp.getKey(), HTTPBrowser.getInstance().getPage(kvp.getValue()));
        }
        loadContent(result);
        return this;
    }
    
    @SuppressWarnings("serial")
    public static Set<Page> getPageSet()
    {
        if (pages == null)
        {
            pages = new HashSet<Page>()
            {{
                add(new AdvisorInfo());
                add(new MailBox());
                add(new MealPlan());
                add(new Transcript());
            }};
        }
        return pages;
    }
}
