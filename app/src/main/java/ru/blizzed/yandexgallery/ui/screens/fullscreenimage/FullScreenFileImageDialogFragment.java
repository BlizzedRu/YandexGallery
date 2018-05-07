package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.files.folder.OnFileImageRemovedListener;

public class FullScreenFileImageDialogFragment extends FullScreenImageDialogFragment<FileImage> {

    private OnFileImageRemovedListener onFileRemovedListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof OnFileImageRemovedListener) {
            onFileRemovedListener = (OnFileImageRemovedListener) parentFragment;
        }
    }

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
                    else
                        createPermissionSettingsSnackbar(R.string.image_menu_delete_error_permissions).show();
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

    private void removeFile() {
        addDisposable(Completable
                .create(emitter -> {
                    boolean deleted = getCurrentImage().getFile().delete();
                    if (deleted) emitter.onComplete();
                    else emitter.onError(new IOException());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    int removingPosition = position;

                    if (onFileRemovedListener != null)
                        onFileRemovedListener.onImageRemoved(images.get(removingPosition));

                    images.remove(removingPosition);
                    adapter.notifyDataSetChanged();

                    if (images.size() == 0) {
                        dismiss();
                        return;
                    }

                    updateToolbarTitle();
                    createSnackbar(R.string.image_menu_delete_success).show();
                }, error -> {
                    createSnackbar(R.string.image_menu_delete_error).show();
                })
        );
    }

}
