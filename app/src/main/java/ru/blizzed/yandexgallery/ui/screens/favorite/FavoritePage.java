package ru.blizzed.yandexgallery.ui.screens.favorite;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessURLImageListFragment;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

public class FavoritePage extends EndlessURLImageListFragment implements EndlessImageListContract.View<URLImage> {

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
    @Override
    protected FavoritePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.images_favorite_spans
                : R.integer.images_favorite_spans_horizontal
        );
    }
}
