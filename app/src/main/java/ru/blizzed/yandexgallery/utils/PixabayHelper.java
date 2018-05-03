package ru.blizzed.yandexgallery.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.Pixabay;
import ru.blizzed.pixabaylib.model.PixabayResult;
import ru.blizzed.pixabaylib.params.LangParam;
import ru.blizzed.pixabaylib.params.Param;
import ru.blizzed.pixabaylib.params.PixabayParams;
import ru.blizzed.yandexgallery.BuildConfig;
import ru.blizzed.yandexgallery.data.model.URLImage;

public final class PixabayHelper {

    static {
        Pixabay.initialize(BuildConfig.PixabayApiKey, LangParam.Lang.RU);
    }

    private PixabayHelper() {
    }

    public static Observable<List<URLImage>> createRequest(List<Param> additionalParams, int offset, int count) {
        List<Param> params = new ArrayList<>(additionalParams);
        params.add(PixabayParams.PER_PAGE.of(count));
        params.add(PixabayParams.PAGE.of(offset / count + 1));
        return Pixabay.rxSearch()
                .image(params.toArray(new Param[params.size()]))
                .map(PixabayResult::getHits)
                .map(hits -> Observable.fromIterable(hits).map(URLImage::new).toList().blockingGet());
    }

}
