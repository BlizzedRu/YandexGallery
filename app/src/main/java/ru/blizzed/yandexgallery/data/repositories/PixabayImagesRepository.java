package ru.blizzed.yandexgallery.data.repositories;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.Param;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.utils.PixabayHelper;

public class PixabayImagesRepository {

    private int loadCount;

    public PixabayImagesRepository(int loadCount) {
        this.loadCount = loadCount;
    }

    public Observable<List<URLImage>> createRequest(List<Param> additionalParams, int offset) {
        return PixabayHelper.createRequest(additionalParams, offset, loadCount);
    }

    public List<CategoryParam.Category> getCategories() {
        return Observable.fromArray(CategoryParam.Category.values())
                .sorted((o1, o2) -> o1.name().compareTo(o2.name()))
                .toList()
                .blockingGet();
    }

}
