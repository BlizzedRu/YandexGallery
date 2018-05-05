package ru.blizzed.yandexgallery.di.components;

import android.content.Context;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.data.repositories.FavoriteImagesRepository;
import ru.blizzed.yandexgallery.data.repositories.FileImagesRepository;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;
import ru.blizzed.yandexgallery.di.AppContext;
import ru.blizzed.yandexgallery.di.ImagesPerRequest;
import ru.blizzed.yandexgallery.di.YandexGalleryScope;
import ru.blizzed.yandexgallery.di.modules.ContextModule;
import ru.blizzed.yandexgallery.di.modules.DBImagesRepositoryModule;
import ru.blizzed.yandexgallery.di.modules.FileImagesRepositoryModule;
import ru.blizzed.yandexgallery.di.modules.PixabayRepositoryModule;

@YandexGalleryScope
@Component(modules = {
        AndroidInjectionModule.class,
        ContextModule.class,
        DBImagesRepositoryModule.class,
        FileImagesRepositoryModule.class,
        PixabayRepositoryModule.class
})
public interface RepositoriesComponent extends AndroidInjector<App> {

    FileImagesRepository fileImagesRepository();

    PixabayImagesRepository pixabayImagesRepository();

    FavoriteImagesRepository favoriteImagesRepository();

    @AppContext
    Context context();

    @ImagesPerRequest
    int imagesPerRequest();

}