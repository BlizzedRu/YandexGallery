package ru.blizzed.yandexgallery.ui.screens.favorite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.blizzed.yandexgallery.data.model.Section;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface FavoriteContract extends BaseContract {

    interface Model extends BaseModel {
        Flowable<List<URLImage>> getAll();

        Completable remove(URLImage image);

        Completable add(URLImage image);
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
