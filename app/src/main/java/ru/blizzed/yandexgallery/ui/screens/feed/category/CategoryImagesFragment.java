package ru.blizzed.yandexgallery.ui.screens.feed.category;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessURLImageListFragment;

public class CategoryImagesFragment extends EndlessURLImageListFragment implements CategoryImagesContract.View {

    private static final String KEY_CATEGORY = "category";

    @Inject
    @InjectPresenter
    CategoryImagesPresenter presenter;

    public static CategoryImagesFragment newInstance(CategoryParam.Category category) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CATEGORY, category);

        CategoryImagesFragment fragment = new CategoryImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        CategoryParam.Category category = (CategoryParam.Category) getArguments().getSerializable(KEY_CATEGORY);
        DaggerCategoryFeedScreenComponent.builder()
                .feedCategory(category)
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);
    }

    @ProvidePresenter
    @Override
    public CategoryImagesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onImageFavoriteStateChanged(URLImage image, boolean isFavorite) {
        //getImages().get(getImages().indexOf(image)).setFavorite(isFavorite);
    }

}
