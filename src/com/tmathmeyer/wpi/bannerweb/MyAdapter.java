package com.tmathmeyer.wpi.bannerweb;

import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.wpi.bannerweb.page.Page;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private List<Page> myDataset = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public CardView mCardView;

        public ViewHolder(LinearLayout v)
        {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public MyAdapter()
    {
        for(Page p : Page.getPageSet())
        {
            myDataset.add(p);
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card_layout, parent, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        View v = myDataset.get(position).getCardViewContents();
        if (v.getParent() != null)
        {
            holder.mCardView.removeView(v);
        }
        holder.mCardView.addView(v);
    }
    
    
    @Override
    public int getItemCount()
    {
        return myDataset.size();
    }
}