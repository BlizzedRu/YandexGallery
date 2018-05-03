package ru.blizzed.yandexgallery.ui.screens.files.folder;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.yandexgallery.data.model.FileImage;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;

public class FolderImagesModel implements EndlessImageListContract.Model<FileImage> {

    private FileImagesFolder folder;

    public FolderImagesModel(FileImagesFolder folder) {
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
