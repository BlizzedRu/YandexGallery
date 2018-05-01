package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.screens.FullScreenImageFragment;

public abstract class EndlessImageListFragment extends MvpFragment implements EndlessImageListContract.View {

    @BindView(R.id.imagesRecycler)
    RecyclerView imagesRecycler;

    @InjectPresenter
    EndlessImageListPresenter presenter;

    private EndlessImageListViewAdapter imagesAdapter;
    private List<URLImage> images;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_category, container, false);

        unbinder = ButterKnife.bind(this, view);

        int spanCount = getResources().getInteger(R.integer.feed_span_count);
        imagesAdapter = new EndlessImageListViewAdapter(spanCount, images, (position, item) -> {
            FullScreenImageFragment dialog = FullScreenImageFragment.newInstance(item);
            dialog.show(getFragmentManager(), "");
        });
        imagesRecycler.setAdapter(imagesAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        imagesRecycler.setLayoutManager(layoutManager);
        imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.images_preview_spacing)));

        imagesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) presenter.onDownScrolled(layoutManager.findLastVisibleItemPosition());
            }
        });

        return view;
    }

    @ProvidePresenter
    EndlessImageListPresenter providePresenter() {
        return new EndlessImageListPresenter(provideModel());
    }

    protected abstract EndlessImageListContract.Model provideModel();

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

    @Override
    public void addImages(List<URLImage> images) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}