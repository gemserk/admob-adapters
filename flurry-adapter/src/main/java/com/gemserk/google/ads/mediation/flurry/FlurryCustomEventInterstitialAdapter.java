package com.gemserk.google.ads.mediation.flurry;

import android.app.Activity;

import com.flurry.android.FlurryAgent;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class FlurryCustomEventInterstitialAdapter implements CustomEventInterstitial {

	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, //
			String serverParameter, MediationAdRequest mediationAdRequest) {
		FlurryAgent.initializeAds(activity);
		FlurryAgent.enableTestAds(mediationAdRequest.isTesting());

	}

	@Override
	public void showInterstitial() {

	}

}
