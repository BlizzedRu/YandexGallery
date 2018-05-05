package ru.blizzed.yandexgallery.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;
import ru.blizzed.yandexgallery.di.AppContext;
import ru.blizzed.yandexgallery.di.ImagesPerRequest;
import ru.blizzed.yandexgallery.di.YandexGalleryScope;

@Module
public class PixabayRepositoryModule {

    @YandexGalleryScope
    @Provides
    PixabayImagesRepository provideRepository(@ImagesPerRequest int loadCount) {
        return new PixabayImagesRepository(loadCount);
    }

    @Provides
    @ImagesPerRequest
    int provideLoadCount(@AppContext Context context) {
        return context.getResources().getInteger(R.integer.images_per_request);
    }

}
