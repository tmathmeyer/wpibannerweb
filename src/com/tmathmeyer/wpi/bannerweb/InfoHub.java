package com.tmathmeyer.wpi.bannerweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class InfoHub extends Activity
{
    private static InfoHub INSTANCE = null;
    public static InfoHub getInfoHub()
    {
        while(INSTANCE == null);
        return INSTANCE;
    }
    
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        
        setContentView(R.layout.activity_info_hub);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.info_hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_logout)
        {
            HTTPBrowser.getInstance().logOut();
            Intent intent = new Intent(InfoHub.this, LoginActivity.class);
            InfoHub.this.startActivity(intent);
            InfoHub.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
