package ru.blizzed.yandexgallery.ui.screens.fullscreenimage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class FullScreenURLImageActivity extends FullScreenImageActivity<URLImage> {

    private Disposable currentDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                /*try {
                    File file = Glide.with(this).asFile().load(getCurrentImage().getMediumURL()).submit().get();
                    startActivity(getShareIntent(file.getAbsolutePath()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Log.e("ru.blizzed.yandex", e.toString());
                }*/
                break;
            case R.id.favorite:
                boolean behavior = !getCurrentImage().isFavorite();
                if (behavior) addToFavorite();
                else removeFromFavorite();
                break;
            case R.id.save:
                /*if (hasPermission(PERMISSION)) removeFile();
                else {
                    if (canRequestPermissions(PERMISSION)) requestPermission(PERMISSION);
                    else showPermissionSettingsSnackbar();
                }*/
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
        currentDisposable = App.getRepositoriesComponent()
                .favoriteImagesRepository()
                .add(getCurrentImage())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> onFavoriteError(e, true))
                .subscribe();
    }

    private void removeFromFavorite() {
        onFavoriteChange(false);
        currentDisposable = App.getRepositoriesComponent()
                .favoriteImagesRepository()
                .remove(getCurrentImage())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> onFavoriteError(e, false))
                .subscribe();
    }

    private void onFavoriteChange(boolean behavior) {
        dispose();
        getCurrentImage().setFavorite(behavior);
        updateFavoriteState(true);
    }

    private void onFavoriteError(Throwable e, boolean behavior) {
        onFavoriteChange(!behavior);
        Log.e("ru.blizzed.yandex", e.toString());
    }

    private void dispose() {
        if (currentDisposable != null)
            currentDisposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }
}