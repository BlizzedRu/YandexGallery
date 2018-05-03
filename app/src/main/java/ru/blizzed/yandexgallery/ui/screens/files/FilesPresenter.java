package ru.blizzed.yandexgallery.ui.screens.files;

import android.Manifest;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.data.repositories.NoPermissionException;

@InjectViewState
public class FilesPresenter extends FilesContract.BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    private Disposable repositoryDisposable;
    private FilesContract.Model model;

    private FilesContract.PermissionsHelper permissionsHelper;

    public FilesPresenter(FilesContract.Model model) {
        this.model = model;
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
        repositoryDisposable.dispose();
    }

    private void loadImageFolders() {
        repositoryDisposable = model.getImageFolders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getViewState()::addFolder, error -> {
                    if (error instanceof NoPermissionException) {
                        getViewState().hideContent();
                        getViewState().showNoPermissionsMessage();
                        if (permissionsHelper.canRequestPermissions(PERMISSION))
                            getViewState().requestPermissions(PERMISSION);
                    }
                });
    }
}
