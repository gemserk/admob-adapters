package com.gemserk.google.ads.mediation.flurry;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAgent;
import com.flurry.android.IListener;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class FlurryAdView extends RelativeLayout {

	private static final int SHOW = 1;

	private class FlurryAdListener implements IListener {

		private CustomEventBannerListener listener;

		public FlurryAdListener(CustomEventBannerListener listener) {
			this.listener = listener;
		}

		@Override
		public void onAdClosed(String arg0) {
			Log.d(FlurryLogTag.Tag, "On ad closed " + arg0);
			listener.onDismissScreen();
		}

		@Override
		public void onApplicationExit(String arg0) {
			Log.d(FlurryLogTag.Tag, "On application exit " + arg0);
			listener.onLeaveApplication();
		}

		@Override
		public void onRenderFailed(String arg0) {
			Log.d(FlurryLogTag.Tag, "On render failed " + arg0);
			listener.onFailedToReceiveAd();
		}

		@Override
		public void onReward(String arg0, Map<String, String> arg1) {
			Log.d(FlurryLogTag.Tag, "On reward " + arg0);
		}

		@Override
		public boolean shouldDisplayAd(String arg0, FlurryAdType flurryAdType) {
			return flurryAdType == FlurryAdType.WEB_BANNER;
		}

	}

	private class FlurryAdHandler extends Handler {

		private CustomEventBannerListener listener;
		private ViewGroup view;

		public FlurryAdHandler(CustomEventBannerListener listener, ViewGroup view) {
			this.listener = listener;
			this.view = view;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SHOW:
				Log.d(FlurryLogTag.Tag, "Starting getAd() assuming it is already cached");
				FlurryAgent.getAd(getContext(), adSpace, view, FlurryAdSize.BANNER_TOP, 10000L);
				Log.d(FlurryLogTag.Tag, "Ad should be ready");
				listener.onReceivedAd(view);
				break;
			default:
				break;
			}
		}

	}

	private class FlurryAdRequestRunnable implements Runnable {

		private CustomEventBannerListener listener;
		private long timeout;
		// private ViewGroup view;
		private Handler handler;

		private FlurryAdRequestRunnable(CustomEventBannerListener listener, long timeout, Handler handler) {
			this.listener = listener;
			this.timeout = timeout;
			this.handler = handler;
		}

		@Override
		public void run() {
			// Looper.prepare();
			Log.d(FlurryLogTag.Tag, "Starting ad request");
			FlurryAgent.setAdListener(new FlurryAdListener(listener));
			if (FlurryAgent.isAdAvailable(getContext(), adSpace, FlurryAdSize.BANNER_TOP, timeout)) {
				// if (FlurryAgent.getAd(getContext(), adSpace, view, FlurryAdSize.BANNER_TOP, timeout)) {
				Log.d(FlurryLogTag.Tag, "Ad available, sending message to handler in the proper thread");
				handler.sendEmptyMessage(SHOW);
			} else {
				Log.d(FlurryLogTag.Tag, "On ad failed");
				listener.onFailedToReceiveAd();
			}
		}
	}

	private String adSpace;
	private Thread thread;

	public FlurryAdView(Context context, String adSpace) {
		super(context);
		this.adSpace = adSpace;
	}

	public void loadNewAd(final long timeout, final CustomEventBannerListener listener, ViewGroup view) {
		stopPreviousThread();
		thread = new Thread(new FlurryAdRequestRunnable(listener, timeout, new FlurryAdHandler(listener, view)));
		thread.start();
	}

	private void stopPreviousThread() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	@Override
	public void setVisibility(final int visibility) {
		super.setVisibility(visibility);
		if (visibility != VISIBLE) {
			FlurryAgent.removeAd(getContext(), adSpace, this);
		}
	}

}