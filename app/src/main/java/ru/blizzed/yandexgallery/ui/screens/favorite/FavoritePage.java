package ru.blizzed.yandexgallery.ui.screens.favorite;

import android.app.DialogFragment;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageDialogFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenURLImageDialogFragment;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

public class FavoritePage extends EndlessImageListFragment<URLImage> implements EndlessImageListContract.View<URLImage> {

    public static final String TAG = "favorite";

    @Inject
    @InjectPresenter
    FavoritePresenter presenter;

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        DaggerFavoriteScreenComponent.builder()
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);
    }

    @ProvidePresenter
    FavoritePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected FavoritePresenter getPresenter() {
        return presenter;
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
    protected int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.images_favorite_spans
                : R.integer.images_favorite_spans_horizontal
        );
    }
}
