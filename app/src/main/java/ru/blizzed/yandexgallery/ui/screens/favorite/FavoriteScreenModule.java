package ru.blizzed.yandexgallery.ui.screens.favorite;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.data.repositories.FavoriteImagesRepository;
import ru.blizzed.yandexgallery.di.ImagesPerRequest;
import ru.blizzed.yandexgallery.di.ScreensScope;

@Module
public class FavoriteScreenModule {

    @Inject
    public FavoriteScreenModule() {
    }

    @ScreensScope
    @Provides
    FavoriteImagesModel provideModel(FavoriteImagesRepository repository, @ImagesPerRequest int perRequest) {
        return new FavoriteImagesModel(repository, perRequest);
    }

    @ScreensScope
    @Provides
    FavoritePresenter providePresenter(FavoriteImagesModel model) {
        return new FavoritePresenter(model);
    }

}
