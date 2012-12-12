package com.gemserk.google.ads.mediation.chartboost;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.chartboost.sdk.Chartboost;

public class ChartBoostInterstitialActivity extends Activity {

	private static final String Tag = "ChartBoostInterstitialActivity";

	private Chartboost cb;
	private Handler mHandler;
	private boolean backHandled;
	private boolean showingAds;
	private boolean linkOpened;

	public class CloseAfterAnimationFinishesRunnable implements Runnable {
		@Override
		public void run() {
			// just in case.
			showingAds = false;
			linkOpened = false;
			ChartBoostInterstitialActivity.this.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		cb = Chartboost.sharedChartboost();
		mHandler = new Handler();
		backHandled = false;
		showingAds = false;
		linkOpened = false;
		Log.d(Tag, "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// avoid asking for ads twice if app was minimized and reopened.
		if (showingAds)
			return;

		if (linkOpened) {
			Log.d(Tag, "destroying activity because link was opened");
			ChartBoostInterstitialActivity.this.finish();
			return;
		}

		cb.onStart(this);
		Log.d(Tag, "onStart()");

		// configures the activity delegate in the static delegate.
		ChartBoostNewCustomEventInterstitial.mainDelegate.setActivityDelegate(new ChartBoostDelegateAdapter() {
			@Override
			public void didCloseInterstitial(String paramString) {
				super.didCloseInterstitial(paramString);
				// time to wait for the animation to close
				mHandler.postDelayed(new CloseAfterAnimationFinishesRunnable(), 500);
			}

			@Override
			public void didClickInterstitial(String paramString) {
				super.didClickInterstitial(paramString);
				Log.d(Tag, "link opened");
				linkOpened = true;
				showingAds = false;
			}

			@Override
			public void didShowInterstitial(String paramString) {
				super.didShowInterstitial(paramString);
				showingAds = true;
			}
		});

		cb.showInterstitial();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(Tag, "onStop()");
		cb.onStop(this);
		// removes the delegate from the static delegate.
		ChartBoostNewCustomEventInterstitial.mainDelegate.unsetActivityDelegate();
	}

	@Override
	public void onBackPressed() {
		Log.d(Tag, "onBack()");
		if (backHandled)
			return;
		// mHandler.postDelayed(new CloseAfterAnimationFinishesRunnable(), 260);
		backHandled = true;
		this.cb.onBackPressed();
		// super.onBackPressed();
	}

}
