package edu.wpi.tmathmeyer.mybannerwebwpi;

import edu.wpi.tmathmeyer.mybannerwebwpi.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		final Button personal_info = (Button) findViewById(R.id.home_page_personal_info);
        personal_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(HomePage.this, InfoListActivity.class);
				HomePage.this.startActivity(intent);
            }
        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}

}



