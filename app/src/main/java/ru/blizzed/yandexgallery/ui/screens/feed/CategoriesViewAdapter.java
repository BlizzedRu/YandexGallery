package ru.blizzed.yandexgallery.ui.screens.feed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.SelectableCategory;
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;
import ru.blizzed.yandexgallery.ui.customs.CategoryView;

public class CategoriesViewAdapter extends ItemClickableRecyclerViewAdapter<SelectableCategory> {

    public static final String KEY_SELECTION = "selection";

    public CategoriesViewAdapter(List<SelectableCategory> data, @NonNull OnItemClickListener<SelectableCategory> listener) {
        super(data, listener);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_category;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.contains(KEY_SELECTION)) {
                boolean selected = getData().get(holder.getAdapterPosition()).isSelected();
                ((ViewHolder) holder).categoryView.setSelected(selected);
            }
        } else super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SelectableCategory category = getData().get(position);
        CategoryView categoryView = ((ViewHolder) holder).categoryView;
        categoryView.setText(category.get().name().toLowerCase());
        categoryView.setSelected(category.isSelected());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CategoryView categoryView;

        ViewHolder(View itemView) {
            super(itemView);
            categoryView = (CategoryView) itemView;
        }
    }
}
