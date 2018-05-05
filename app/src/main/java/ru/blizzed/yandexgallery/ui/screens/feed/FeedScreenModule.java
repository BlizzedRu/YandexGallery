package ru.blizzed.yandexgallery.ui.screens.feed;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.data.repositories.FavoriteImagesRepository;
import ru.blizzed.yandexgallery.data.repositories.PixabayImagesRepository;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.ui.screens.feed.category.CategoryImagesModel;
import ru.blizzed.yandexgallery.ui.screens.feed.category.CategoryImagesPresenter;

@Module
public class FeedScreenModule {

    @Inject
    public FeedScreenModule() {
    }

    @ScreensScope
    @Provides
    FeedContract.Model provideModel(PixabayImagesRepository repository) {
        return new FeedCategoriesModel(repository);
    }

    @ScreensScope
    @Provides
    FeedPresenter providePresenter(FeedContract.Model model) {
        return new FeedPresenter(model);
    }

    @ScreensScope
    @Provides
    CategoryImagesPresenter provideCategoryPresenter(CategoryImagesModel model) {
        return new CategoryImagesPresenter(model);
    }

    @ScreensScope
    @Provides
    CategoryImagesModel provideCategoryModel(PixabayImagesRepository repository,
                                             FavoriteImagesRepository favoriteRepository,
                                             CategoryParam.Category category) {
        return new CategoryImagesModel(repository, favoriteRepository, category);
    }

}
