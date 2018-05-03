package ru.blizzed.yandexgallery.ui.screens.feed;

import java.util.List;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;

public class FeedCategoriesModel implements FeedContract.Model {

    private PixabayImagesRepository repository;

    public FeedCategoriesModel(PixabayImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryParam.Category> getCategories() {
        return repository.getCategories();
    }

}
