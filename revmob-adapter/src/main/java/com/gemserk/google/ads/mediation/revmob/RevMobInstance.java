package com.gemserk.google.ads.mediation.revmob;

import android.app.Activity;

import com.revmob.RevMob;

public class RevMobInstance {
	
	private static RevMob revMob;
	
	public static RevMob getInstance(Activity activity, String applicationId) {
		if (revMob == null)
			 revMob = RevMob.start(activity, applicationId);
		return revMob;
	}

	public static void close() {
		revMob = null;
	}

}
