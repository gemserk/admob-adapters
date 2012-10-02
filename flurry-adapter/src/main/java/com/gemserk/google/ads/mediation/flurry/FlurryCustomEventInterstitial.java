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

	private Activity activity;
	private String adSpace;

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
		this.activity = activity;
		
		FlurryAgent.initializeAds(activity);
		FlurryAgent.enableTestAds(mediationAdRequest.isTesting());

		adSpace = serverParameter;
		
		FlurryAgent.setAdListener(new FlurryInterstitialAdListener(listener));
		
		long timeout = 10000L;
		boolean adAvailable = FlurryAgent.isAdAvailable(activity, adSpace, FlurryAdSize.FULLSCREEN, timeout);
		
		if (!adAvailable) {
			listener.onFailedToReceiveAd();
			return;
		}
		
		listener.onReceivedAd();
	}

	@Override
	public void showInterstitial() {
		FlurryAgent.getAd(activity, adSpace, new RelativeLayout(activity), FlurryAdSize.FULLSCREEN, 0L);
	}

}
