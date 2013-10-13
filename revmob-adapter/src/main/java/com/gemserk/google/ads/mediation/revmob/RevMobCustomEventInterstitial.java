package com.gemserk.google.ads.mediation.revmob;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.RevMobTestingMode;
import com.revmob.ads.fullscreen.RevMobFullscreen;

public class RevMobCustomEventInterstitial implements CustomEventInterstitial {

	private static final String Tag = "RevmobInterstitialAdapter";

	private class RevMobAdslistener implements RevMobAdsListener {

		private final CustomEventInterstitialListener listener;

		public RevMobAdslistener(CustomEventInterstitialListener listener) {
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

		@Override
		public void onRevMobAdDisplayed() {
			Log.d(Tag, "On ad shown");
			listener.onPresentScreen();
		}
	}

	private String appId;
	private String placementId;
	private RevMobFullscreen fullscreen;

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(Tag, "Ad request received with parameters " + serverParameter);

		String[] parameters = serverParameter.split(",");

		if (parameters.length == 0) {
			Log.d(Tag, "Wrong parameters received ");
			return;
		}

		appId = parameters[0];

		if (parameters.length >= 2)
			placementId = parameters[1];

		
		RevMob revMob = RevMobInstance.getInstance(activity, appId);
		
		if (mediationAdRequest.isTesting()) {
			Log.d(Tag, "Test mode enabled");
			revMob.setTestingMode(RevMobTestingMode.WITH_ADS);
		}

		Log.d(Tag, "Starting fullscreen ad request with appId: " + appId + ", placementId: " + placementId);

		RevMobAdslistener revMobAdslistener = new RevMobAdslistener(listener);

		if (placementId == null)
			fullscreen = revMob.createFullscreen(activity, revMobAdslistener);
		else
			fullscreen = revMob.createFullscreen(activity, placementId, revMobAdslistener);

	}

	@Override
	public void showInterstitial() {
		if (fullscreen != null)
			fullscreen.show();
	}

	@Override
	public void destroy() {
		
	}

}
