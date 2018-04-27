package ru.blizzed.yandexgallery.ui.screens.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.SelectableCategory;
import ru.blizzed.yandexgallery.ui.customs.ItemOffsetDecoration;

import static ru.blizzed.yandexgallery.ui.screens.feed.CategoriesViewAdapter.KEY_SELECTION;

public class FeedPage extends Fragment {

    @BindView(R.id.categoriesRecycler)
    RecyclerView categoriesRecycler;

    private CategoriesViewAdapter categoriesAdapter;
    private List<SelectableCategory> categories;
    private Map<CategoryParam.Category, Fragment> categoryPages;
    private int lastSelectedCategoryPosition = -1;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = Observable.fromArray(CategoryParam.Category.values())
                .map(SelectableCategory::new)
                .toList()
                .blockingGet();
        categoryPages = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feed, container, false);

        unbinder = ButterKnife.bind(this, view);

        categoriesAdapter = new CategoriesViewAdapter(categories, (position, item) -> onCategorySelected(position));

        categoriesRecycler.setAdapter(categoriesAdapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.images_preview_spacing));

        onCategorySelected(0);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void onCategorySelected(int position) {
        if (position == lastSelectedCategoryPosition) return;
        categories.get(position).setSelected(true);

        categoriesAdapter.notifyItemChanged(position, KEY_SELECTION);

        if (lastSelectedCategoryPosition != -1) {
            categories.get(lastSelectedCategoryPosition).setSelected(false);
            categoriesAdapter.notifyItemChanged(lastSelectedCategoryPosition, KEY_SELECTION);
        }

        lastSelectedCategoryPosition = position;

        openPage(categories.get(position).get());
    }

    private void openPage(CategoryParam.Category category) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, getPageForCategory(category)).commit();
    }

    private Fragment getPageForCategory(CategoryParam.Category category) {
        if (categoryPages.containsKey(category))
            return categoryPages.get(category);
        Fragment page = CategoryPage.newInstance(category);
        categoryPages.put(category, page);
        return page;
    }

}
