package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageActivity;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

import static android.app.Activity.RESULT_OK;

public abstract class EndlessImageListFragment<T extends Image> extends MvpFragment implements EndlessImageListContract.View<T> {

    public static final int FULL_SCREEN_REQUEST_CODE = 2324423;

    @BindView(R.id.imagesRecycler)
    RecyclerView imagesRecycler;

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
        imagesAdapter = new EndlessImageListViewAdapter<>(spanCount, images, provideImageLoader(), (position, item) -> getPresenter().onImageClicked(item)/*presenter.onImageClicked(item)*/);
        imagesRecycler.setAdapter(imagesAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        imagesRecycler.setLayoutManager(layoutManager);
        imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.images_preview_spacing)));

        imagesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // if (dy > 0) presenter.onDownScrolled(layoutManager.findLastVisibleItemPosition());
                if (dy > 0)
                    getPresenter().onDownScrolled(layoutManager.findLastVisibleItemPosition());
            }
        });

        return view;
    }

    /*@ProvidePresenter
    protected EndlessImageListPresenter<T> providePresenter() {
        return new EndlessImageListPresenter<>(provideModel());
    }*/

    protected abstract EndlessImageListPresenter<T> getPresenter();

    protected abstract EndlessImageListContract.Model<T> provideModel();

    protected abstract ImageLoader<T> provideImageLoader();

    protected ArrayList<T> getImages() {
        return images;
    }

    @Override
    public void showEmptyMessage() {

    }

    @Override
    public void hideEmptyMessage() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideContent() {

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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /* Ловим закрытие активити для полноэкранного просмотра изображения,
     * чтобы проскроллить лену до позиции, на которой юзер закончил просмотр
     */
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

    private int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.preview_list_spans
                : R.integer.preview_list_spans_horizontal
        );
    }

}
