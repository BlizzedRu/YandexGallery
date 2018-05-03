package ru.blizzed.yandexgallery.ui.screens.favorite;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Section;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.BaseRecyclerViewAdapter;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListViewAdapter;

public class SectionsFavoriteViewAdapter extends BaseRecyclerViewAdapter<Section<URLImage>> {

    public SectionsFavoriteViewAdapter(List<Section<URLImage>> data) {
        super(data);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_section_favorite;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;

        Section<URLImage> section = getData().get(position);
        vh.title.setText(section.getTitle());

        EndlessImageListViewAdapter adapter = new EndlessImageListViewAdapter(2, section.getItems(), ImageLoader.URL_IMAGE_PREVIEW, (position1, item) -> {
        });
        vh.recycler.setAdapter(adapter);
        vh.recycler.setLayoutManager(new LinearLayoutManager(context));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.imagesRecycler)
        RecyclerView recycler;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
