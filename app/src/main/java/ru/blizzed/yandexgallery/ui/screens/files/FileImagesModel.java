package ru.blizzed.yandexgallery.ui.screens.files;

import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.data.repositories.FileImagesRepository;

public class FileImagesModel implements FilesContract.Model {

    FileImagesRepository repository;

    public FileImagesModel(FileImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<FileImagesFolder> getImageFolders() {
        return repository.getImageFolders();
    }
}
