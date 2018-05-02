package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureImageView;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.RecycledPagerAdapter;

public class FullScreenImagePagerAdapter<T extends Image> extends RecycledPagerAdapter<FullScreenImagePagerAdapter.ViewHolder> {

    private OnImageClickListener<T> listener;

    private List<T> images;
    private LayoutInflater inflater;
    private ImageLoader<T> imageLoader;

    public FullScreenImagePagerAdapter(List<T> images, ImageLoader<T> imageLoader, @NonNull OnImageClickListener<T> listener) {
        this.images = images;
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        imageLoader.loadImage(viewHolder.img, images.get(position), false);

        viewHolder.img.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
            @Override
            public void onDown(@NonNull MotionEvent event) {
            }

            @Override
            public void onUpOrCancel(@NonNull MotionEvent event) {
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent event) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                listener.onImageClicked(position, images.get(position));
                return true;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent event) {
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        View v = inflater.inflate(R.layout.item_image_single, parent, false);

        return new ViewHolder(v);
    }

    public interface OnImageClickListener<T extends Image> {
        void onImageClicked(int position, T image);
    }

    static class ViewHolder extends RecycledPagerAdapter.ViewHolder {

        private GestureImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
