package com.gemserk.google.ads.mediation.chartboost;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.chartboost.sdk.Chartboost;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class ChartBoostNewCustomEventInterstitial implements CustomEventInterstitial {

	private static final String Tag = "ChartBoostNewCustomEventInterstitial";

	// Should be set in the moment Chartboost instance is created as the main ChartboostDelegate.
	public static ChartBoostMainDelegate mainDelegate = new ChartBoostMainDelegate();

	private Chartboost cb;
	private Activity activity;

	public static class InternalChartBoostDelegate extends ChartBoostDelegateAdapter {

		private CustomEventInterstitialListener listener;

		public InternalChartBoostDelegate(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public void didCacheInterstitial(String paramString) {
			super.didCacheInterstitial(paramString);
			Log.d(Tag, "interstitial cached " + paramString);
			listener.onReceivedAd();
		}

		@Override
		public void didFailToLoadInterstitial(String paramString) {
			super.didFailToLoadInterstitial(paramString);
			Log.d(Tag, "interstitial failed to load " + paramString);
			listener.onFailedToReceiveAd();
		}

		@Override
		public void didClickInterstitial(String paramString) {
			super.didClickInterstitial(paramString);
			Log.d(Tag, "interstitial clicked" + paramString);
			listener.onLeaveApplication();
		}

		@Override
		public void didShowInterstitial(String paramString) {
			super.didShowInterstitial(paramString);
			Log.d(Tag, "interstitial shown " + paramString);
			listener.onPresentScreen();
		}

		@Override
		public void didCloseInterstitial(String paramString) {
			super.didCloseInterstitial(paramString);
			Log.d(Tag, "interstitial closed " + paramString);
			listener.onDismissScreen();
		}

	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		this.activity = activity;

		Log.d(Tag, "Received an interstitial ad request for " + label);

		// access the main delegate, assumes it is not null

		cb = Chartboost.sharedChartboost();

		// cb.setDelegate(new InternalChartBoostDelegate(listener));

		String[] parameters = serverParameter.split(",");
		if (parameters.length != 2) {
			Log.d(Tag, "Invalid parameter " + serverParameter + ", needed \"appId,appSignature\"");
			return;
		}

		String appId = parameters[0];
		String appSignature = parameters[1];

		Log.d(Tag, "Received custom event with appId:" + appId + ", appSignature:" + appSignature);

		// if (ChartBoostNewCustomEventInterstitial.shouldInstall || !appId.equals(cb.getAppId()) || !appSignature.equals(cb.getAppSignature())) {
		// Log.d(ChartBoostCustomEventTag, "Installing chartboost with appId:" + appId + ", appSignature:" + appSignature);
		// cb.setAppId(appId);
		// cb.setAppSignature(appSignature);
		//
		// cb.clearCache();
		// cb.install();
		// ChartBoostNewCustomEventInterstitial.shouldInstall = false;
		// }

		mainDelegate.setAdapterDelegate(new InternalChartBoostDelegate(listener));

		if (cb.hasCachedInterstitial()) {
			Log.d(Tag, "Interstitial already cached");
			listener.onReceivedAd();
			return;
		}

		Log.d(Tag, "Caching interstitial ad");
		cb.cacheInterstitial();
	}

	@Override
	public void showInterstitial() {
		Log.d(Tag, "Showing previously loaded interstitial ad");
		Intent intent = new Intent(activity, ChartBoostInterstitialActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void destroy() {
		mainDelegate.unsetAdapterDelegate();
	}

}
