package edu.wpi.tmathmeyer.mybannerwebwpi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.wpi.tmathmeyer.mybannerwebwpi.content.Content;

/**
 * A fragment representing a single Info detail screen. This fragment is either
 * contained in a {@link InfoListActivity} in two-pane mode (on tablets) or a
 * {@link InfoDetailActivity} on handsets.
 */
public class Mailbox extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Content.Item mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public Mailbox() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = Content.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mailbox,
				container, false);

		if (mItem != null) {
			WebReader.getInstance("", "").sendGetRequest("https://bannerweb.wpi.edu/pls/prod/"+mItem.url);
			
			//((TextView) rootView.findViewById(R.id.info_detail)).setText(mItem.HTML);
		}

		return rootView;
	}
}
