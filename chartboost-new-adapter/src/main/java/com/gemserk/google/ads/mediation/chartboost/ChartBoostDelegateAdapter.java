package com.gemserk.google.ads.mediation.chartboost;

import com.chartboost.sdk.ChartboostDelegate;

public class ChartBoostDelegateAdapter implements ChartboostDelegate {

	@Override
	public boolean shouldRequestInterstitial(String paramString) {
		return true;
	}

	@Override
	public boolean shouldDisplayInterstitial(String paramString) {
		return true;
	}

	@Override
	public void didCacheInterstitial(String paramString) {
		
	}

	@Override
	public void didFailToLoadInterstitial(String paramString) {
		
	}

	@Override
	public void didDismissInterstitial(String paramString) {
		
	}

	@Override
	public void didCloseInterstitial(String paramString) {
		
	}

	@Override
	public void didClickInterstitial(String paramString) {
		
	}

	@Override
	public void didShowInterstitial(String paramString) {
		
	}

	@Override
	public boolean shouldDisplayLoadingViewForMoreApps() {
		return true;
		
	}

	@Override
	public boolean shouldRequestMoreApps() {
		return true;
	}

	@Override
	public void didCacheMoreApps() {
		
	}

	@Override
	public boolean shouldDisplayMoreApps() {
		return true;
	}

	@Override
	public void didFailToLoadMoreApps() {
		
	}

	@Override
	public void didDismissMoreApps() {
		
	}

	@Override
	public void didCloseMoreApps() {
		
	}

	@Override
	public void didClickMoreApps() {
		
	}

	@Override
	public void didShowMoreApps() {
		
	}

	@Override
	public boolean shouldRequestInterstitialsInFirstSession() {
		return true;
	}

}
