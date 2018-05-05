package ru.blizzed.yandexgallery.ui.screens.favorite;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListPresenter;

@InjectViewState
public class FavoritePresenter extends EndlessImageListPresenter<URLImage> {

    public FavoritePresenter(FavoriteImagesModel model) {
        super(model);

        model.subscribe(new Observer<FavoriteImageEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(FavoriteImageEvent favoriteImageEvent) {
                switch (favoriteImageEvent.getType()) {
                    case ADDED:
                        onNextImagesLoaded(Collections.singletonList(favoriteImageEvent.getImage()));
                        break;
                    case REMOVED:
                        onImagesDeleted(Collections.singletonList(favoriteImageEvent.getImage()));
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }
}
