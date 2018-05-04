package ru.blizzed.yandexgallery.ui.screens.files;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolderEvent;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface FilesContract extends BaseContract {

    interface PermissionsHelper {
        boolean hasPermissions(String permission);

        boolean canRequestPermissions(String permission);
    }

    interface Model extends BaseModel {
        Flowable<FileImagesFolder> getImageFoldersAsync();

        List<FileImagesFolder> getImageFolders();

        void subscribeToChanges(Observer<FileImagesFolderEvent> observer);
    }

    interface View extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addFolder(FileImagesFolder folder);

        void setFolders(List<FileImagesFolder> folders);

        void openFolder(FileImagesFolder folder);

        void updateFolder(FileImagesFolderEvent event);

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
