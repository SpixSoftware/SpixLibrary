package com.spixsoftware.spixlibrary.main.utils;

import android.media.ExifInterface;

public class ExifInterfaceUtils {

    public static void copyExifAttribute(ExifInterface source, ExifInterface target, String tag) {
        String value = source.getAttribute(tag);
        if (value == null) {
            return;
        }
        target.setAttribute(tag, value);
    }

    public static void copyExifAttributes(ExifInterface source, ExifInterface target, String... tags) {
        for (String tag : tags) {
            copyExifAttribute(source, target, tag);
        }
    }


}
