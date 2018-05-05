package ru.blizzed.yandexgallery.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import ru.blizzed.yandexgallery.data.model.URLImage;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavoriteDAO {

    @Insert(onConflict = REPLACE)
    void insertAll(URLImage... images);

    @Delete
    void delete(URLImage image);

    @Query("SELECT * FROM urlimage WHERE id = :imageId")
    Maybe<URLImage> get(long imageId);

    @Query("SELECT * FROM urlimage WHERE id IN (:imagesIds)")
    Flowable<List<URLImage>> get(Long[] imagesIds);

    @Query("SELECT * FROM urlimage")
    Flowable<List<URLImage>> getAll();

    @Query("SELECT * FROM urlimage LIMIT :limit OFFSET :offset")
    Flowable<List<URLImage>> get(int limit, int offset);

    @Query("SELECT COUNT(*) FROM urlimage")
    Single<Integer> getCount();

}
