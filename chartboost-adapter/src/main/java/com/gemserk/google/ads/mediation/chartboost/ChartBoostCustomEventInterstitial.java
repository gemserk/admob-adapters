package com.gemserk.google.ads.mediation.chartboost;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.chartboost.sdk.ChartBoost;
import com.chartboost.sdk.ChartBoostDelegate;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class ChartBoostCustomEventInterstitial implements CustomEventInterstitial {

	private static final String ChartBoostCustomEventTag = "ChartBoostCustomEvent";
	private ChartBoost cb;

	public static boolean shouldInstall = true;

	private class InternalChartBoostDelegate extends ChartBoostDelegate {

		private final int cacheCheckThreadSleepTime = 100;
		private final int cacheCheckThreadSleepTimeout = 10000;

		CustomEventInterstitialListener listener;
		Thread thread;

		public InternalChartBoostDelegate(CustomEventInterstitialListener listener) {
			this.listener = listener;
			this.thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int time = cacheCheckThreadSleepTimeout;
						while (time > 0) {
							Thread.sleep(cacheCheckThreadSleepTime);
							time -= cacheCheckThreadSleepTime;
							if (cb.hasCachedInterstitial()) {
								Log.d(ChartBoostCustomEventTag, "Interstitial ad ready");
								InternalChartBoostDelegate.this.listener.onReceivedAd();
								return;
							}
						}
						Log.d(ChartBoostCustomEventTag, "Cached interstitial ad request timeout");
						InternalChartBoostDelegate.this.listener.onFailedToReceiveAd();
					} catch (InterruptedException e) {

					}
				}
			});
			thread.start();
		}

		@Override
		public void didFailToLoadInterstitial() {
			super.didFailToLoadInterstitial();
			listener.onFailedToReceiveAd();
			thread.interrupt();
		}

		@Override
		public void didDismissInterstitial(View interstitialView) {
			super.didDismissInterstitial(interstitialView);
			listener.onDismissScreen();
		}

		@Override
		public void didCloseInterstitial(View interstitialView) {
			super.didCloseInterstitial(interstitialView);
			listener.onDismissScreen();
		}

		@Override
		public void didClickInterstitial(View interstitialView) {
			super.didClickInterstitial(interstitialView);
			listener.onLeaveApplication();
		}

	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(ChartBoostCustomEventTag, "Received an interstitial ad request for " + label);

		cb = ChartBoost.getSharedChartBoost(activity);
		cb.setDelegate(new InternalChartBoostDelegate(listener));

		String[] parameters = serverParameter.split(",");
		if (parameters.length != 2) {
			Log.d(ChartBoostCustomEventTag, "Invalid parameter " + serverParameter + ", needed \"appId,appSignature\"");
			return;
		}

		String appId = parameters[0];
		String appSignature = parameters[1];

		Log.d(ChartBoostCustomEventTag, "Received custom event with appId:" + appId + ", appSignature:" + appSignature);

		if (ChartBoostCustomEventInterstitial.shouldInstall || !appId.equals(cb.getAppId()) || !appSignature.equals(cb.getAppSignature())) {
			Log.d(ChartBoostCustomEventTag, "Installing chartboost with appId:" + appId + ", appSignature:" + appSignature);
			cb.setAppId(appId);
			cb.setAppSignature(appSignature);
			
			cb.clearCache();
			cb.install();
			ChartBoostCustomEventInterstitial.shouldInstall = false;
		}

		if (cb.hasCachedInterstitial()) {
			Log.d(ChartBoostCustomEventTag, "Interstitial already cached");
			listener.onReceivedAd();
			return;
		}

		Log.d(ChartBoostCustomEventTag, "Caching interstitial ad");
		cb.cacheInterstitial();
	}

	@Override
	public void showInterstitial() {
		Log.d(ChartBoostCustomEventTag, "Showing previously loaded interstitial ad");
		cb.showInterstitial();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
