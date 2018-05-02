package ru.blizzed.yandexgallery.ui.screens.feed.category;

import android.content.Intent;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageActivity;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenURLImageActivity;

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
    protected ImageLoader<URLImage> provideImageLoader() {
        return ImageLoader.URL_IMAGE_PREVIEW;
    }

    @Override
    public void openImage(URLImage image) {
        Intent intent = new Intent(getActivity(), FullScreenURLImageActivity.class);
        intent.putExtra(FullScreenImageActivity.KEY_IMAGES, getImages());
        intent.putExtra(FullScreenImageActivity.KEY_POSITION, getImages().indexOf(image));
        intent.putExtra(FullScreenImageActivity.KEY_REQUEST_CODE, FULL_SCREEN_REQUEST_CODE);
        startActivityForResult(intent, FULL_SCREEN_REQUEST_CODE);
    }

}
