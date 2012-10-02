package com.gemserk.google.ads.mediation.mobclix;

import java.util.Set;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.mobclix.android.sdk.MobclixFullScreenAdView;
import com.mobclix.android.sdk.MobclixFullScreenAdViewListener;

public class MobclixCustomEventInterstitial implements CustomEventInterstitial {

	private static final String MobclixAdapterTag = "MobclixCustomEventInterstitial";

	private class MobclixInterstitialListener implements MobclixFullScreenAdViewListener {

		private final CustomEventInterstitialListener listener;
		private final String keywords;

		public MobclixInterstitialListener(CustomEventInterstitialListener listener, String keywords) {
			this.listener = listener;
			this.keywords = keywords;
		}

		@Override
		public String query() {
			return null;
		}

		@Override
		public void onPresentAd(MobclixFullScreenAdView adView) {
			listener.onPresentScreen();
		}

		@Override
		public void onFinishLoad(MobclixFullScreenAdView adView) {
			listener.onReceivedAd();
		}

		@Override
		public void onFailedLoad(MobclixFullScreenAdView adView, int errorCode) {
			Log.d(MobclixAdapterTag, "Failed to load ad, errorCode: " + errorCode);
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onDismissAd(MobclixFullScreenAdView adView) {
			listener.onDismissScreen();
		}

		@Override
		public String keywords() {
			return keywords;
		}
	}

	private MobclixFullScreenAdView adView;

	public static String convertKeywords(Set<String> keywords) {
		if (keywords == null)
			return null;
		if (keywords.isEmpty())
			return null;
		StringBuilder stringBuilder = new StringBuilder();
		for (String keyword : keywords) {
			stringBuilder.append(keyword);
			stringBuilder.append(",");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest) {
		Log.d(MobclixAdapterTag, "Received Mobclix custom interstitial with parameters : " + serverParameter);

		try {
			String keywords = convertKeywords(mediationAdRequest.getKeywords());
			
			Log.d(MobclixAdapterTag, "Using keywords from mediationAdRequest: " + keywords);
			
			adView = new MobclixFullScreenAdView(activity);
			adView.addMobclixAdViewListener(new MobclixInterstitialListener(listener, keywords));

			if (adView.hasAd()) {
				Log.d(MobclixAdapterTag, "Interstitial already cached");
				listener.onReceivedAd();
				return;
			}

			Log.d(MobclixAdapterTag, "Requesting new interstitial");
			adView.requestAd();
		} catch (Exception e) {
			Log.d(MobclixAdapterTag, "Failed to process Mobclix custom event interstitial : " + e.getMessage());
			listener.onFailedToReceiveAd();
		}
	}

	@Override
	public void showInterstitial() {
		Log.d(MobclixAdapterTag, "Displaying cached interstitial");
		adView.displayRequestedAd();
	}

}
