package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.customs.RecycledPagerAdapter;

public class FullScreenImagePagerAdapter<T extends Image> extends RecycledPagerAdapter<FullScreenImagePagerAdapter.ViewHolder> {

    private List<T> images;
    private LayoutInflater inflater;

    private FullScreenImageFragment.ImageLoader<T> imageLoader;

    public FullScreenImagePagerAdapter(List<T> images, FullScreenImageFragment.ImageLoader<T> imageLoader) {
        this.images = images;
        this.imageLoader = imageLoader;
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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        imageLoader.loadImage(viewHolder.img, images.get(position));
    }

    static class ViewHolder extends RecycledPagerAdapter.ViewHolder {

        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
