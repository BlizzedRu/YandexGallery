package ru.blizzed.yandexgallery.ui.screens.feed.category;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.pixabaylib.params.PixabayParams;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;

public class CategoryImagesModel implements EndlessImageListContract.Model<URLImage> {

    private CategoryParam.Category category;
    private PixabayImagesRepository repository;

    public CategoryImagesModel(PixabayImagesRepository repository, CategoryParam.Category category) {
        this.category = category;
        this.repository = repository;
    }

    @Override
    public Observable<List<URLImage>> getImagesObservable(int offset) {
        return repository.createRequest(Collections.singletonList(PixabayParams.CATEGORY.of(category)), offset);
    }

    @Override
    public boolean hasNextImages() {
        return true;
    }


}
