package com.resc.remgauge;

import java.util.Random;

public class MathUtils {

	static float mAve = -1;
	static Random mrand = new Random();

	public MathUtils() {
		mrand = new Random();
	}

	public static int randInt(int min, int max) {
		int r = mrand.nextInt((max - min) + 1) + min;
		return r;
	}

	public static float c2f(float c) {
		return (c *9.0f / 5.0f) + 32.0f;
	}

	public static float f2c(float f) {
		return (f - 32.0f) * (5.0f/9.0f);
	}

	public static float movingAverage(float newval) {
		return mAve;
	}


}
