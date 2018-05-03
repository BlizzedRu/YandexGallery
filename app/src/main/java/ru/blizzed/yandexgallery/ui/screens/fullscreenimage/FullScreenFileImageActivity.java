package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.view.MenuItem;
import android.widget.Toast;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.FileImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class FullScreenFileImageActivity extends FullScreenImageActivity<FileImage> {

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
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
    }

}
