package ru.blizzed.yandexgallery.ui.screens.favorite;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.data.repositories.DBImagesRepository;
import ru.blizzed.yandexgallery.di.ScreensScope;

@Module
public class FavoriteScreenModule {

    @Inject
    public FavoriteScreenModule() {
    }

    @ScreensScope
    @Provides
    FavoriteContract.Model provideModel(DBImagesRepository repository) {
        return new FavoriteImagesModel(repository);
    }

    @ScreensScope
    @Provides
    FavoritePresenter providePresenter(FavoriteContract.Model model) {
        return new FavoritePresenter(model);
    }

}
