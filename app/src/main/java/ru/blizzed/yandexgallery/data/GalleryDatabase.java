package ru.blizzed.yandexgallery.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.blizzed.yandexgallery.data.dao.FavoriteDAO;
import ru.blizzed.yandexgallery.data.model.URLImage;

@Database(entities = {URLImage.class}, version = 1, exportSchema = false)
public abstract class GalleryDatabase extends RoomDatabase {
    public abstract FavoriteDAO favoriteDAO();
}
