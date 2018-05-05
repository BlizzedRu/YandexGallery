package ru.blizzed.yandexgallery.data.repositories;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.pixabaylib.Pixabay;
import ru.blizzed.pixabaylib.model.PixabayImage;
import ru.blizzed.pixabaylib.model.PixabayResult;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.LangParam;
import ru.blizzed.pixabaylib.params.Param;
import ru.blizzed.pixabaylib.params.PixabayParams;
import ru.blizzed.yandexgallery.BuildConfig;

public class PixabayImagesRepository {

    static {
        Pixabay.initialize(BuildConfig.PixabayApiKey, LangParam.Lang.RU);
    }

    private int loadCount;

    public PixabayImagesRepository(int loadCount) {
        this.loadCount = loadCount;
    }

    public Observable<PixabayResult<PixabayImage>> createRequest(List<Param> additionalParams, int offset) {
        List<Param> params = new ArrayList<>(additionalParams);
        params.add(PixabayParams.PER_PAGE.of(loadCount));
        params.add(PixabayParams.PAGE.of(offset / loadCount + 1));
        return Pixabay.rxSearch().image(params.toArray(new Param[params.size()])).subscribeOn(Schedulers.io());
    }

    public List<CategoryParam.Category> getCategories() {
        return Observable.fromArray(CategoryParam.Category.values())
                .sorted((o1, o2) -> o1.name().compareTo(o2.name()))
                .toList()
                .blockingGet();
    }

}
