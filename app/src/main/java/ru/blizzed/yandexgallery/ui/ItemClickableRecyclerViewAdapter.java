package ru.blizzed.yandexgallery.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ItemClickableRecyclerViewAdapter<DataType> extends BaseRecyclerViewAdapter<DataType> {

    private OnItemClickListener listener;

    public ItemClickableRecyclerViewAdapter(List<DataType> data, @NonNull OnItemClickListener listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> notifyListener(position, getData().get(position)));
    }

    @SuppressWarnings("unchecked")
    private void notifyListener(int position, DataType item) {
        if (listener != null) listener.onItemClicked(position, item);
    }

    public interface OnItemClickListener<DataType> {
        void onItemClicked(int position, DataType item);
    }

}
