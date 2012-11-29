package com.gemserk.google.ads.mediation.millenialmedia;

import java.util.Hashtable;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMAdView.MMAdListener;
import com.millennialmedia.android.MMAdViewSDK;

public class MillenialMediaCustomEventInterstitial implements CustomEventInterstitial {

	private static final String TAG = "MillenialMediaCustomEventInterstitial";

	class MillenialMediaInterstitialListener implements MMAdListener {

		private CustomEventInterstitialListener listener;

		public MillenialMediaInterstitialListener(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public void MMAdCachingCompleted(MMAdView arg0, boolean arg1) {
			Log.d(TAG, "Ad caching completed");
		}

		@Override
		public void MMAdClickedToOverlay(MMAdView arg0) {
			Log.d(TAG, "Ad clicked to overlay");
			listener.onLeaveApplication();
		}

		@Override
		public void MMAdFailed(MMAdView arg0) {
			Log.d(TAG, "Ad failed");
			listener.onFailedToReceiveAd();
		}

		@Override
		public void MMAdOverlayLaunched(MMAdView arg0) {
			Log.d(TAG, "Ad overlay launched");
			listener.onPresentScreen();
		}

		@Override
		public void MMAdRequestIsCaching(MMAdView arg0) {
			Log.d(TAG, "Ad request is caching");
		}

		@Override
		public void MMAdReturned(MMAdView arg0) {
			Log.d(TAG, "Ad returned");
			listener.onReceivedAd();
		}

	}

	public static final String DEFAULT_AD_TYPE = MMAdView.FULLSCREEN_AD_TRANSITION;

	public static final Hashtable<String, String> metadata = new Hashtable<String, String>();

	private MMAdView mmAdView;

	@Override
	public void destroy() {
		mmAdView = null;
	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		String apId = serverParameter;

		if (apId == null || "".equals(apId)) {
			Log.e(TAG, "failed with invalid serverParameter: " + serverParameter);
			listener.onFailedToReceiveAd();
			return;
		}

		Log.d(TAG, "request received with serverParameter: " + serverParameter);

		mmAdView = new MMAdView(activity, apId, DEFAULT_AD_TYPE, true, metadata);
		mmAdView.setId(MMAdViewSDK.DEFAULT_VIEWID + 1);
		mmAdView.setListener(new MillenialMediaInterstitialListener(listener));

		Log.d(TAG, "starting to fetch an ad");

		mmAdView.fetch();
	}

	@Override
	public void showInterstitial() {
		if (mmAdView.check()) {
			Log.d(TAG, "showing ad");
			mmAdView.display();
		}
	}

}
