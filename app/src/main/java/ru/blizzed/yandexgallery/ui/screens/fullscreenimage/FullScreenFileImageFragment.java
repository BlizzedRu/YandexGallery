package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImage;

public class FullScreenFileImageFragment extends FullScreenImageFragment<FileImage> {

    public static FullScreenFileImageFragment newInstance(ArrayList<FileImage> images, int position) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_IMAGES, images);
        args.putInt(KEY_POSITION, position);

        FullScreenFileImageFragment fragment = new FullScreenFileImageFragment();
        fragment.setArguments(args);
        return fragment;
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
        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
    }

}
