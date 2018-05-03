package ru.blizzed.yandexgallery.data.repositories;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.data.dao.FavoriteDAO;
import ru.blizzed.yandexgallery.data.model.URLImage;

public class DBImagesRepository {

    private FavoriteDAO favoriteDAO;

    public DBImagesRepository(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public Flowable<List<URLImage>> getAll() {
        return favoriteDAO.getAll();
    }

    public Observable<URLImage> getObservable() {
        return null;
    }

    public Completable remove(URLImage image) {
        return Completable.fromAction(() -> favoriteDAO.delete(image)).subscribeOn(Schedulers.io());
    }

    public Completable add(URLImage image) {
        return Completable.fromAction(() -> favoriteDAO.insertAll(image)).subscribeOn(Schedulers.io());
    }

}
