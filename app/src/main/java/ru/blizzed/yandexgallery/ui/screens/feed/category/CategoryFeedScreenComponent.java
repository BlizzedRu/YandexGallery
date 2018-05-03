package ru.blizzed.yandexgallery.ui.screens.feed.category;

import dagger.BindsInstance;
import dagger.Component;
import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.screens.feed.FeedScreenModule;

@ScreensScope
@Component(modules = FeedScreenModule.class, dependencies = RepositoriesComponent.class)
public interface CategoryFeedScreenComponent {

    void inject(CategoryImagesFragment fragment);

    CategoryImagesPresenter presenter();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder feedCategory(CategoryParam.Category category);

        Builder repositoriesComponent(RepositoriesComponent repositoriesComponent);

        CategoryFeedScreenComponent build();

    }

}
