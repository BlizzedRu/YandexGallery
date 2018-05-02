package ru.blizzed.yandexgallery.ui.screens.files.folder;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImage;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesFolder;

public class FolderImagesRepository implements EndlessImageListContract.Model<FileImage> {

    private FileImagesFolder folder;

    public FolderImagesRepository(FileImagesFolder folder) {
        this.folder = folder;
    }

    @Override
    public Observable<List<FileImage>> getImagesObservable(int offset) {
        return Observable.just(folder.getImagesList());
    }

    @Override
    public boolean hasNextImages() {
        return false;
    }

}
