package com.gemserk.google.ads.mediation.revmob;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.revmob.RevMob;
import com.revmob.ads.EnvironmentConfig;
import com.revmob.ads.RevMobAdsListener;
import com.revmob.ads.fullscreen.Fullscreen;

public class RevmobCustomEventInterstitial implements CustomEventInterstitial {

	private static final String Tag = "RevmobInterstitialAdapter";
	private static RevMob revmob = null;

	private class RecMobAdslistener implements RevMobAdsListener {

		private final CustomEventInterstitialListener listener;

		public RecMobAdslistener(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public void onRevMobAdReceived() {
			Log.d(Tag, "Ad received");
			listener.onReceivedAd();
		}

		@Override
		public void onRevMobAdNotReceived(String message) {
			Log.d(Tag, "Ad failed: " + message);
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onRevMobAdDismiss() {
			Log.d(Tag, "On ad dismiss");
			listener.onDismissScreen();
		}

		@Override
		public void onRevMobAdClicked() {
			Log.d(Tag, "On ad clicked");
			listener.onLeaveApplication();
		}
	}

	private String appId;
	private String placementId;
	private Fullscreen fullscreen;

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest) {
		Log.d(Tag, "Ad request received with parameters " + serverParameter);

		EnvironmentConfig.setTestingWithoutAds(mediationAdRequest.isTesting());

		String[] parameters = serverParameter.split(",");

		if (parameters.length == 0) {
			Log.d(Tag, "Wrong parameters received ");
			return;
		}

		appId = parameters[0];

		if (parameters.length >= 2)
			placementId = parameters[1];

		if (!RevMob.hasSession())
			revmob = RevMob.start(activity, appId);

		Log.d(Tag, "Starting fullscreen ad request with appId: " + appId + ", placementId: " + placementId);

		if (placementId == null)
			fullscreen = revmob.createFullscreen(activity);
		else
			fullscreen = revmob.createFullscreen(activity, placementId);

		fullscreen.setRevMobListener(new RecMobAdslistener(listener));
	}

	@Override
	public void showInterstitial() {
		if (fullscreen.isAdLoaded()) {
			fullscreen.show();
		}
	}

}
