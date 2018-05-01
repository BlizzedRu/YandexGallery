package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

@InjectViewState
public class EndlessImageListPresenter extends BaseContract.BasePresenterImpl<EndlessImageListContract.View> implements EndlessImageListContract.Presenter {

    private EndlessImageListContract.Model repository;

    private Disposable imagesDisposable;

    private int imagesCount = 0;
    private boolean toEndScrolled = false;

    public EndlessImageListPresenter(EndlessImageListContract.Model repository) {
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

    private void loadMore() {
        if (imagesDisposable != null && !imagesDisposable.isDisposed()) imagesDisposable.dispose();
        getViewState().showLoading();
        imagesDisposable = repository.getImagesObservable(imagesCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(next -> {
                    imagesCount += next.size();
                    getViewState().addImages(next);
                    toEndScrolled = false;
                }, error -> {
                    Log.e("ru.blizzed", error.toString());
                }, () -> {
                    getViewState().hideLoading();
                    Log.e("ru.blizzed", "Completed");
                });
    }

}
