package com.gemserk.google.ads.mediation.mobclix;

import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;

public class MobclixCustomEventInterstitialTest {

	@Test
	public void testConvertNullSet() {
		assertThat(MobclixCustomEventInterstitial.convertKeywords(null), IsNull.nullValue());
	}

	@Test
	public void testConvertEmptySet() {
		assertThat(MobclixCustomEventInterstitial.convertKeywords(new HashSet<String>()), IsNull.nullValue());
	}
	
	@Test
	public void testConvertSetOfKeywordsWithOneElement() {
		Set<String> keywords = new HashSet<String>();
		keywords.add("pipote");
		assertThat(MobclixCustomEventInterstitial.convertKeywords(keywords), IsEqual.equalTo("pipote"));
	}
	
	@Test
	public void testConvertSetOfKeywordsWithMoreThanOneElement() {
		Set<String> keywords = new LinkedHashSet<String>();
		keywords.add("gangam");
		keywords.add("style");
		keywords.add("pipote");
		assertThat(MobclixCustomEventInterstitial.convertKeywords(keywords), IsEqual.equalTo("gangam,style,pipote"));
	}

}
