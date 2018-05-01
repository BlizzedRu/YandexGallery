package ru.blizzed.yandexgallery;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.blizzed.yandexgallery.dao.FavoriteDAO;
import ru.blizzed.yandexgallery.model.URLImage;

@Database(entities = {URLImage.class}, version = 1, exportSchema = false)
public abstract class GalleryDatabase extends RoomDatabase {
    public abstract FavoriteDAO favoriteDAO();
}
