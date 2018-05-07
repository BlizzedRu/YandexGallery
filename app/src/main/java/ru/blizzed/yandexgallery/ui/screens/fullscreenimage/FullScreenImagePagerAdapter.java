package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.RecycledPagerAdapter;
import ru.blizzed.yandexgallery.ui.customs.flickableimageview.FlickableImageView;

public class FullScreenImagePagerAdapter<T extends Image> extends RecycledPagerAdapter<FullScreenImagePagerAdapter.ViewHolder> {

    private OnImageListener<T> listener;
    private List<T> images;
    private LayoutInflater inflater;
    private ImageLoader<T> imageLoader;

    public FullScreenImagePagerAdapter(List<T> images, ImageLoader<T> imageLoader, @NonNull OnImageListener<T> listener) {
        this.images = images;
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        imageLoader.loadImage(viewHolder.img, images.get(position), false);

        viewHolder.img.setOnSingleTapListener(() -> listener.onImageClicked(position, images.get(position)));

        setUpListeners(viewHolder.img, position);
    }

    private void setUpListeners(FlickableImageView imageView, int position) {
        if (listener == null) return;

        imageView.setOnSingleTapListener(() -> listener.onImageClicked(position, images.get(position)));

        imageView.setOnDoubleTapListener(() -> listener.onImageDoubleClicked(imageView));

        imageView.setOnZoomListener(new FlickableImageView.OnFlickableImageViewZoomListener() {
            @Override
            public void onStartZoom() {
                listener.onStartZoom();
            }

            @Override
            public void onBackFromMinScale() {
                listener.onZoomBackToMinScale();
            }
        });

        imageView.setOnFlickListener(new FlickableImageView.OnFlickableImageViewFlickListener() {
            @Override
            public void onStartFlick() {
                listener.onStartFlick();
            }

            @Override
            public void onFinishFlick() {
                listener.onFinishFlick();
            }
        });

        imageView.setRoot(listener.getRoot());
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        View v = inflater.inflate(R.layout.item_image_single, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageLoader.recycle(((ViewHolder) object).img);
        super.destroyItem(container, position, object);
    }

    public interface OnImageListener<T extends Image> {
        void onImageClicked(int position, T image);

        void onImageDoubleClicked(FlickableImageView imageView);

        void onStartZoom();

        void onZoomBackToMinScale();

        void onStartFlick();

        void onFinishFlick();

        View getRoot();
    }

    static class ViewHolder extends RecycledPagerAdapter.ViewHolder {

        private FlickableImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
