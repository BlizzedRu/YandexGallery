package ru.blizzed.yandexgallery.ui.screens.feed.category;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.Param;
import ru.blizzed.pixabaylib.params.PixabayParams;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.utils.PixabayHelper;

public class PixabayCategoryImagesRepository implements EndlessImageListContract.Model {

    private int loadCount;

    private CategoryParam.Category category;

    public PixabayCategoryImagesRepository(CategoryParam.Category category, int perPage) {
        this.loadCount = perPage;
        this.category = category;
    }

    @Override
    public Observable<List<URLImage>> getImagesObservable(int offset) {
        return createRequest(Collections.singletonList(PixabayParams.CATEGORY.of(category)), offset);
    }

    @Override
    public boolean hasNextImages() {
        return true;
    }

    public int getLoadCount() {
        return loadCount;
    }

    private Observable<List<URLImage>> createRequest(List<Param> additionalParams, int offset) {
        return PixabayHelper.createRequest(additionalParams, offset, loadCount);
    }

}
