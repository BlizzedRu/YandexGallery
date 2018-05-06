package ru.blizzed.yandexgallery.ui.screens.fullscreenimage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class FullScreenFileImageActivity extends FullScreenImageActivity<FileImage> {

    public static final String KEY_REMOVED = "removed_images";

    private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private Disposable fileRemovingDisposable;

    private ArrayList<FileImage> removedImages;

    @Override
    protected ImageLoader<FileImage> provideImageLoader() {
        return ImageLoader.FILE_IMAGE;
    }

    @Override
    protected int getDownMenuRes() {
        return R.menu.image_file_actions;
    }

    @Override
    public void onMenuItemClicked(MenuItem item, int position) {
        switch (item.getItemId()) {
            case R.id.share:
                startActivity(getShareIntent(getCurrentImage().getMediumURL()));
                break;
            case R.id.delete:
                if (hasPermission(PERMISSION)) removeFile();
                else {
                    if (canRequestPermissions(PERMISSION)) requestPermission(PERMISSION);
                    else showPermissionSettingsSnackbar();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == getPermissionRequestCode()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                removeFile();
            }
        }
    }

    @Override
    public void finish() {
        // Pull removed images to folder (prev screen) to display changes
        addToResultIntent(new Intent().putParcelableArrayListExtra(KEY_REMOVED, removedImages));
        super.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fileRemovingDisposable != null)
            fileRemovingDisposable.dispose();
    }

    private void removeFile() {
        fileRemovingDisposable = Completable
                .create(emitter -> {
                    boolean deleted = getCurrentImage().getFile().delete();
                    if (deleted) emitter.onComplete();
                    else emitter.onError(new IOException());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    int removingPosition = position;

                    if (removedImages == null) removedImages = new ArrayList<>();
                    removedImages.add(images.get(position));

                    images.remove(removingPosition);

                    if (images.size() == 0) {
                        finish();
                        return;
                    }

                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(removingPosition == 0 ? removingPosition : removingPosition - 1, true);

                    createSnackbar(R.string.image_menu_delete_success).show();
                }, error -> {
                    createSnackbar(R.string.image_menu_delete_error).show();
                });
    }

    private void showPermissionSettingsSnackbar() {
        createSnackbar(R.string.image_menu_delete_error_permissions)
                .setDuration(Snackbar.LENGTH_LONG)
                .setAction(R.string.image_menu_delete_error_permissions_button, v -> openPermissionsSettings())
                .show();
    }

}
