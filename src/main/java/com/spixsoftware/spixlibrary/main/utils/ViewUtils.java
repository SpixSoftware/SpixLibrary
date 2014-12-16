package com.spixsoftware.spixlibrary.main.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(View view, Drawable drawable) {
		if (android.os.Build.VERSION.SDK_INT < 16) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}

    /**
     * If view id is <0 that  View.findViewById() will not find it. This method will
     */
	public static View findViewById(ViewGroup viewGroup, int idOfViewToFind) {
		final int childsCount = viewGroup.getChildCount();
		for (int i = 0; i < childsCount; i++) {
			View childView = viewGroup.getChildAt(i);
			if (childView.getId() == idOfViewToFind) {
				return childView;
			}
		}
		return null;
	}

	public static void removeViewFromParent(View view) {
		if (view == null) {
			return;
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
	}

}
