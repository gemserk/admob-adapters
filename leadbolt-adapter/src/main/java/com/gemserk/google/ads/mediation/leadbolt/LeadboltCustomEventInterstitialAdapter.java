package com.gemserk.google.ads.mediation.leadbolt;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.pad.android.iappad.AdController;
import com.pad.android.listener.AdListener;

public class LeadboltCustomEventInterstitialAdapter implements CustomEventInterstitial {

	private static final String LeadBoltInterstitialAdapterTag = "LeadBoltInterstitialAdmobAdapter";

	class LeadboltAdListener implements AdListener {

		private final CustomEventInterstitialListener listener;

		public LeadboltAdListener(CustomEventInterstitialListener listener) {
			this.listener = listener;
		}

		@Override
		public void onAdResumed() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdResumed");
		}

		@Override
		public void onAdProgress() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdProgress");
		}

		@Override
		public void onAdPaused() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdPaused");
		}

		@Override
		public void onAdLoaded() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdLoaded");
			listener.onReceivedAd();
			leadboltAdController.pauseAd();
		}

		@Override
		public void onAdHidden() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdHidden");
		}

		@Override
		public void onAdFailed() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdFailed");
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onAdCompleted() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdCompleted");
		}

		@Override
		public void onAdClosed() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdClosed");
			listener.onDismissScreen();
		}

		@Override
		public void onAdClicked() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdClicked");
			listener.onLeaveApplication();
			// listener.onClick();
		}

		@Override
		public void onAdAlreadyCompleted() {
			Log.d(LeadBoltInterstitialAdapterTag, "onAdAlreadyCompleted");
		}
	}
	
	private AdController leadboltAdController;

	@Override
	public void destroy() {
		leadboltAdController.destroyAd();
	}

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, //
			String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(LeadBoltInterstitialAdapterTag, "Received LeadBolt custom event with parameters : " + serverParameter);
		
		String adId = serverParameter;
		
		leadboltAdController = new AdController(activity, adId, new LeadboltAdListener(listener));
		leadboltAdController.loadAd();
	}

	@Override
	public void showInterstitial() {
		leadboltAdController.resumeAd();
	}

}
