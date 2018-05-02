package ru.blizzed.yandexgallery.ui.screens.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.SelectableCategory;
import ru.blizzed.yandexgallery.ui.customs.ItemOffsetDecoration;
import ru.blizzed.yandexgallery.ui.screens.feed.category.CategoryImagesFragment;

import static ru.blizzed.yandexgallery.ui.screens.feed.CategoriesViewAdapter.KEY_SELECTION;

public class FeedPage extends MvpFragment implements FeedContract.View {

    public static final String TAG = "feed";

    @BindView(R.id.categoriesRecycler)
    RecyclerView categoriesRecycler;

    @InjectPresenter
    FeedPresenter presenter;

    private CategoriesViewAdapter categoriesAdapter;
    private Map<CategoryParam.Category, Fragment> categoryPages;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryPages = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feed, container, false);

        unbinder = ButterKnife.bind(this, view);

        categoriesAdapter = new CategoriesViewAdapter(Collections.emptyList(), presenter::onCategorySelected);

        categoriesRecycler.setAdapter(categoriesAdapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.images_preview_spacing));

        return view;
    }

    @ProvidePresenter
    FeedPresenter providePresenter() {
        return new FeedPresenter(new PixabayCategoriesRepository());
    }

    @Override
    public void setCategories(List<SelectableCategory> categories) {
        categoriesAdapter.setData(categories);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void openCategory(SelectableCategory category) {
        openPage(category.get());
    }

    @Override
    public void selectCategory(int position) {
        categoriesAdapter.notifyItemChanged(position, KEY_SELECTION);
    }

    @Override
    public void deselectCategory(int position) {
        categoriesAdapter.notifyItemChanged(position, KEY_SELECTION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void openPage(CategoryParam.Category category) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, getPageForCategory(category)).commit();
    }

    private Fragment getPageForCategory(CategoryParam.Category category) {
        if (categoryPages.containsKey(category))
            return categoryPages.get(category);
        Fragment page = CategoryImagesFragment.newInstance(category);
        categoryPages.put(category, page);
        return page;
    }

}
