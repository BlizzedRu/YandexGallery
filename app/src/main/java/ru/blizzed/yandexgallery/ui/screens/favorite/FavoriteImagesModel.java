package ru.blizzed.yandexgallery.ui.screens.favorite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.data.repositories.DBImagesRepository;

public class FavoriteImagesModel implements FavoriteContract.Model {

    private DBImagesRepository repository;

    public FavoriteImagesModel(DBImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<URLImage>> getAll() {
        return repository.getAll();
    }

    @Override
    public Completable remove(URLImage image) {
        return repository.remove(image);
    }

    @Override
    public Completable add(URLImage image) {
        return repository.add(image);
    }

}
