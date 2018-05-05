package ru.blizzed.yandexgallery.ui.screens.favorite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.data.repositories.FavoriteImagesRepository;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;

public class FavoriteImagesModel implements EndlessImageListContract.Model<URLImage> {

    private FavoriteImagesRepository repository;
    private int perRequest;

    public FavoriteImagesModel(FavoriteImagesRepository repository, int perRequest) {
        this.repository = repository;
        this.perRequest = perRequest;
    }

    @Override
    public Observable<List<URLImage>> getImagesObservable(int offset) {
        return repository.get(perRequest, offset).toObservable().observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> hasNextImages(int alreadyLoaded) {
        return repository.count()
                .flatMap(count -> Single.just(alreadyLoaded < count))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable remove(URLImage image) {
        return repository.remove(image);
    }

    public Completable add(URLImage image) {
        return repository.add(image).observeOn(AndroidSchedulers.mainThread());
    }

    public void subscribe(Observer<FavoriteImageEvent> eventObserver) {
        repository.subscribe(eventObserver);
    }

}
