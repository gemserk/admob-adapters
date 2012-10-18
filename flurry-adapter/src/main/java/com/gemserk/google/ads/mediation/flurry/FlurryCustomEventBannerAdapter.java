package com.gemserk.google.ads.mediation.flurry;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class FlurryCustomEventBannerAdapter implements CustomEventBanner {

	@Override
	public void requestBannerAd(final CustomEventBannerListener listener, final Activity activity, String label, String serverParameter, //
			AdSize size, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		FlurryAgent.initializeAds(activity);
		FlurryAgent.enableTestAds(mediationAdRequest.isTesting());

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = (int) (320 * metrics.density);
		int height = (int) (50 * metrics.density);

		String adSpace = serverParameter;

		FrameLayout.LayoutParams wrappedLayoutParams = new FrameLayout.LayoutParams(width, height);
		
		FrameLayout wrappedView = new FrameLayout(activity);
		wrappedView.setLayoutParams(wrappedLayoutParams);

		FlurryAdView adView = new FlurryAdView(activity, adSpace);

		wrappedView.addView(adView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

		adView.loadNewAd(10000L, listener, wrappedView);
	}

	@Override
	public void destroy() {
	}
}
