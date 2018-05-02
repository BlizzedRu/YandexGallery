package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;

public class EndlessImageListViewAdapter<T extends Image> extends ItemClickableRecyclerViewAdapter<T> {

    private ImageLoader<T> imageLoader;

    private int childWidth;
    private int childHeight;
    private int spanCount;

    public EndlessImageListViewAdapter(int spanCount, List<T> data, ImageLoader<T> imageLoader, @NonNull OnItemClickListener<T> listener) {
        super(data, listener);
        this.spanCount = spanCount;
        this.imageLoader = imageLoader;
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_image_preview;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int parentWidth = parent.getMeasuredWidth();
        childWidth = parentWidth / spanCount;
        childHeight = childWidth / 4 * 3;
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(childWidth, childHeight));

        imageLoader.loadImage(((ViewHolder) holder).img, getData().get(position), true);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
