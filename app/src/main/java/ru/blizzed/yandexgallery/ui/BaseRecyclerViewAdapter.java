package ru.blizzed.yandexgallery.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<DataType> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    private List<DataType> data;

    public BaseRecyclerViewAdapter(List<DataType> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return createViewHolder(LayoutInflater.from(context).inflate(getItemLayoutResId(), parent, false));
    }

    @LayoutRes
    protected abstract int getItemLayoutResId();

    protected abstract RecyclerView.ViewHolder createViewHolder(View itemView);

    public List<DataType> getData() {
        return data;
    }

    public void setData(List<DataType> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
