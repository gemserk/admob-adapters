package com.gemserk.google.ads.mediation.tapjoy;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyDisplayAdNotifier;

public class TapJoyCustomEventBanner implements CustomEventBanner {

	private static final String TapJoyBannerTag = "TapJoyBanner";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestBannerAd(final CustomEventBannerListener listener, Activity activity, String label, String serverParameter, AdSize size, MediationAdRequest mediationAdRequest, Object customEventExtra) {

		Log.i(TapJoyBannerTag, "Received a banner ad request for " + label);

		TapjoyConnect.getTapjoyConnectInstance().setDisplayAdSize(size.getWidth() + "x" + size.getHeight());
		TapjoyConnect.getTapjoyConnectInstance().getDisplayAdWithCurrencyID(serverParameter,new TapjoyDisplayAdNotifier() {
			
			@Override
			public void getDisplayAdResponseFailed(String paramString) {
				Log.i(TapJoyBannerTag, "Banner Response Failed - " + paramString);
				listener.onFailedToReceiveAd();
			}
			
			@Override
			public void getDisplayAdResponse(View view) {
				Log.i(TapJoyBannerTag, "Banner Response succeded");
				listener.onReceivedAd(view);
			}
		}); 
	}

}
