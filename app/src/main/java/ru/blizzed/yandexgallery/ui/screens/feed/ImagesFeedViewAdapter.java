package ru.blizzed.yandexgallery.ui.screens.feed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.blizzed.pixabaylib.model.PixabayImage;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;

public class ImagesFeedViewAdapter extends ItemClickableRecyclerViewAdapter<PixabayImage> {

    private int childWidth;
    private int childHeight;
    private int spanCount;

    public ImagesFeedViewAdapter(int spanCount, List<PixabayImage> data, @NonNull OnItemClickListener<PixabayImage> listener) {
        super(data, listener);
        this.spanCount = spanCount;
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.image_preview;
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
        PixabayImage image = getData().get(position);

        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(childWidth, childHeight));

        Picasso.get()
                .load(image.getWebformatURL())
                .placeholder(R.drawable.background_placeholder)
                .into(((ViewHolder) holder).img);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
