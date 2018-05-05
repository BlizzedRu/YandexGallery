package ru.blizzed.yandexgallery.data.repositories;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.blizzed.yandexgallery.data.dao.FavoriteDAO;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;

public class FavoriteImagesRepository {

    private FavoriteDAO favoriteDAO;

    private Subject<FavoriteImageEvent> subject;

    public FavoriteImagesRepository(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public Flowable<List<URLImage>> getAll() {
        return favoriteDAO.getAll();
    }

    public Flowable<List<URLImage>> get(int limit, int offset) {
        return favoriteDAO.get(limit, offset);
    }

    public Flowable<List<URLImage>> getIfContains(List<URLImage> images) {
        return favoriteDAO.get(Observable.fromIterable(images)
                .map(URLImage::getId)
                .toList()
                .blockingGet()
                .toArray(new Long[images.size()])
        );
    }

    public void subscribe(Observer<FavoriteImageEvent> observer) {
        if (subject == null) subject = PublishSubject.create();
        subject.publish().refCount().subscribe(observer);
        //ConnectableObservable<FavoriteImageEvent> obs = subject.publish();
        //obs.subscribe(observer);
        //obs.connect();
    }

    public Completable remove(URLImage image) {
        return Completable.fromAction(() -> favoriteDAO.delete(image))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> {
                    if (subject != null)
                        subject.onNext(new FavoriteImageEvent(image, FavoriteImageEvent.Type.REMOVED));
                });
    }

    public Completable add(URLImage image) {
        return Completable.fromAction(() -> favoriteDAO.insertAll(image))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> {
                    if (subject != null) ;
                    subject.onNext(new FavoriteImageEvent(image, FavoriteImageEvent.Type.ADDED));
                });
    }

    public Maybe<URLImage> contains(URLImage image) {
        return favoriteDAO.get(image.getId()).subscribeOn(Schedulers.io());
    }

    public Single<Integer> count() {
        return favoriteDAO.getCount().subscribeOn(Schedulers.io());
    }

}
