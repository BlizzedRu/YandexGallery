package ru.blizzed.yandexgallery.ui.screens.files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.FileImage;

public class ImagePreviewViewAdapter extends RecyclerView.Adapter<ImagePreviewViewAdapter.ViewHolder> {

    private Context context;
    private List<FileImage> images;

    private int maxSize;

    public ImagePreviewViewAdapter(List<FileImage> images, int maxSize) {
        this.images = images;
        this.maxSize = maxSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_preview_square, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(images.get(position).getFile())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.background_placeholder)
                )
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return Math.min(images.size(), maxSize);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
