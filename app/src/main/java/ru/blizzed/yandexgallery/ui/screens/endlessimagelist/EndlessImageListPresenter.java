package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.data.model.Image;

import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.BasePresenterImpl;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.Model;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.Presenter;
import static ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract.View;

public class EndlessImageListPresenter<T extends Image> extends BasePresenterImpl<View<T>> implements Presenter<T> {

    private Model<T> model;

    private Disposable imagesDisposable;

    private int imagesCount = 0;
    private boolean toEndScrolled = false;

    public EndlessImageListPresenter(Model<T> model) {
        this.model = model;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadMore();
    }

    @Override
    public void onDownScrolled(int lastVisibleItemPosition) {
        if (!toEndScrolled && imagesCount - lastVisibleItemPosition < 10) {
            toEndScrolled = true;
            Disposable disposable = model.hasNextImages(imagesCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(has -> {
                        if (has) loadMore();
                    }, this::onErrorOccurred);
        }
    }

    @Override
    public void onImageClicked(T image) {
        getViewState().openImage(image);
    }

    @Override
    public void onImagesRemoved(List<T> images) {
        getViewState().removeImages(images);
    }

    private void loadMore() {
        getViewState().hideEmptyMessage();
        getViewState().showLoading();

        imagesDisposable = model.getImagesObservable(imagesCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(next -> toEndScrolled = false)
                .subscribe(this::onNextImagesLoaded, this::onErrorOccurred);
    }

    protected void onNextImagesLoaded(List<T> images) {
        if (imagesDisposable != null && !imagesDisposable.isDisposed()) imagesDisposable.dispose();
        getViewState().addImages(images);
        getViewState().hideLoading();
        getViewState().showContent();
        imagesCount += images.size();
        checkForEmpty();
    }

    protected void onImagesDeleted(List<T> images) {
        getViewState().removeImages(images);
        imagesCount -= images.size();
        checkForEmpty();
    }

    protected void checkForEmpty() {
        if (imagesCount == 0) getViewState().showEmptyMessage();
        else getViewState().hideEmptyMessage();
    }

    private void onErrorOccurred(Throwable error) {
        Logger.e(error, error.getMessage());
    }

}
