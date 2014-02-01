package com.gemserk.google.ads.mediation.leadbolt;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.pad.android.iappad.AdController;
import com.pad.android.listener.AdListener;

public class LeadboltCustomEventBannerAdapter implements CustomEventBanner {

	private static final String LeadBoltAdapterTag = "LeadBoltAdmobAdapter";

	private class LeadboltAdListener implements AdListener {

		private final CustomEventBannerListener listener;
		private final ViewGroup view;
		private View childView;

		public LeadboltAdListener(CustomEventBannerListener listener, ViewGroup view) {
			this.listener = listener;
			this.view = view;
		}

		@Override
		public void onAdResumed() {
			Log.d(LeadBoltAdapterTag, "onAdResumed");
		}

		@Override
		public void onAdProgress() {
			Log.d(LeadBoltAdapterTag, "onAdProgress");
		}

		@Override
		public void onAdPaused() {
			Log.d(LeadBoltAdapterTag, "onAdPaused");
		}

		@Override
		public void onAdLoaded() {
			Log.d(LeadBoltAdapterTag, "onAdLoaded");
			childView = view.getChildAt(0);
			view.removeAllViews();
			listener.onReceivedAd(childView);
		}

		@Override
		public void onAdHidden() {
			Log.d(LeadBoltAdapterTag, "onAdHidden");
		}

		@Override
		public void onAdFailed() {
			Log.d(LeadBoltAdapterTag, "onAdFailed");
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onAdCompleted() {
			Log.d(LeadBoltAdapterTag, "onAdCompleted");
		}

		@Override
		public void onAdClosed() {
			Log.d(LeadBoltAdapterTag, "onAdClosed");
			listener.onDismissScreen();
		}

		@Override
		public void onAdClicked() {
			Log.d(LeadBoltAdapterTag, "onAdClicked");
			listener.onClick();
		}

		@Override
		public void onAdAlreadyCompleted() {
			Log.d(LeadBoltAdapterTag, "onAdAlreadyCompleted");
		}
	}

	@Override
	public void requestBannerAd(CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(LeadBoltAdapterTag, "Received LeadBolt custom event with parameters : " + serverParameter);

		// DisplayMetrics metrics = new DisplayMetrics();
		// activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//
		// int width = (int) (320 * metrics.density);
		// int height = (int) (50 * metrics.density);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, //
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout relativeLayout = new RelativeLayout(activity);
		relativeLayout.setLayoutParams(layoutParams);

		String adId = serverParameter;

		AdController leadboltAdController = new AdController(activity, adId, new LeadboltAdListener(listener, relativeLayout));
		leadboltAdController.setLayout(relativeLayout);
		// leadboltAdController.setAsynchTask(false);
		leadboltAdController.loadAd();
		
		Log.d(LeadBoltAdapterTag, "requestBannerAd method finished");
	}

	@Override
	public void destroy() {
		
	}

}
