package ru.blizzed.yandexgallery.ui.screens.files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.screens.files.model.Image;

public class ImagePreviewViewAdapter extends RecyclerView.Adapter<ImagePreviewViewAdapter.ViewHolder> {

    private Context context;
    private List<Image> images;

    public ImagePreviewViewAdapter(List<Image> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_preview_square, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position).getFile())
                .centerCrop()
                .resizeDimen(R.dimen.image_preview, R.dimen.image_preview)
                .placeholder(R.drawable.background_placeholder)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return Math.min(images.size(), 12);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
