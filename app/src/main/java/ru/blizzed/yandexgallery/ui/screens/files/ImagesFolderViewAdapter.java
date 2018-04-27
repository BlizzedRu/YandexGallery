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
import ru.blizzed.yandexgallery.ui.ItemClickableRecyclerViewAdapter;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.screens.files.model.ImagesFolder;

public class ImagesFolderViewAdapter extends ItemClickableRecyclerViewAdapter<ImagesFolder> {

    private int imageSpacingPx;

    public ImagesFolderViewAdapter(List<ImagesFolder> data, @NonNull OnItemClickListener listener) {
        super(data, listener);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.folder_images;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        imageSpacingPx = context.getResources().getDimensionPixelSize(R.dimen.images_preview_spacing);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        ImagesFolder folder = getData().get(position);
        vh.title.setText(folder.getTitle());

        int imagesCount = folder.getImagesList().size();
        vh.description.setText(context.getResources().getQuantityString(R.plurals.folder_images_description, imagesCount, imagesCount));

        vh.imagesRecycler.setAdapter(new ImagePreviewViewAdapter(folder.getImagesList()));
        vh.imagesRecycler.setLayoutManager(new GridLayoutManager(context, 6));
        if (vh.imagesRecycler.getItemDecorationCount() == 0)
            vh.imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(6, imageSpacingPx));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.imagesRecycler)
        RecyclerView imagesRecycler;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
