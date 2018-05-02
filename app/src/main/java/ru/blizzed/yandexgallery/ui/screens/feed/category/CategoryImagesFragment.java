package ru.blizzed.yandexgallery.ui.screens.feed.category;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.FullScreenURLImageFragment;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;

public class CategoryImagesFragment extends EndlessImageListFragment<URLImage> implements EndlessImageListContract.View<URLImage> {

    @InjectPresenter
    CategoryImagesPresenter presenter;
    private CategoryParam.Category category;

    public static CategoryImagesFragment newInstance(CategoryParam.Category category) {
        CategoryImagesFragment fragment = new CategoryImagesFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public CategoryImagesPresenter getPresenter() {
        return presenter;
    }

    @ProvidePresenter
    public CategoryImagesPresenter providePresenter() {
        return new CategoryImagesPresenter(provideModel());
    }

    @Override
    protected EndlessImageListContract.Model<URLImage> provideModel() {
        return new PixabayCategoryImagesRepository(category, getResources().getInteger(R.integer.images_per_request));
    }

    @Override
    public void openImage(URLImage image) {
        FullScreenURLImageFragment dialog = FullScreenURLImageFragment.newInstance(image);
        dialog.show(getFragmentManager(), "fullscreen_url_image");
    }

}
