package ru.blizzed.yandexgallery.ui.screens.feed.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.Pixabay;
import ru.blizzed.pixabaylib.model.PixabayImage;
import ru.blizzed.pixabaylib.model.PixabayResult;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.LangParam;
import ru.blizzed.pixabaylib.params.Param;
import ru.blizzed.pixabaylib.params.PixabayParams;

public class PixabayImagesRepository {

    private int loadCount;

    public PixabayImagesRepository(int perPage) {
        this.loadCount = perPage;
        Pixabay.initialize("8747111-af76d80e5e357356f8f5ae4b0", LangParam.Lang.RU);
    }

    public Observable<List<PixabayImage>> getImagesByTag(CategoryParam.Category category, int offset) {
        return createRequest(Collections.singletonList(PixabayParams.CATEGORY.of(category)), offset);
    }

    public Observable<List<PixabayImage>> getImages(int offset) {
        return createRequest(Collections.emptyList(), offset);
    }

    public int getLoadCount() {
        return loadCount;
    }

    private Observable<List<PixabayImage>> createRequest(List<Param> additionalParams, int offset) {
        List<Param> params = new ArrayList<>(additionalParams);
        params.add(PixabayParams.PER_PAGE.of(loadCount));
        params.add(PixabayParams.PAGE.of(offset / loadCount + 1));

        return Pixabay.rxSearch()
                .image(params.toArray(new Param[params.size()]))
                .map(PixabayResult::getHits);
    }

}
