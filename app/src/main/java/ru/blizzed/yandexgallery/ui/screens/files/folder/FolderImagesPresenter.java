package ru.blizzed.yandexgallery.ui.screens.files.folder;

import com.arellomobile.mvp.InjectViewState;

import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListPresenter;

@InjectViewState
public class FolderImagesPresenter extends EndlessImageListPresenter<FileImage> {

    public FolderImagesPresenter(EndlessImageListContract.Model<FileImage> model) {
        super(model);
    }

}
