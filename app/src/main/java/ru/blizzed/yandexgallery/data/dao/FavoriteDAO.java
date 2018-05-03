package ru.blizzed.yandexgallery.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.data.model.URLImage;

@Dao
public interface FavoriteDAO {

    @Insert
    void insertAll(URLImage... images);

    @Delete
    void delete(URLImage image);

    @Query("SELECT * FROM urlimage")
    Flowable<List<URLImage>> getAll();

}
