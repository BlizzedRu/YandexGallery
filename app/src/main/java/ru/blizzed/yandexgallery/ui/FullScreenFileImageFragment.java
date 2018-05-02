package ru.blizzed.yandexgallery.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import ru.blizzed.yandexgallery.ui.screens.files.model.FileImage;

public class FullScreenFileImageFragment extends FullScreenImageFragment<FileImage> {

    public static FullScreenFileImageFragment newInstance(FileImage image) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_IMAGE, image);

        FullScreenFileImageFragment fragment = new FullScreenFileImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadImage(ImageView imageView, FileImage image) {

        Glide.with(getActivity().getApplicationContext())
                .load(image.getFile())
                .apply(new RequestOptions()
                        .dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

}
