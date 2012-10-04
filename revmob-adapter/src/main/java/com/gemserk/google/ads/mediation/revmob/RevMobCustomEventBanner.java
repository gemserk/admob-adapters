package com.gemserk.google.ads.mediation.revmob;

import android.app.Activity;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class RevMobCustomEventBanner implements CustomEventBanner {

	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest) {
		
	}

}
