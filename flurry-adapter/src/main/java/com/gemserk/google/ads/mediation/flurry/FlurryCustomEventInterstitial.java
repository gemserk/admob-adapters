package com.gemserk.google.ads.mediation.flurry;

import java.util.Map;

import android.app.Activity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAgent;
import com.flurry.android.IListener;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class FlurryCustomEventInterstitial implements CustomEventInterstitial {

	public static long FLURRY_INTERSTITIAL_TIMEOUT = 10000L;

	private Activity activity;
	private String adSpace;
	private boolean adAvailable;

	private RelativeLayout viewGroup;

	private class FlurryInterstitialAdListener implements IListener {

		private CustomEventInterstitialListener listener;

		public FlurryInterstitialAdListener(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public void onAdClosed(String arg0) {
			Log.d(FlurryLogTag.Tag, "On ad closed " + arg0);
			listener.onDismissScreen();
		}

		@Override
		public void onApplicationExit(String arg0) {
			Log.d(FlurryLogTag.Tag, "On application exit " + arg0);
			listener.onLeaveApplication();
		}

		@Override
		public void onRenderFailed(String arg0) {
			Log.d(FlurryLogTag.Tag, "On render failed " + arg0);
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onReward(String arg0, Map<String, String> arg1) {
			Log.d(FlurryLogTag.Tag, "On reward " + arg0);
		}

		@Override
		public boolean shouldDisplayAd(String arg0, FlurryAdType flurryAdType) {
			return flurryAdType != FlurryAdType.WEB_BANNER;
		}

	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, //
			String serverParameter, MediationAdRequest mediationAdRequest) {
		Log.d(FlurryLogTag.Tag, "Interstitial ad request from Admob with parameters " + serverParameter);

		this.activity = activity;

		FlurryAgent.initializeAds(activity);
		boolean testing = mediationAdRequest.isTesting();

		if (testing) {
			Log.d(FlurryLogTag.Tag, "Test mode enabled");
			FlurryAgent.enableTestAds(testing);
		}

		adSpace = serverParameter;

		FlurryAgent.setAdListener(new FlurryInterstitialAdListener(listener));

		Log.d(FlurryLogTag.Tag, "Checking if fullscreen ad is available");
		adAvailable = FlurryAgent.isAdAvailable(activity, adSpace, FlurryAdSize.FULLSCREEN, FLURRY_INTERSTITIAL_TIMEOUT);

		if (!adAvailable) {
			Log.d(FlurryLogTag.Tag, "Failed to recieve fullscreen ad.");
			listener.onFailedToReceiveAd();
			return;
		}

		Log.d(FlurryLogTag.Tag, "Received fullscreen ad.");
		listener.onReceivedAd();
	}

	@Override
	public void showInterstitial() {
		if (adAvailable) {
			Log.d(FlurryLogTag.Tag, "Showing already cached fullscreen ad");
			viewGroup = new RelativeLayout(activity);
			FlurryAgent.getAd(activity, adSpace, viewGroup, FlurryAdSize.FULLSCREEN, 0L);
		}
	}

}
