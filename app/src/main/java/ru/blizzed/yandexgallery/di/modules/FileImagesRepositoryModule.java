package ru.blizzed.yandexgallery.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.data.repositories.FileImagesRepository;
import ru.blizzed.yandexgallery.di.YandexGalleryScope;

@Module
public class FileImagesRepositoryModule {

    @YandexGalleryScope
    @Provides
    FileImagesRepository provideRepository() {
        return new FileImagesRepository();
    }

}
