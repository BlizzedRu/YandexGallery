package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.data.model.Image;

import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.BasePresenterImpl;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.Model;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.Presenter;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.View;

public class EndlessImageListPresenter<T extends Image> extends BasePresenterImpl<View<T>> implements Presenter<T> {

    private Model<T> repository;

    private Disposable imagesDisposable;

    private int imagesCount = 0;
    private boolean toEndScrolled = false;

    public EndlessImageListPresenter(Model<T> repository) {
        this.repository = repository;
        loadMore();
    }

    @Override
    public void onDownScrolled(int lastVisibleItemPosition) {
        if (!toEndScrolled && imagesCount - lastVisibleItemPosition < 10) {
            toEndScrolled = true;
            loadMore();
        }
    }

    @Override
    public void onImageClicked(T image) {
        getViewState().openImage(image);
    }

    private void loadMore() {
        if (imagesDisposable != null && !imagesDisposable.isDisposed()) imagesDisposable.dispose();
        getViewState().showLoading();
        imagesDisposable = repository.getImagesObservable(imagesCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(next -> imagesCount += next.size())
                .doOnNext(next -> toEndScrolled = false)
                .subscribe(next -> {
                    getViewState().addImages(next);
                }, error -> {
                    Log.e("ru.blizzed", error.toString());
                }, () -> getViewState().hideLoading());
    }

}
