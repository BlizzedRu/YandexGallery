package ru.blizzed.yandexgallery.ui.screens.feed;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.SelectableCategory;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.customs.ItemOffsetDecoration;
import ru.blizzed.yandexgallery.ui.mvp.DiMvpFragment;
import ru.blizzed.yandexgallery.ui.screens.feed.category.CategoryImagesFragment;

import static ru.blizzed.yandexgallery.ui.screens.feed.CategoriesViewAdapter.KEY_SELECTION;

public class FeedPage extends DiMvpFragment implements FeedContract.View {

    public static final String TAG = "feed";

    @BindView(R.id.categoriesRecycler)
    RecyclerView categoriesRecycler;

    @Inject
    @InjectPresenter
    FeedPresenter presenter;

    private CategoriesViewAdapter categoriesAdapter;

    private Unbinder unbinder;

    private CategoryParam.Category currentCategory;

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        DaggerFeedScreenComponent.builder()
                .repositoriesComponent(App.getRepositoriesComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feed, container, false);

        unbinder = ButterKnife.bind(this, view);

        categoriesAdapter = new CategoriesViewAdapter(Collections.emptyList(), (position, item) -> presenter.onCategorySelected(position, item));
        categoriesRecycler.setAdapter(categoriesAdapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.images_preview_spacing));

        return view;
    }

    @ProvidePresenter
    FeedPresenter providePresenter() {
        return presenter;
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
        FragmentManager fragmentManager = getChildFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (currentCategory != null)
            transaction.hide(fragmentManager.findFragmentByTag(currentCategory.name()));

        Fragment categoryFragment = fragmentManager.findFragmentByTag(category.name());
        if (categoryFragment != null) {
            transaction.show(categoryFragment);
        } else
            transaction.add(R.id.container, CategoryImagesFragment.newInstance(category), category.name());

        currentCategory = category;
        transaction.commit();
    }

}
