package com.doublechen.peoplecountfortravel.utils;

import android.content.Context;

public class MSharedPreference {
	private static final String PREFERENCE_NAME = MConstants.APP_NAME;

	public static void save(Context context, String key, boolean value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
	}

	public static boolean get(Context context, String key, boolean defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}

	public static void save(Context context, String key, int value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
	}

	public static int get(Context context, String key, int defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}

	public static void save(Context context, String key, long value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
	}

	public static long get(Context context, String key, long defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(key, defaultValue);
	}

	public static void save(Context context, String key, float value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
	}

	public static float get(Context context, String key, float defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(key, defaultValue);
	}

	public static void save(Context context, String key, String value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}

	public static String get(Context context, String key, String defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
	}
}
