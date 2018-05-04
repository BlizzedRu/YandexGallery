package ru.blizzed.yandexgallery.ui.screens.files;

import android.Manifest;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolderEvent;
import ru.blizzed.yandexgallery.data.repositories.NoPermissionException;

@InjectViewState
public class FilesPresenter extends FilesContract.BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    private Disposable repositoryDisposable;
    private FilesContract.Model model;

    private FilesContract.PermissionsHelper permissionsHelper;

    private int foldersCount;

    private Observer<FileImagesFolderEvent> foldersChangesObserver = new Observer<FileImagesFolderEvent>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(FileImagesFolderEvent event) {
            getViewState().updateFolder(event);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    };

    public FilesPresenter(FilesContract.Model model) {
        this.model = model;
        model.subscribeToChanges(foldersChangesObserver);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().hideNoPermissionsMessage();
        getViewState().hideEmptyMessage();
        getViewState().showContent();
        loadImageFolders();
    }

    @Override
    public void setPermissionsHelper(FilesContract.PermissionsHelper helper) {
        this.permissionsHelper = helper;
    }

    @Override
    public void onFolderClicked(FileImagesFolder folder) {
        getViewState().openFolder(folder);
    }

    @Override
    public void onPermissionsDenied() {
        getViewState().hideContent();
        getViewState().showNoPermissionsMessage();
    }

    @Override
    public void onPermissionsGranted() {
        getViewState().hideNoPermissionsMessage();
        getViewState().showContent();
        loadImageFolders();
    }

    @Override
    public void onPermissionsGrantClicked() {
        if (permissionsHelper.canRequestPermissions(PERMISSION))
            getViewState().requestPermissions(PERMISSION);
        else getViewState().openPermissionAppSettings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (repositoryDisposable != null)
            repositoryDisposable.dispose();
    }

    private void loadImageFolders() {
        if (model.getImageFolders().isEmpty()) {
            foldersCount = 0;
            repositoryDisposable = model.getImageFoldersAsync()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(next -> foldersCount++)
                    .subscribe(getViewState()::addFolder, this::handleError, () -> {
                        if (foldersCount == 0) getViewState().showEmptyMessage();
                    });
        } else {
            List<FileImagesFolder> folders = model.getImageFolders();
            if (folders.isEmpty()) {
                getViewState().showEmptyMessage();
                return;
            }

            getViewState().showContent();
            getViewState().setFolders(folders);
        }
    }

    private void handleError(Throwable error) {
        if (error instanceof NoPermissionException) {
            getViewState().hideContent();
            getViewState().showNoPermissionsMessage();
            if (permissionsHelper.canRequestPermissions(PERMISSION))
                getViewState().requestPermissions(PERMISSION);
        }
    }

}
