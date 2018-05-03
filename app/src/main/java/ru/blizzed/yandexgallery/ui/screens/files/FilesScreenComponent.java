package ru.blizzed.yandexgallery.ui.screens.files;

import dagger.Component;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;

@ScreensScope
@Component(modules = FilesScreenModule.class, dependencies = RepositoriesComponent.class)
public interface FilesScreenComponent {

    void inject(FilesPage fragment);

}
