package com.gemserk.google.ads.mediation.mobclix;

import android.app.Activity;
import android.util.Log;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.mobclix.android.sdk.MobclixAdView;
import com.mobclix.android.sdk.MobclixAdViewListener;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;

public class MobclixCustomEventBanner implements CustomEventBanner {

	private static final String MobclixAdapterTag = "MobclixCustomEventBanner";

	public static class MobclixBannerListener implements MobclixAdViewListener {
		
		private final CustomEventBannerListener listener;
		private final String keywords;

		public MobclixBannerListener(CustomEventBannerListener listener, String keywords) {
			this.listener = listener;
			this.keywords = keywords;
		}

		@Override
		public void onSuccessfulLoad(MobclixAdView adView) {
			listener.onReceivedAd(adView);
			adView.pause();
		}

		@Override
		public void onFailedLoad(MobclixAdView adView, int paramInt) {
			listener.onFailedToReceiveAd();
			adView.pause();
		}

		@Override
		public void onAdClick(MobclixAdView paramMobclixAdView) {
			listener.onClick();
		}

		@Override
		public boolean onOpenAllocationLoad(MobclixAdView adView, int paramInt) {
			Log.d(MobclixAdapterTag, "Received OpenAllocationLoad: " + paramInt);
			listener.onFailedToReceiveAd();
			adView.pause();
			return true;
		}

		@Override
		public void onCustomAdTouchThrough(MobclixAdView paramMobclixAdView, String paramString) {
			Log.d(MobclixAdapterTag, "Received CustomAdTouchThrough: " + paramString);
		}

		@Override
		public String keywords() {
			return keywords;
		}

		@Override
		public String query() {
			return null;
		}
		
	}
	
	
	
	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest) {
		Log.d(MobclixAdapterTag, "Received Mobclix custom event with parameters : " + serverParameter);
		
		try {
			String keywords = MobclixCustomEventInterstitial.convertKeywords(mediationAdRequest.getKeywords());
			
			Log.d(MobclixAdapterTag, "Using keywords from mediationAdRequest: " + keywords);
			
			MobclixMMABannerXLAdView adView = new MobclixMMABannerXLAdView(activity);
			adView.addMobclixAdViewListener(new MobclixBannerListener(listener, keywords));

			adView.getAd();
			
		} catch (Exception e) {
			Log.d(MobclixAdapterTag, "Failed to process mobclix banner : " + e.getMessage());
			listener.onFailedToReceiveAd();
		}
	}

}
