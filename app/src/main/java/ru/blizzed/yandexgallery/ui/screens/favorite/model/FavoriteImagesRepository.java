package ru.blizzed.yandexgallery.ui.screens.favorite.model;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.favorite.FavoriteContract;

public class FavoriteImagesRepository implements FavoriteContract.Model {

    @Override
    public Flowable<List<URLImage>> getAll() {
        return App.getInstance().getDatabase().favoriteDAO().getAll();
    }

    @Override
    public Observable<URLImage> getObservable() {
        return null;
    }

    @Override
    public void remove(URLImage image) {
        App.getInstance().getDatabase().favoriteDAO().delete(image);
    }

    @Override
    public void add(URLImage image) {
        App.getInstance().getDatabase().favoriteDAO().insertAll(image);
    }

}
