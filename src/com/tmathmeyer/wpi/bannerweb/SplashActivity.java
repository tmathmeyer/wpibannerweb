package com.tmathmeyer.wpi.bannerweb;

import java.io.FileInputStream;

import com.tmathmeyer.wpi.bannerweb.content.Content;
import com.tmathmeyer.wpi.bannerweb.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author ted
 *
 *         The main splash screen. This is what is shown to the user while the
 *         system attempts to log in.
 */
public class SplashActivity extends Activity
{
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
				{
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0)
					{
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0)
					{
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} else
				{
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE)
				{
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (TOGGLE_ON_CLICK)
				{
					mSystemUiHider.hide();
				} else
				{
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		// findViewById(R.id.dummy_button).setOnTouchListener(
		// mDelayHideTouchListener);

		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			if (AUTO_HIDE)
			{
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run()
		{
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	private class IntentLauncher extends Thread
	{
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run()
		{
			try
			{
				// let the splash screen show for two seconds
				Thread.sleep(2000);

				// if there is a user, attempt to log him in
				if (profileExists())
				{
					FileInputStream fis1 = openFileInput("usr");
					FileInputStream fis2 = openFileInput("pwd");

					int usr_len = fis1.read();
					int pwd_len = fis2.read();

					byte[] pwd_b = new byte[pwd_len];
					byte[] usr_b = new byte[usr_len];

					fis1.read(usr_b);
					fis2.read(pwd_b);

					String pwd = new String(pwd_b);
					String usr = new String(usr_b);
					
					ConnectionManager.getInstance().setCredentials(usr, pwd);
					if (ConnectionManager.getInstance().logIn())
					{
						new Thread(new Content()).start();
						Intent intent = new Intent(SplashActivity.this, InfoListActivity.class);
						SplashActivity.this.startActivity(intent);
						SplashActivity.this.finish();
						return;
					}
				}

				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();

			} catch (Exception e)
			{
				Log.d("BB+", e.getMessage());
				Log.d("BB+", "user had default information which was stored... deleting it now");
				
				deleteFile("usr");
				deleteFile("pwd");
				
				new IntentLauncher().start();
			}

		}
	}

	/**
	 * 
	 * @return whether or not the user has a saved profile on their device, with
	 *         valid data
	 */
	private boolean profileExists()
	{
		String[] names = fileList();
		boolean usr = false;
		boolean pwd = false;
		for (String s : names)
		{
			if (s.equals("usr"))
			{
				usr = true;
			}
			if (s.equals("pwd"))
			{
				pwd = true;
			}
		}
		return usr && pwd;
	}
}
