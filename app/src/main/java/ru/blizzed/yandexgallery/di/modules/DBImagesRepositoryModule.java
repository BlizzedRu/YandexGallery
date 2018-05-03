package ru.blizzed.yandexgallery.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.data.GalleryDatabase;
import ru.blizzed.yandexgallery.data.dao.FavoriteDAO;
import ru.blizzed.yandexgallery.data.repositories.DBImagesRepository;
import ru.blizzed.yandexgallery.di.AppContext;
import ru.blizzed.yandexgallery.di.YandexGalleryScope;

@Module
public class DBImagesRepositoryModule {

    @YandexGalleryScope
    @Provides
    DBImagesRepository provideRepository(FavoriteDAO favoriteDAO) {
        return new DBImagesRepository(favoriteDAO);
    }

    @Provides
    FavoriteDAO provideFavoriteDAO(GalleryDatabase database) {
        return database.favoriteDAO();
    }

    @YandexGalleryScope
    @Provides
    GalleryDatabase provideDatabase(@AppContext Context context) {
        return Room.databaseBuilder(context, GalleryDatabase.class, "gallery").build();
    }

}
