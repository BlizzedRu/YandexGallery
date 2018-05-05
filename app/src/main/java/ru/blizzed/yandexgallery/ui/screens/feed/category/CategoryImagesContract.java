package ru.blizzed.yandexgallery.ui.screens.feed.category;

import io.reactivex.Observer;
import ru.blizzed.yandexgallery.data.model.FavoriteImageEvent;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;

public interface CategoryImagesContract extends EndlessImageListContract {

    interface Model extends EndlessImageListContract.Model<URLImage> {
        void subscribeToFavorites(Observer<FavoriteImageEvent> eventsObserver);
    }

    interface View extends EndlessImageListContract.View<URLImage> {
        void onImageFavoriteStateChanged(URLImage image, boolean isFavorite);
    }

    interface Presenter extends EndlessImageListContract.Presenter<URLImage> {
    }

}
