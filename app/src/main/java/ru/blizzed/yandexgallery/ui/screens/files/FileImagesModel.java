package ru.blizzed.yandexgallery.ui.screens.files;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolderEvent;
import ru.blizzed.yandexgallery.data.repositories.FileImagesRepository;

public class FileImagesModel implements FilesContract.Model {

    private FileImagesRepository repository;

    public FileImagesModel(FileImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<FileImagesFolder> getImageFoldersAsync() {
        return repository.asyncScan();
    }

    @Override
    public List<FileImagesFolder> getImageFolders() {
        return repository.getFolders();
    }

    @Override
    public void subscribeToChanges(Observer<FileImagesFolderEvent> observer) {
        repository.subscribe(observer);
    }

}
