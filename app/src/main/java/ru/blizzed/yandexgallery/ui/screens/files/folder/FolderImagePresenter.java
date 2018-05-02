package ru.blizzed.yandexgallery.ui.screens.files.folder;

import com.arellomobile.mvp.InjectViewState;

import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListPresenter;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImage;

@InjectViewState
public class FolderImagePresenter extends EndlessImageListPresenter<FileImage> {

    public FolderImagePresenter(EndlessImageListContract.Model<FileImage> repository) {
        super(repository);
    }
}
