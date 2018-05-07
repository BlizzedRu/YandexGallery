package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public class EndlessImageListViewAdapter<T extends Image> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int IMAGE = 1;
    public static final int LOADING = 2;
    public static final int ERROR = 3;
    private List<T> images;
    private OnClickListener<T> listener;
    private FooterItemStatus footerItemStatus;
    private ImageLoader<T> imageLoader;

    public EndlessImageListViewAdapter(int spanCount, List<T> images, ImageLoader<T> imageLoader, @NonNull OnClickListener<T> listener) {
        this.images = images;
        this.spanCount = spanCount;
        this.imageLoader = imageLoader;
        this.listener = listener;

        footerItemStatus = FooterItemStatus.EMPTY;
    }

    public void setFooterItemStatus(FooterItemStatus footerItemStatus) {
        /*int position = getItemCountWith(footerItemStatus) - 1;
        position = position < 0 ? 0 : position;

        if (this.footerItemStatus == FooterItemStatus.EMPTY) {
            if (footerItemStatus != FooterItemStatus.EMPTY) {
                notifyItemInserted(position);
            }
        } else {
            if (footerItemStatus == FooterItemStatus.EMPTY) {
                notifyItemRemoved(position);
            } else notifyItemChanged(position);
        }*/

        // The code above produce RecyclerView's bug (Inconsistency) and strange behavior of scrolling
        // Temporarily changed that to "hardcore" ndsc();

        notifyDataSetChanged();

        this.footerItemStatus = footerItemStatus;
    }

    private int childWidth;
    private int childHeight;
    private int spanCount;

    @Override
    public int getItemViewType(int position) {
        if (position < images.size()) return IMAGE;
        else {
            if (footerItemStatus == FooterItemStatus.ERROR) return ERROR;
            else return LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return getItemCountWith(footerItemStatus);
    }

    private int getItemCountWith(FooterItemStatus footerItemStatus) {
        return images.size() + (footerItemStatus == FooterItemStatus.EMPTY ? 0 : 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int parentWidth = parent.getMeasuredWidth();
        childWidth = parentWidth / spanCount;
        childHeight = childWidth / 4 * 3;

        if (viewType == IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == ERROR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
            return new ErrorViewHolder(view);
        } else if (viewType == LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        throw new IllegalArgumentException("Expected IMAGE, ERROR or LOADING");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            T image = images.get(position);
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(childWidth, childHeight));
            imageLoader.loadImage(((ImageViewHolder) holder).img, image, true);
            holder.itemView.setOnClickListener(v -> listener.onImageClick(image));
        } else if (holder instanceof ErrorViewHolder) {
            holder.itemView.setOnClickListener(v -> listener.onErrorClick());
            ((ErrorViewHolder) holder).error.setText(R.string.loading_error_message);
            ((ErrorViewHolder) holder).repeat.setText(R.string.loading_error_action);
        } else if (holder instanceof LoadingViewHolder) {

        }
    }

    public enum FooterItemStatus {
        EMPTY, ERROR, LOADING
    }

    public interface OnClickListener<T extends Image> {
        void onImageClick(T image);

        void onErrorClick();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ImageViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    static class ErrorViewHolder extends RecyclerView.ViewHolder {
        private TextView error;
        private TextView repeat;

        ErrorViewHolder(View itemView) {
            super(itemView);
            error = itemView.findViewById(R.id.error);
            repeat = itemView.findViewById(R.id.repeat);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progress;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress);
        }
    }

}
