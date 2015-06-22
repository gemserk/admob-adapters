package com.gemserk.google.ads.mediation.chartboost;

import android.app.Activity;
import android.util.Log;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Libraries.CBLogging.Level;
import com.chartboost.sdk.Model.CBError.CBImpressionError;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class ChartBoostCustomEventInterstitial implements CustomEventInterstitial {

	public static boolean shouldCreate = true;
	public static boolean useActivitiesForImpressions = true;

	private static final String ChartBoostCustomEventTag = "ChartBoostCustomEvent";

	private class InternalChartBoostDelegate extends ChartboostDelegate {

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
							if (Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT)) {
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
		public void didFailToLoadInterstitial(String location, CBImpressionError error) {
			super.didFailToLoadInterstitial(location, error);
			listener.onFailedToReceiveAd();
			thread.interrupt();
		}

		@Override
		public void didDismissInterstitial(String location) {
			super.didDismissInterstitial(location);
			listener.onDismissScreen();
		}
		
		@Override
		public void didCloseInterstitial(String location) {
			super.didCloseInterstitial(location);
			listener.onDismissScreen();
		}
		
		@Override
		public void didClickInterstitial(String location) {
			super.didClickInterstitial(location);
			listener.onLeaveApplication();
		}

	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(ChartBoostCustomEventTag, "Received an interstitial ad request for " + label);

		String[] parameters = serverParameter.split(",");
		if (parameters.length != 2) {
			Log.d(ChartBoostCustomEventTag, "Invalid parameter " + serverParameter + ", needed \"appId,appSignature\"");
			return;
		}

		String appId = parameters[0];
		String appSignature = parameters[1];

		Log.d(ChartBoostCustomEventTag, "Received custom event with appId:" + appId + ", appSignature:" + appSignature);
		
		if (ChartBoostCustomEventInterstitial.shouldCreate) {
			Chartboost.startWithAppId(activity, appId, appSignature);
			// cb.onCreate(activity, appId, appSignature, new InternalChartBoostDelegate(listener));
			Chartboost.setImpressionsUseActivities(ChartBoostCustomEventInterstitial.useActivitiesForImpressions);
			// cb.setImpressionsUseActivities(ChartBoostCustomEventInterstitial.useActivitiesForImpressions);
			Chartboost.setLoggingLevel(Level.ALL);
			Chartboost.setDelegate(new InternalChartBoostDelegate(listener));
//			cb.startSession();
			ChartBoostCustomEventInterstitial.shouldCreate = false;
		} else {
			
//			cb.setAppID(appId);
//			cb.setAppSignature(appSignature);
			Chartboost.setDelegate(new InternalChartBoostDelegate(listener));
		}
		
//		Chartboost.clearCache();

		if (Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT)) {
			Log.d(ChartBoostCustomEventTag, "Interstitial already cached");
			listener.onReceivedAd();
			return;
		}

		Log.d(ChartBoostCustomEventTag, "Caching interstitial ad");
		Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
//		cb.cacheInterstitial();
	}

	@Override
	public void showInterstitial() {
		Log.d(ChartBoostCustomEventTag, "Showing previously loaded interstitial ad");
		Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
