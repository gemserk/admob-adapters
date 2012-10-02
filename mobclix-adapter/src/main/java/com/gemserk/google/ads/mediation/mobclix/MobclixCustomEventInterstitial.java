package com.gemserk.google.ads.mediation.mobclix;

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

		public MobclixInterstitialListener(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public String query() {
			return null;
		}

		@Override
		public void onPresentAd(MobclixFullScreenAdView arg0) {
			listener.onPresentScreen();
		}

		@Override
		public void onFinishLoad(MobclixFullScreenAdView arg0) {
			listener.onReceivedAd();
		}

		@Override
		public void onFailedLoad(MobclixFullScreenAdView arg0, int arg1) {
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onDismissAd(MobclixFullScreenAdView arg0) {
			listener.onDismissScreen();
		}

		@Override
		public String keywords() {
			return null;
		}
	}

	private MobclixFullScreenAdView adView;

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest) {
		Log.d(MobclixAdapterTag, "Received Mobclix custom interstitial with parameters : " + serverParameter);

		adView = new MobclixFullScreenAdView(activity);
		adView.setCreativeId(serverParameter);
		adView.addMobclixAdViewListener(new MobclixInterstitialListener(listener));

		if (adView.hasAd()) {
			Log.d(MobclixAdapterTag, "Interstitial already cached");
			listener.onReceivedAd();
			return;
		}

		Log.d(MobclixAdapterTag, "Requesting new interstitial");
		adView.requestAd();
	}

	@Override
	public void showInterstitial() {
		Log.d(MobclixAdapterTag, "Displaying cached interstitial");
		adView.displayRequestedAd();
	}

}
