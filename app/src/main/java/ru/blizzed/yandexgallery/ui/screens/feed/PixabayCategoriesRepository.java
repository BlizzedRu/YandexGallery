package ru.blizzed.yandexgallery.ui.screens.feed;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.pixabaylib.params.CategoryParam;

public class PixabayCategoriesRepository implements FeedContract.Model {

    public PixabayCategoriesRepository() {
    }

    @Override
    public List<CategoryParam.Category> getCategories() {
        return Observable.fromArray(CategoryParam.Category.values())
                .sorted((o1, o2) -> o1.name().compareTo(o2.name()))
                .toList()
                .blockingGet();
    }

}
