package ru.blizzed.yandexgallery.ui.screens.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.customs.GridSpacingItemDecoration;
import ru.blizzed.yandexgallery.ui.screens.FullScreenImageFragment;
import ru.blizzed.yandexgallery.ui.screens.feed.model.PixabayImagesRepository;

public class CategoryPage extends Fragment {

    private static final String CATEGORY = "category";
    @BindView(R.id.imagesRecycler)
    RecyclerView imagesRecycler;
    private CategoryParam.Category category;
    private ImagesFeedViewAdapter imagesAdapter;

    private List<URLImage> images;

    private PixabayImagesRepository repository;
    private Disposable imagesDisposable;

    private boolean toEndScrolled = false;

    private Unbinder unbinder;

    public static CategoryPage newInstance(CategoryParam.Category category) {
        Bundle args = new Bundle();
        args.putString(CATEGORY, category.name());
        CategoryPage fragment = new CategoryPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new PixabayImagesRepository(30);
        images = new ArrayList<>();

        category = CategoryParam.Category.valueOf(getArguments().getString(CATEGORY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_category, container, false);

        unbinder = ButterKnife.bind(this, view);

        int spanCount = getResources().getInteger(R.integer.feed_span_count);
        imagesAdapter = new ImagesFeedViewAdapter(spanCount, images, (position, item) -> {
            FullScreenImageFragment dialog = FullScreenImageFragment.newInstance(item);
            dialog.show(getFragmentManager(), "");
            Completable.fromAction(() -> {
                App.getInstance().getDatabase().favoriteDAO().insertAll(item);
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
        });
        imagesRecycler.setAdapter(imagesAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        imagesRecycler.setLayoutManager(layoutManager);
        imagesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.images_preview_spacing)));

        loadMore();

        imagesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!toEndScrolled && dy > 0 && images.size() - layoutManager.findLastVisibleItemPosition() < 10) {
                    toEndScrolled = true;
                    loadMore();
                }
            }
        });

        return view;
    }

    private void loadMore() {
        if (imagesDisposable != null && !imagesDisposable.isDisposed()) imagesDisposable.dispose();
        imagesDisposable = repository.getImagesByTag(category, images.size())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(next -> {
                    int oldSize = images.size();
                    images.addAll(next);
                    imagesAdapter.notifyItemRangeInserted(oldSize, oldSize + next.size());
                    toEndScrolled = false;
                }, error -> {
                    Log.e("ru.blizzed", error.toString());
                }, () -> Log.e("ru.blizzed", "Completed"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        imagesDisposable.dispose();
    }

}
