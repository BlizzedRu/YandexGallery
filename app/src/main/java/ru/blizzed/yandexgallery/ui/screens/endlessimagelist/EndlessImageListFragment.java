package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.app.DialogFragment;
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
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageDialogFragment;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListViewAdapter.FooterItemStatus;

public abstract class EndlessImageListFragment<T extends Image> extends DiMvpFragment implements EndlessImageListContract.View<T>, FullScreenImageDialogFragment.OnCloseListener {

    public static final int FULL_SCREEN_REQUEST_CODE = 2324423;

    private static final String KEY_SCROLL_POSITION = "scroll_position";

    @BindView(R.id.imagesRecycler)
    RecyclerView imagesRecycler;

    @BindView(R.id.noImages)
    View emptyMessageView;

    private EndlessImageListViewAdapter<T> imagesAdapter;
    private ArrayList<T> images;
    private Unbinder unbinder;

    private DialogFragment fullScreenImageDialog;

    private int firstVisibleImagePosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();

        if (savedInstanceState != null) {
            firstVisibleImagePosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endless_image_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        int spanCount = getSpanCount();
        imagesAdapter = new EndlessImageListViewAdapter<T>(spanCount, images, provideImageLoader(), new EndlessImageListViewAdapter.OnClickListener<T>() {
            @Override
            public void onImageClick(T image) {
                getPresenter().onImageClicked(image);
            }

            @Override
            public void onErrorClick() {
                getPresenter().onErrorClicked();
            }
        });

        imagesRecycler.setAdapter(imagesAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (imagesAdapter.getItemViewType(position)) {
                    case EndlessImageListViewAdapter.IMAGE:
                        return 1;
                    case EndlessImageListViewAdapter.LOADING:
                        return spanCount;
                    case EndlessImageListViewAdapter.ERROR:
                        return spanCount;
                    default:
                        return spanCount;
                }
            }
        });

        imagesRecycler.setLayoutManager(layoutManager);
        imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.images_preview_spacing)));

        imagesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    getPresenter().onDownScrolled(layoutManager.findLastVisibleItemPosition());

                firstVisibleImagePosition = layoutManager.findFirstVisibleItemPosition();
            }
        });

        scrollTo(firstVisibleImagePosition);
        imagesAdapter.notifyDataSetChanged();

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

    @Override
    public void showErrorMessage() {
        imagesAdapter.setFooterItemStatus(FooterItemStatus.ERROR);
    }

    @Override
    public void hideErrorMessage() {
        imagesAdapter.setFooterItemStatus(FooterItemStatus.EMPTY);
    }

    @Override
    public void showLoading() {
        imagesAdapter.setFooterItemStatus(FooterItemStatus.LOADING);
    }

    @Override
    public void hideLoading() {
        imagesAdapter.setFooterItemStatus(FooterItemStatus.EMPTY);
    }

    @Override
    public void scrollTo(int position) {
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

    protected abstract DialogFragment provideFullScreenDialogFragment();

    @Override
    public void openImage(T image) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(FullScreenImageDialogFragment.KEY_IMAGES, getImages());
        args.putInt(FullScreenImageDialogFragment.KEY_POSITION, getImages().indexOf(image));
        args.putInt(FullScreenImageDialogFragment.KEY_REQUEST_CODE, FULL_SCREEN_REQUEST_CODE);
        fullScreenImageDialog = provideFullScreenDialogFragment();
        fullScreenImageDialog.setArguments(args);
        fullScreenImageDialog.show(getChildFragmentManager(), "fullscreen");
    }

    @Override
    public void closeImage() {
        if (fullScreenImageDialog != null)
            fullScreenImageDialog.dismiss();
    }

    @Override
    public void onClose(DialogFragment dialog, int position) {
        getPresenter().onImageClosed(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCROLL_POSITION, firstVisibleImagePosition);
    }

    protected int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.preview_list_spans
                : R.integer.preview_list_spans_horizontal
        );
    }

}
