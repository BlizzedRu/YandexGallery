package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;

public class EndlessImageListViewAdapter extends ItemClickableRecyclerViewAdapter<URLImage> {

    private int childWidth;
    private int childHeight;
    private int spanCount;

    public EndlessImageListViewAdapter(int spanCount, List<URLImage> data, @NonNull OnItemClickListener<URLImage> listener) {
        super(data, listener);
        this.spanCount = spanCount;
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
        URLImage image = getData().get(position);

        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(childWidth, childHeight));

        Glide.with(context)
                .load(image.getMediumURL())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.background_placeholder)
                        .dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
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
