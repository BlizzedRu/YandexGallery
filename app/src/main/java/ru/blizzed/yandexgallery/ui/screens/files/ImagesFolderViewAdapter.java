package ru.blizzed.yandexgallery.ui.screens.files;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.BaseRecyclerViewAdapter;
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesFolder;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

public class ImagesFolderViewAdapter extends BaseRecyclerViewAdapter<FileImagesFolder> {

    private ItemClickableRecyclerViewAdapter.OnItemClickListener<FileImagesFolder> listener;

    private int imageSpacingPx;

    private int maxImagesInFolder;

    public ImagesFolderViewAdapter(List<FileImagesFolder> data, @NonNull ItemClickableRecyclerViewAdapter.OnItemClickListener<FileImagesFolder> listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_folder_images;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        imageSpacingPx = context.getResources().getDimensionPixelSize(R.dimen.images_preview_spacing);
        maxImagesInFolder = context.getResources().getInteger(R.integer.images_folder_max_count);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        FileImagesFolder folder = getData().get(position);
        vh.title.setText(folder.getTitle());

        int imagesCount = folder.getImagesList().size();
        vh.description.setText(context.getResources().getQuantityString(R.plurals.folder_images_description, imagesCount, imagesCount));

        vh.imagesRecycler.setAdapter(new ImagePreviewViewAdapter(folder.getImagesList(), maxImagesInFolder));
        int spanCount = getSpanCount();
        vh.imagesRecycler.setLayoutManager(new GridLayoutManager(context, spanCount));
        if (vh.imagesRecycler.getItemDecorationCount() == 0)
            vh.imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, imageSpacingPx));

        vh.root.setOnClickListener(v -> listener.onItemClicked(position, folder));
    }

    private int getSpanCount() {
        return context.getResources().getInteger(OrientationUtils.get(context) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.images_folder_spans
                : R.integer.images_folder_spans_horizontal
        );
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.imagesRecycler)
        RecyclerView imagesRecycler;

        @BindView(R.id.root)
        View root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
