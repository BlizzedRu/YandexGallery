package ru.blizzed.yandexgallery.ui.screens.files;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.data.repositories.FileImagesRepository;
import ru.blizzed.yandexgallery.di.ScreensScope;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.files.folder.FolderImagesModel;
import ru.blizzed.yandexgallery.ui.screens.files.folder.FolderImagesPresenter;

@Module
public class FilesScreenModule {

    @Inject
    public FilesScreenModule() {
    }

    @ScreensScope
    @Provides
    FilesContract.Model provideModel(FileImagesRepository repository) {
        return new FileImagesModel(repository);
    }

    @ScreensScope
    @Provides
    FilesPresenter providePresenter(FilesContract.Model model) {
        return new FilesPresenter(model);
    }

    @ScreensScope
    @Provides
    EndlessImageListContract.Model<FileImage> provideFolderModel(FileImagesFolder folder) {
        return new FolderImagesModel(folder);
    }

    @ScreensScope
    @Provides
    FolderImagesPresenter provideFolderPresenter(EndlessImageListContract.Model<FileImage> model) {
        return new FolderImagesPresenter(model);
    }

}
