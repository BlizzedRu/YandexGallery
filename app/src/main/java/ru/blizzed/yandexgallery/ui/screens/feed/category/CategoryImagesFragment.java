package ru.blizzed.yandexgallery.ui.screens.feed.category;

import android.app.DialogFragment;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageDialogFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenURLImageDialogFragment;

public class CategoryImagesFragment extends EndlessImageListFragment<URLImage> implements CategoryImagesContract.View {

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

    @Override
    public CategoryImagesPresenter getPresenter() {
        return presenter;
    }

    @ProvidePresenter
    public CategoryImagesPresenter providePresenter() {
        return presenter;
    }

    @Override
    public void onImageFavoriteStateChanged(URLImage image, boolean isFavorite) {
        //getImages().get(getImages().indexOf(image)).setFavorite(isFavorite);
    }

    @Override
    protected ImageLoader<URLImage> provideImageLoader() {
        return ImageLoader.URL_IMAGE_PREVIEW;
    }

    @Override
    public void openImage(URLImage image) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(FullScreenImageDialogFragment.KEY_IMAGES, getImages());
        args.putInt(FullScreenImageDialogFragment.KEY_POSITION, getImages().indexOf(image));
        args.putInt(FullScreenImageDialogFragment.KEY_REQUEST_CODE, FULL_SCREEN_REQUEST_CODE);
        DialogFragment dialog = new FullScreenURLImageDialogFragment();
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), "fullscreen");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
