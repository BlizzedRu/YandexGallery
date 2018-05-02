package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class FullScreenURLImageFragment extends FullScreenImageFragment<URLImage> {

    public static FullScreenURLImageFragment newInstance(ArrayList<URLImage> images, int position) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_IMAGES, images);
        args.putInt(KEY_POSITION, position);

        FullScreenURLImageFragment fragment = new FullScreenURLImageFragment();
        fragment.setArguments(args);
        return fragment;
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
        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
    }

}