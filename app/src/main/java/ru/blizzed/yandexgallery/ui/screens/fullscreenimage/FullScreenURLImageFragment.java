package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import ru.blizzed.yandexgallery.model.URLImage;

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
        return new ImageLoader<URLImage>() {
            @Override
            void loadImage(ImageView imageView, URLImage image) {
                RequestBuilder<Drawable> previewRequest = Glide.with(imageView)
                        .load(image.getPreviewURL());

                Glide.with(getActivity().getApplicationContext())
                        .load(image.getLargeURL())
                        .thumbnail(previewRequest)
                        .apply(new RequestOptions()
                                .dontTransform()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(imageView);
            }
        };
    }
}
