package com.gemserk.google.ads.mediation;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;
import com.playhaven.src.common.PHAPIRequest;
import com.playhaven.src.publishersdk.content.PHContent;
import com.playhaven.src.publishersdk.content.PHPublisherContentRequest;
import com.playhaven.src.publishersdk.content.PHPublisherContentRequest.ContentDelegate;
import com.playhaven.src.publishersdk.content.PHPublisherContentRequest.FailureDelegate;
import com.playhaven.src.publishersdk.content.PHPublisherContentRequest.PHDismissType;

public class PlayHavenCustomEventInterstitial implements CustomEventInterstitial{

	static final String PlayHavenCustomEventTag = "PlayHavenCustomEvent";
	private PHAPIRequest request;
	
	

	@Override
	public void requestInterstitialAd(final CustomEventInterstitialListener listener, Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest, Object customEventExtra) {
		Log.i(PlayHavenCustomEventTag, "Received an interstitial ad request for " + label);
		PHPublisherContentRequest request = new PHPublisherContentRequest(activity, serverParameter+"test");
		
		
		request.setOnContentListener(new ContentDelegate() {
			
			@Override
			public void requestSucceeded(PHAPIRequest request, JSONObject responseData) {
				Log.i(PlayHavenCustomEventTag, "requestSucceeded");
				listener.onReceivedAd();
			}
			
			@Override
			public void requestFailed(PHAPIRequest request, Exception e) {
				Log.i(PlayHavenCustomEventTag, "requestFailed");
				listener.onFailedToReceiveAd();
			}
			
			@Override
			public void willGetContent(PHPublisherContentRequest request) {
				Log.i(PlayHavenCustomEventTag, "willGetContent");
			}
			
			@Override
			public void willDisplayContent(PHPublisherContentRequest request, PHContent content) {
				Log.i(PlayHavenCustomEventTag, "willDisplayContent");
				listener.onPresentScreen();
			}
			
			@Override
			public void didDisplayContent(PHPublisherContentRequest request, PHContent content) {
				Log.i(PlayHavenCustomEventTag, "didDisplayContent");
			}
			
			@Override
			public void didDismissContent(PHPublisherContentRequest request, PHDismissType type) {
				Log.i(PlayHavenCustomEventTag, "didDismissContent");
				listener.onDismissScreen();
			}
		});
		
		request.setOnFailureListener(new FailureDelegate() {
			
			@Override
			public void didFail(PHPublisherContentRequest request, String error) {
				Log.i(PlayHavenCustomEventTag, "didFail");
				listener.onFailedToReceiveAd();
				
			}
			
			@Override
			public void contentDidFail(PHPublisherContentRequest request, Exception e) {
				Log.i(PlayHavenCustomEventTag, "contentDidFail");
				listener.onFailedToReceiveAd();
				
			}
		});
		request.preload();

	}

	@Override
	public void showInterstitial() {
		request.send();
	}

	@Override
	public void destroy() {
	}
}
