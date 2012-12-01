package com.gemserk.google.ads.mediation.tapjoy;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyFullScreenAdNotifier;

public class TapJoyCustomEventInterstitial implements CustomEventInterstitial {

	private static final String TapJoyInterstitialTag = "TapJoyInterstitial";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestInterstitialAd(final CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.i(TapJoyInterstitialTag, "Received an interstitial ad request for " + label);
		
		TapjoyFullScreenAdNotifier notifier = new TapjoyFullScreenAdNotifier() {
			
			@Override
			public void getFullScreenAdResponseFailed(int arg0) {
				Log.i(TapJoyInterstitialTag, "FullScreenAd Response Failed" + arg0);
				listener.onFailedToReceiveAd();
			}
			
			@Override
			public void getFullScreenAdResponse() {
				Log.i(TapJoyInterstitialTag, "FullScreenAd Response succeded");
				listener.onReceivedAd();
			}
		};

		TapjoyConnect.getTapjoyConnectInstance().getFullScreenAdWithCurrencyID("bedce2f7-bdd0-4991-944f-960fd15dc158", notifier);

	}

	@Override
	public void showInterstitial() {
		Log.i(TapJoyInterstitialTag, "Attempting to show Interstitial");
		TapjoyConnect.getTapjoyConnectInstance().showFullScreenAd();
	}

}
