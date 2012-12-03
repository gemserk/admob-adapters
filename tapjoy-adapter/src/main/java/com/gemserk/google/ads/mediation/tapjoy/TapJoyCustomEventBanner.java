package com.gemserk.google.ads.mediation.tapjoy;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyDisplayAdNotifier;
import com.tapjoy.TapjoyDisplayAdSize;

public class TapJoyCustomEventBanner implements CustomEventBanner {

	private static final String TAG = "TapJoyCustomEventBanner";
	private int tapJoyAdScale;

	@Override
	public void destroy() {
		Log.d(TAG, "destroy()");
	}

	@Override
	public void requestBannerAd(final CustomEventBannerListener listener, final Activity activity, String label, String serverParameter, final AdSize size, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.i(TAG, "Received a banner ad request for " + label + " with size " + size);

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = (int) (size.getWidth() * metrics.density);

		int tapJoyAdWidth = 320;

		String tapJoyAdSize = TapjoyDisplayAdSize.TJC_DISPLAY_AD_SIZE_320X50;
		if (width > 320) {
			tapJoyAdSize = TapjoyDisplayAdSize.TJC_DISPLAY_AD_SIZE_640X100;
			tapJoyAdWidth = 640;
		}

		tapJoyAdScale = Math.round(100 * ((float) width) / ((float) tapJoyAdWidth));

		Log.i(TAG, "Ad width:" + width + ", tapJoyAdWidth: " + tapJoyAdWidth + ", scale: " + tapJoyAdScale);

		TapjoyConnect tapjoy = TapjoyConnect.getTapjoyConnectInstance();

		tapjoy.enableDisplayAdAutoRefresh(false);

		tapjoy.setDisplayAdSize(tapJoyAdSize);
		tapjoy.getDisplayAdWithCurrencyID(serverParameter, new TapjoyDisplayAdNotifier() {

			@Override
			public void getDisplayAdResponseFailed(String paramString) {
				Log.i(TAG, "Banner Response Failed - " + paramString);
				listener.onFailedToReceiveAd();
			}

			@Override
			public void getDisplayAdResponse(View adView) {
				Log.i(TAG, "Banner Response succeded");

				((WebView) adView).setInitialScale(tapJoyAdScale);

				listener.onReceivedAd(adView);
			}
		});
	}

}
