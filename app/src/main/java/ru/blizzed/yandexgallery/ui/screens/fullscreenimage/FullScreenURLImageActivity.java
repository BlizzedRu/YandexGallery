package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.view.MenuItem;
import android.widget.Toast;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class FullScreenURLImageActivity extends FullScreenImageActivity<URLImage> {

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
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
    }

}
