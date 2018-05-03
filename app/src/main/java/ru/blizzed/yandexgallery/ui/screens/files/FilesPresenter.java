package ru.blizzed.yandexgallery.ui.screens.files;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;

@InjectViewState
public class FilesPresenter extends FilesContract.BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private Disposable repositoryDisposable;
    private FilesContract.Model model;

    public FilesPresenter(FilesContract.Model model) {
        this.model = model;
        repositoryDisposable = model.getImageFolders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getViewState()::addFolder);
    }

    @Override
    public void onFolderClicked(FileImagesFolder folder) {
        getViewState().openFolder(folder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repositoryDisposable.dispose();
    }
}
