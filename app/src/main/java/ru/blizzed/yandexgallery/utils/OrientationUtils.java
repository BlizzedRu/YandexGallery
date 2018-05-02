package ru.blizzed.yandexgallery.utils;

import android.content.Context;
import android.content.res.Configuration;

public class OrientationUtils {

    public static Orientation get(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

}
