package com.doublechen.peoplecountfortravel.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtils {
	private static DisplayMetrics dm = new DisplayMetrics();

	/**
	 * 获取屏幕宽度——px
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidthInPx(Activity activity) {
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度——px
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeightInPx(Activity activity) {
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * dp转为px
	 */
	public static int dp2px(float dp, Resources res) {
		return (int) (dp * res.getDisplayMetrics().density + 0.5f);
	}

	/**
	 * px转为dp
	 */
	public static int px2dp(float px, Resources res) {
		return (int) (px / res.getDisplayMetrics().density + 0.5f);
	}
}
