package com.doublechen.peoplecountfortravel.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtils {
	private static DisplayMetrics dm = new DisplayMetrics();

	/**
	 * ��ȡ��Ļ��ȡ���px
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidthInPx(Activity activity) {
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * ��ȡ��Ļ�߶ȡ���px
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeightInPx(Activity activity) {
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * dpתΪpx
	 */
	public static int dp2px(float dp, Resources res) {
		return (int) (dp * res.getDisplayMetrics().density + 0.5f);
	}

	/**
	 * pxתΪdp
	 */
	public static int px2dp(float px, Resources res) {
		return (int) (px / res.getDisplayMetrics().density + 0.5f);
	}
}
