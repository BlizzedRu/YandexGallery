package ru.blizzed.yandexgallery.ui.screens.feed.category;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListPresenter;

@InjectViewState
public class CategoryImagesPresenter extends EndlessImageListPresenter<URLImage> implements CategoryImagesContract.Presenter {

    public CategoryImagesPresenter(CategoryImagesContract.Model model) {
        super(model);

        model.subscribeToFavorites(new Observer<FavoriteImageEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(FavoriteImageEvent favoriteImageEvent) {
                ((CategoryImagesContract.View) getViewState()).onImageFavoriteStateChanged(
                        favoriteImageEvent.getImage(),
                        favoriteImageEvent.getType() == FavoriteImageEvent.Type.ADDED
                );
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

}
