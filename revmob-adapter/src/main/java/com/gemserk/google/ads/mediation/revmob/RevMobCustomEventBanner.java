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
import com.revmob.RevMobAdsListener;
import com.revmob.RevMobTestingMode;
import com.revmob.ads.banner.RevMobBanner;

public class RevMobCustomEventBanner implements CustomEventBanner {
	
	private static class RevMobBannerListener implements RevMobAdsListener {
		
		private ViewGroup view;
		private final CustomEventBannerListener listener;
		
		public void setView(ViewGroup view) {
			this.view = view;
		}

		public RevMobBannerListener(CustomEventBannerListener listener) {
			this.listener = listener;
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

		@Override
		public void onRevMobAdDisplayed() {
			Log.d(Tag, "Ad shown");
			listener.onPresentScreen();
		}
	}

	private static final String Tag = "RevmobBannerAdapter";
	
	private String appId;
	private String placementId;

	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest, Object customEventExtra) {
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

		Log.d(Tag, "Starting banner ad request with appId: " + appId + ", placementId: " + placementId);
		
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = (int) (size.getWidth() * metrics.density);
		int height = (int) (size.getHeight() * metrics.density);
		
		RevMobBannerListener revMobBannerListener = new RevMobBannerListener(listener);
		
		RevMobBanner banner = revMob.createBanner(activity, placementId, revMobBannerListener);
		banner.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		
		revMobBannerListener.setView(banner);
	}

	@Override
	public void destroy() {
	}

}
