package com.gemserk.google.ads.mediation.adcolony;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class AdColonyCustomEventInterstitial implements CustomEventInterstitial {

	private static final String Tag = "RevmobInterstitialAdapter";

		@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(Tag, "Ad request received with parameters " + serverParameter);
	}

	@Override
	public void showInterstitial() {

	}

	@Override
	public void destroy() {
		
	}

}
