package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.mvp.DiMvpFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.activity.FullScreenImageActivity;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

import static android.app.Activity.RESULT_OK;

public abstract class EndlessImageListFragment<T extends Image> extends DiMvpFragment implements EndlessImageListContract.View<T> {

    public static final int FULL_SCREEN_REQUEST_CODE = 2324423;

    @BindView(R.id.imagesRecycler)
    RecyclerView imagesRecycler;

    @BindView(R.id.noImages)
    View emptyMessageView;

    private EndlessImageListViewAdapter<T> imagesAdapter;
    private ArrayList<T> images;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endless_image_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        int spanCount = getSpanCount();
        imagesAdapter = new EndlessImageListViewAdapter<>(spanCount, images, provideImageLoader(), (position, item) -> getPresenter().onImageClicked(item));
        imagesRecycler.setAdapter(imagesAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        imagesRecycler.setLayoutManager(layoutManager);
        imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.images_preview_spacing)));

        imagesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    getPresenter().onDownScrolled(layoutManager.findLastVisibleItemPosition());
            }
        });

        return view;
    }

    protected abstract EndlessImageListPresenter<T> getPresenter();

    protected abstract ImageLoader<T> provideImageLoader();

    protected ArrayList<T> getImages() {
        return images;
    }

    public EndlessImageListViewAdapter<T> getImagesAdapter() {
        return imagesAdapter;
    }

    @Override
    public void showEmptyMessage() {
        emptyMessageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyMessage() {
        emptyMessageView.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        imagesRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        imagesRecycler.setVisibility(View.GONE);
    }

    protected void scrollTo(int position) {
        imagesRecycler.scrollToPosition(position);
    }

    @Override
    public void addImages(List<T> images) {
        int oldSize = this.images.size();
        this.images.addAll(images);
        imagesAdapter.notifyItemRangeInserted(oldSize, this.images.size() - 1);
    }

    @Override
    public void removeImages(List<T> images) {
        this.images.removeAll(images);
        imagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /* Catching closed fullscreen image activity for scrolling to the last position */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FULL_SCREEN_REQUEST_CODE) {
                int position = data.getIntExtra(FullScreenImageActivity.KEY_POSITION, 0);
                scrollTo(position);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.preview_list_spans
                : R.integer.preview_list_spans_horizontal
        );
    }

}
