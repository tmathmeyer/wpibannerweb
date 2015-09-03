package com.tmathmeyer.wpi.bannerweb;

import com.tmathmeyer.wpi.bannerweb.content.Content;
import com.tmathmeyer.wpi.bannerweb.page.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;

/**
 * An activity representing a list of Infos. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link InfoDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link InfoListFragment} and the item details (if present) is a
 * {@link InfoDetailFragment}.
 * <p>
 * This activity also implements the required {@link InfoListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class InfoListActivity extends FragmentActivity implements InfoListFragment.Callbacks
{

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_list);

		if (findViewById(R.id.info_detail_container) != null)
		{
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((InfoListFragment) getSupportFragmentManager().findFragmentById(R.id.info_list))
			        .setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link InfoListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id)
	{
		if (mTwoPane)
		{
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Page mPage = Content.item_map.get(id);
			getSupportFragmentManager().beginTransaction().replace(R.id.info_detail_container, mPage).commit();

		} else
		{
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, InfoDetailActivity.class);
			detailIntent.putExtra(InfoDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
