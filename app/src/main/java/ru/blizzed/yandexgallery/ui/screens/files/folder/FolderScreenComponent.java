package ru.blizzed.yandexgallery.ui.screens.files.folder;

import dagger.BindsInstance;
import dagger.Component;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.screens.files.FilesScreenModule;

@ScreensScope
@Component(modules = FilesScreenModule.class, dependencies = RepositoriesComponent.class)
public interface FolderScreenComponent {

    void inject(FolderImagesFragment fragment);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder filesFolder(FileImagesFolder folder);

        Builder repositoriesComponent(RepositoriesComponent repositoriesComponent);

        FolderScreenComponent build();

    }

}
