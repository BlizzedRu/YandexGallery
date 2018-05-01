package ru.blizzed.yandexgallery.ui.screens.favorite;

import com.arellomobile.mvp.InjectViewState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.favorite.model.Section;

@InjectViewState
public class FavoritePresenter extends FavoriteContract.BasePresenterImpl<FavoriteContract.View> implements FavoriteContract.Presenter {

    private FavoriteContract.Model repository;

    private Disposable imagesDisposable;

    public FavoritePresenter(FavoriteContract.Model repository) {
        this.repository = repository;

    }

    private boolean isWithinOneDay(Date d1, Date d2) {
        return d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDay() == d2.getDay();
    }

    @Override
    public void favoriteRemoved(URLImage image) {

    }

    @Override
    public void favoriteRemoveUndo(URLImage image) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imagesDisposable != null)
            imagesDisposable.dispose();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        imagesDisposable = repository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(list -> {
                    List<Section<URLImage>> sections = new ArrayList<>();

                    List<URLImage> images = Observable.fromIterable(list)
                            .sorted((o1, o2) -> Long.compare(o1.getTimestamp(), o2.getTimestamp()))
                            .toList()
                            .blockingGet();

                    Date today = new Date(System.currentTimeMillis());
                    Section<URLImage> currentSection = null;

                    for (URLImage image : images) {
                        Date date = new Date(image.getTimestamp());
                        if (currentSection == null || isWithinOneDay(date, today)) {
                            currentSection = new Section<>(new SimpleDateFormat("dd-MM-yyyy").format(image.getTimestamp()));
                            sections.add(currentSection);
                        }
                        currentSection.getItems().add(image);
                    }

                    getViewState().addSections(sections);
                });
    }
}
