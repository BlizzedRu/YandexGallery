package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.utils.FileUtils;
import ru.blizzed.yandexgallery.utils.GlideUtils;

public class FullScreenURLImageDialogFragment extends FullScreenImageDialogFragment<URLImage> {

    private Disposable favoriteDisposable;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFavoriteState(false);
    }

    @Override
    protected ImageLoader<URLImage> provideImageLoader() {
        return ImageLoader.URL_IMAGE_FULL;
    }

    @Override
    protected int getDownMenuRes() {
        return R.menu.image_url_actions;
    }

    @Override
    public void onMenuItemClicked(MenuItem item, int position) {
        switch (item.getItemId()) {
            case R.id.share:
                onShare();
                break;
            case R.id.favorite:
                boolean behavior = !getCurrentImage().isFavorite();
                if (behavior) addToFavorite();
                else removeFromFavorite();
                break;
            case R.id.save:
                if (hasPermission(PERMISSION)) saveFile();
                else {
                    if (canRequestPermissions(PERMISSION)) requestPermission(PERMISSION);
                    else
                        createPermissionSettingsSnackbar(R.string.image_menu_save_error_permissions).show();
                }
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        updateFavoriteState(false);
    }

    private void updateFavoriteState(boolean animate) {
        downMenu.setActive(downMenu.getPositionOf(R.id.favorite), getCurrentImage().isFavorite(), animate);
    }

    private void addToFavorite() {
        onFavoriteChange(true);
        favoriteDisposable = App.getRepositoriesComponent()
                .favoriteImagesRepository()
                .add(getCurrentImage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, e -> onFavoriteError(e, true));
        addDisposable(favoriteDisposable);
    }

    private void removeFromFavorite() {
        onFavoriteChange(false);
        favoriteDisposable = App.getRepositoriesComponent()
                .favoriteImagesRepository()
                .remove(getCurrentImage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, e -> onFavoriteError(e, false));
        addDisposable(favoriteDisposable);
    }

    private void onFavoriteChange(boolean behavior) {
        dispose(favoriteDisposable);
        getCurrentImage().setFavorite(behavior);
        updateFavoriteState(true);
    }

    private void onFavoriteError(Throwable e, boolean behavior) {
        onFavoriteChange(!behavior);
        Log.e("ru.blizzed.yandex", e.toString());
    }

    private void onShare() {
        Maybe<File> shareMaybe = Maybe.create(emitter -> {
            emitter.onSuccess(GlideUtils.getFileFromImageURL(getActivity(), getCurrentImage().getLargeURL()));
        });

        addDisposable(shareMaybe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onShareSuccess, this::onShareError)
        );
    }

    private void onShareSuccess(File file) {
        startActivity(getShareIntent(file.getAbsolutePath()));
    }

    private void onShareError(Throwable e) {
        createSnackbar(R.string.image_menu_share_error).show();
        Log.e("ru.blizzed.yandex", e.toString());
    }

    private void saveFile() {
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        addDisposable(Completable
                .fromAction(() -> {
                    File cachedImageFile = GlideUtils.getFileFromImageURL(getActivity(), getCurrentImage().getLargeURL());
                    File imageFile = new File(String.format("%s/%s.jpg", downloads.getAbsolutePath(), getCurrentImage().getId()));
                    FileUtils.copy(cachedImageFile, imageFile);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSaveSuccess, this::onSaveError)
        );
    }

    private void onSaveSuccess() {
        createSnackbar(R.string.image_menu_save_success).show();
    }

    private void onSaveError(Throwable e) {
        createSnackbar(R.string.image_menu_save_error).show();
        Log.e("ru.blizzed.yandex", e.toString());
    }

    private void dispose(Disposable disposable) {
        if (disposable != null) {
            removeDisposable(disposable);
            disposable.dispose();
        }
    }

}
