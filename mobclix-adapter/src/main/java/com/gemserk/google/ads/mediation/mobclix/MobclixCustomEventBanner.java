package com.gemserk.google.ads.mediation.mobclix;

import android.app.Activity;
import android.util.Log;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class MobclixCustomEventBanner implements CustomEventBanner {

	private static final String MobclixAdapterTag = "MobclixCustomEventBanner";

	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest) {
		Log.d(MobclixAdapterTag, "Received Mobclix custom event with parameters : " + serverParameter);
	}

}
