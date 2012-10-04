package com.gemserk.google.ads.mediation.revmob;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.revmob.RevMob;
import com.revmob.ads.EnvironmentConfig;
import com.revmob.ads.RevMobAdsListener;
import com.revmob.ads.banner.Banner;

public class RevMobCustomEventBanner implements CustomEventBanner {
	
	private class RevMobBannerListener implements RevMobAdsListener {
		
		private final ViewGroup view;
		private final CustomEventBannerListener listener;

		public RevMobBannerListener(CustomEventBannerListener listener, ViewGroup view) {
			this.listener = listener;
			this.view = view;
		}

		@Override
		public void onRevMobAdReceived() {
			Log.d(Tag, "Ad received");
			listener.onReceivedAd(view);
		}

		@Override
		public void onRevMobAdNotReceived(String message) {
			Log.d(Tag, "Ad not received");
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onRevMobAdDismiss() {
			Log.d(Tag, "Ad dismiss");
			listener.onDismissScreen();
		}

		@Override
		public void onRevMobAdClicked() {
			Log.d(Tag, "Ad clicked");
			listener.onClick();
		}
	}

	private static final String Tag = "RevmobBannerAdapter";
	
	private String appId;
	private String placementId;

	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest) {
		Log.d(Tag, "Ad request received with parameters " + serverParameter);

		boolean testMode = mediationAdRequest.isTesting();
		
		if (testMode) {
			Log.d(Tag, "Test mode enabled");
			EnvironmentConfig.setTestingMode(testMode);
		}

		String[] parameters = serverParameter.split(",");

		if (parameters.length == 0) {
			Log.d(Tag, "Wrong parameters received ");
			return;
		}

		appId = parameters[0];

		if (parameters.length >= 2)
			placementId = parameters[1];
		
		RevMob revMob = RevMobInstance.getInstance(activity, appId);

		Log.d(Tag, "Starting banner ad request with appId: " + appId + ", placementId: " + placementId);
		
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = (int) (size.getWidth() * metrics.density);
		int height = (int) (size.getHeight() * metrics.density);
		
		Banner banner = revMob.createBanner(activity, placementId);
		banner.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		banner.setAdsListener(new RevMobBannerListener(listener, banner));
	}

}
