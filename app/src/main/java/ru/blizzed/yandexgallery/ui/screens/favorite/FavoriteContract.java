package ru.blizzed.yandexgallery.ui.screens.favorite;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;
import ru.blizzed.yandexgallery.ui.screens.favorite.model.Section;

public interface FavoriteContract extends BaseContract {

    interface Model extends BaseModel {
        Observable<URLImage> getObservable();

        Flowable<List<URLImage>> getAll();

        void remove(URLImage image);

        void add(URLImage image);
    }

    interface View extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addSections(List<Section<URLImage>> sections);

        void addSection(Section<URLImage> section);

        void removeSection(Section<URLImage> section);

        void updateSection(Section<URLImage> section);

        void showRemovingNotification();
    }

    interface Presenter extends BasePresenter {
        void favoriteRemoved(URLImage image);

        void favoriteRemoveUndo(URLImage image);
    }

}
