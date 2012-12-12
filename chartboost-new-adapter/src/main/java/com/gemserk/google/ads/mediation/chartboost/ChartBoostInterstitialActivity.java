package com.gemserk.google.ads.mediation.chartboost;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.chartboost.sdk.Chartboost;

public class ChartBoostInterstitialActivity extends Activity {

	private static final String Tag = "ChartBoostNewCustomEventInterstitial";

	private Chartboost cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		cb = Chartboost.sharedChartboost();
		Log.d(Tag, "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		cb.onStart(this);
		Log.d(Tag, "onStart()");

		// configures the activity delegate in the static delegate.
		ChartBoostNewCustomEventInterstitial.mainDelegate.setActivityDelegate(new ChartBoostDelegateAdapter() {
			@Override
			public void didDismissInterstitial(String paramString) {
				super.didDismissInterstitial(paramString);
				ChartBoostInterstitialActivity.this.finish();
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
		this.cb.onBackPressed();
		super.onBackPressed();
	}

}
