package ru.blizzed.yandexgallery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class PermissionsUtils {

    public static void openPermissionAppSettings(Activity activity, int requestCode) {
        activity.startActivityForResult(getSettingsIntent(activity), requestCode);
    }

    public static void openPermissionAppSettings(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getSettingsIntent(fragment.getContext()), requestCode);
    }

    private static Intent getSettingsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        return intent;
    }

}
