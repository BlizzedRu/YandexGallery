package ru.blizzed.yandexgallery.ui.screens.files;

import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface FilesContract extends BaseContract {

    interface PermissionsHelper {
        boolean hasPermissions(String permission);

        boolean canRequestPermissions(String permission);
    }

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

        void showNoPermissionsMessage();

        void hideNoPermissionsMessage();

        void requestPermissions(String permission);

        void openPermissionAppSettings();

    }

    interface Presenter extends BasePresenter {
        void onFolderClicked(FileImagesFolder folder);

        void onPermissionsDenied();

        void onPermissionsGranted();

        void onPermissionsGrantClicked();

        void setPermissionsHelper(PermissionsHelper helper);
    }

}
