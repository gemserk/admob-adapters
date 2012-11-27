package com.gemserk.google.ads.mediation.adcolony;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyVideoAd;
import com.jirbo.adcolony.AdColonyVideoListener;

public class AdColonyCustomEventInterstitial implements CustomEventInterstitial, AdColonyVideoListener {

	private static final String Tag = "AdColonyInterstitialAdapter";

	public static long TIMEOUT = 8000L;

	public static boolean shouldConfigure = true;

	private String appId;
	private String zoneId;

	private AdColonyVideoAd ad;

	private Thread isReadyThread;

	private CustomEventInterstitialListener listener;

	@Override
	public void requestInterstitialAd(final CustomEventInterstitialListener listener, final Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.d(Tag, "Ad request received with parameters " + serverParameter);

		String[] parameters = serverParameter.split(",");

		if (parameters.length == 0) {
			Log.d(Tag, "Wrong parameters received ");
			return;
		}

		appId = parameters[0];

		if (parameters.length >= 2)
			zoneId = parameters[1];

		if (shouldConfigure)
			AdColony.configure(activity, "1.0", appId, zoneId);

		this.listener = listener;

		ad = new AdColonyVideoAd(zoneId);

		isReadyThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long timeOut = TIMEOUT;
					while (timeOut > 0) {
						if (ad.isReady()) {
							onReceivedAd(listener, activity);
							return;
						}
						Thread.sleep(100L);
						timeOut -= 100L;
					}
					onFailedToReceiveAd(listener, activity);
				} catch (InterruptedException e) {
					Log.d(Tag, "isReady() thread interrupted");
				}
			}

		});
		isReadyThread.start();
	}

	private void onFailedToReceiveAd(final CustomEventInterstitialListener listener, Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listener.onFailedToReceiveAd();
			}
		});
	}

	private void onReceivedAd(final CustomEventInterstitialListener listener, Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listener.onReceivedAd();
			}
		});
	}

	@Override
	public void showInterstitial() {
		if (ad.isReady())
			ad.show(this);
	}

	@Override
	public void destroy() {
		if (isReadyThread != null)
			isReadyThread.interrupt();
		isReadyThread = null;
	}

	@Override
	public void onAdColonyVideoFinished() {

	}

	@Override
	public void onAdColonyVideoStarted() {
		listener.onPresentScreen();
	}

}
