package ru.blizzed.yandexgallery.ui.screens.feed.category;

import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import ru.blizzed.pixabaylib.model.PixabayResult;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.PixabayParams;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.data.repositories.FavoriteImagesRepository;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;

public class CategoryImagesModel implements CategoryImagesContract.Model {

    private CategoryParam.Category category;
    private PixabayImagesRepository imagesRepository;
    private FavoriteImagesRepository favoritesRepository;

    private long imagesCount = -1;

    public CategoryImagesModel(PixabayImagesRepository repository,
                               FavoriteImagesRepository favoritesRepository,
                               CategoryParam.Category category) {
        this.category = category;
        this.imagesRepository = repository;
        this.favoritesRepository = favoritesRepository;
    }

    @Override
    public Observable<List<URLImage>> getImagesObservable(int offset) {
        return imagesRepository.createRequest(Collections.singletonList(PixabayParams.CATEGORY.of(category)), offset)
                .doOnNext(next -> {
                    if (imagesCount == -1) imagesCount = next.getTotal();
                })
                .map(PixabayResult::getHits)
                .map(hits -> Observable.fromIterable(hits).map(URLImage::new).toList().blockingGet())
                .doOnNext(next -> {
                    /*
                     * We've got a batch of new images but are we sure they are not in favorites? Check that!
                     * It's better way (IMO) to unite it here as a list and not each item every time when full screen image opens
                     */
                    favoritesRepository.getIfContains(next)
                            .subscribe(imgs -> {
                                for (URLImage image : next) {
                                    if (imgs.contains(image))
                                        image.setFavorite(imgs.get(imgs.indexOf(image)).isFavorite());
                                }
                            }, error -> Logger.e(error, "With getting favorites from DB"));
                })
                .cache();
    }

    @Override
    public Single<Boolean> hasNextImages(int alreadyLoaded) {
        return Single.just(imagesCount == -1 || alreadyLoaded < imagesCount);
    }

    @Override
    public void subscribeToFavorites(Observer<FavoriteImageEvent> eventsObserver) {
        //favoritesRepository.subscribe(eventsObserver);
    }

}
