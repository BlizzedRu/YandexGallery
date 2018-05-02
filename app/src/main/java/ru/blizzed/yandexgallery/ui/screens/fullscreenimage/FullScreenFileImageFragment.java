package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

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
        return new ImageLoader<FileImage>() {
            @Override
            void loadImage(ImageView imageView, FileImage image) {
                Glide.with(getActivity().getApplicationContext())
                        .load(image.getFile())
                        .apply(new RequestOptions()
                                .dontTransform()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(imageView);
            }
        };
    }
}
