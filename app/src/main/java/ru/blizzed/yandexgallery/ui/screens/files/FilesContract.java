package ru.blizzed.yandexgallery.ui.screens.files;

import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesFolder;

public interface FilesContract extends BaseContract {

    interface Model extends BaseModel {
        Flowable<FileImagesFolder> getImageFolders();
    }

    interface View extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addFolder(FileImagesFolder folder);

        void openFolder(FileImagesFolder folder);
    }

    interface Presenter extends BasePresenter {
        void onFolderClicked(FileImagesFolder folder);
    }

}
