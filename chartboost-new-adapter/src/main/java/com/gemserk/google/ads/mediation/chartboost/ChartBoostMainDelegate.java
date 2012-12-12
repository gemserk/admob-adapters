package com.gemserk.google.ads.mediation.chartboost;

import com.chartboost.sdk.ChartboostDelegate;

public class ChartBoostMainDelegate extends ChartBoostDelegateAdapter {

	private ChartboostDelegate activityDelegate = new ChartBoostDelegateAdapter();
	private ChartboostDelegate adapterDelegate = new ChartBoostDelegateAdapter();
	
	public void setActivityDelegate(ChartboostDelegate activityDelegate) {
		this.activityDelegate = activityDelegate;
	}
	
	public void unsetActivityDelegate() {
		 activityDelegate = new ChartBoostDelegateAdapter();
	}
	
	public void setAdapterDelegate(ChartboostDelegate adapterDelegate) {
		this.adapterDelegate = adapterDelegate;
	}
	
	public void unsetAdapterDelegate() {
		adapterDelegate = new ChartBoostDelegateAdapter();
	}

	public void didCacheInterstitial(String paramString) {
		activityDelegate.didCacheInterstitial(paramString);
		adapterDelegate.didCacheInterstitial(paramString);
	}

	public void didFailToLoadInterstitial(String paramString) {
		activityDelegate.didFailToLoadInterstitial(paramString);
		adapterDelegate.didFailToLoadInterstitial(paramString);
	}

	public void didDismissInterstitial(String paramString) {
		activityDelegate.didDismissInterstitial(paramString);
		adapterDelegate.didDismissInterstitial(paramString);
	}

	public void didCloseInterstitial(String paramString) {
		activityDelegate.didCloseInterstitial(paramString);
		adapterDelegate.didCloseInterstitial(paramString);
	}

	public void didClickInterstitial(String paramString) {
		activityDelegate.didClickInterstitial(paramString);
		adapterDelegate.didClickInterstitial(paramString);
	}

	public void didShowInterstitial(String paramString) {
		activityDelegate.didShowInterstitial(paramString);
		adapterDelegate.didShowInterstitial(paramString);
	}

}