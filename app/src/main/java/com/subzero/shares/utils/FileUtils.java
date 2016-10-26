package com.subzero.shares.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {
	public final static String CACHE = "cache";
	public final static String ICON = "icon";
	public final static String ROOT = "Gupiao";


	public static File getDir(String dir, Context context) {
		StringBuilder mStringBuilder = new StringBuilder();
		if (isSDCardAvailable()) {

			mStringBuilder.append(Environment.getExternalStorageDirectory());
			mStringBuilder.append(File.separator);
			mStringBuilder.append(ROOT);
			mStringBuilder.append(File.separator);
			mStringBuilder.append(dir);
		} else {
			mStringBuilder.append(context.getCacheDir().getAbsoluteFile());
			mStringBuilder.append(File.separator);
			mStringBuilder.append(dir);
		}

		File file = new File(mStringBuilder.toString());

		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return file;
	}


	private static boolean isSDCardAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 获取缓存目录
	 * @param context
	 * @return
	 */
	public static File getCacheDir(Context context) {

		return getDir(CACHE, context);

	}

	/**
	 * 获取本地图片目录
	 * @param context
	 * @return
	 */
	public static File getIconDir(Context context) {
		File file = getDir(ICON, context);
		return file;
	}
}
