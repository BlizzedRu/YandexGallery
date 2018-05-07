package ru.blizzed.yandexgallery.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;

public abstract class ImageLoader<T extends Image> {

    private final static RequestOptions baseRequestOptions = new RequestOptions()
            .dontTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static final ImageLoader<URLImage> URL_IMAGE_PREVIEW = new ImageLoader<URLImage>() {
        @Override
        public void loadImage(ImageView imageView, URLImage image, boolean usePlaceholder) {
            RequestBuilder<Drawable> previewRequest = Glide.with(imageView)
                    .load(image.getPreviewURL());

            Glide.with(imageView)
                    .load(image.getMediumURL())
                    .thumbnail(previewRequest)
                    .apply(ImageLoader.generateOptions(usePlaceholder))
                    .into(imageView);
        }
    };
    public static final ImageLoader<URLImage> URL_IMAGE_FULL = new ImageLoader<URLImage>() {
        @Override
        public void loadImage(ImageView imageView, URLImage image, boolean usePlaceholder) {
            RequestBuilder<Drawable> previewRequest = Glide.with(imageView)
                    .load(image.getPreviewURL());

            Glide.with(imageView)
                    .load(image.getLargeURL())
                    .thumbnail(previewRequest)
                    .apply(ImageLoader.generateOptions(usePlaceholder))
                    .into(imageView);
        }
    };
    public static final ImageLoader<FileImage> FILE_IMAGE = new ImageLoader<FileImage>() {
        @Override
        public void loadImage(ImageView imageView, FileImage image, boolean usePlaceholder) {
            Glide.with(imageView)
                    .load(image.getFile())
                    .apply(ImageLoader.generateOptions(usePlaceholder))
                    .into(imageView);
        }
    };

    public static void recycle(ImageView imageView) {
        Glide.with(imageView).clear(imageView);
        imageView.setImageDrawable(null);
    }

    private static RequestOptions generateOptions(boolean usePlaceholder) {
        RequestOptions options = baseRequestOptions.clone();
        if (usePlaceholder) options = options.placeholder(R.drawable.background_placeholder);
        return options.error(R.drawable.ic_no_images).fallback(R.drawable.ic_no_images);
    }

    public abstract void loadImage(ImageView imageView, T image, boolean usePlaceholder);

}
