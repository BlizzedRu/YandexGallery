package ru.blizzed.yandexgallery.ui.screens.feed;

import dagger.Component;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;

@ScreensScope
@Component(modules = FeedScreenModule.class, dependencies = RepositoriesComponent.class)
public interface FeedScreenComponent {

    void inject(FeedPage fragment);

}
