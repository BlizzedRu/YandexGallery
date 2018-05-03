package ru.blizzed.yandexgallery.ui.screens.favorite;

import dagger.Component;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;

@ScreensScope
@Component(modules = FavoriteScreenModule.class, dependencies = RepositoriesComponent.class)
public interface FavoriteScreenComponent {

    void inject(FavoritePage fragment);

}
