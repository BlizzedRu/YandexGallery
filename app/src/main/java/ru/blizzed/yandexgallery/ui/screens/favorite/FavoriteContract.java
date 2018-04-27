package ru.blizzed.yandexgallery.ui.screens.favorite;

import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface FavoriteContract extends BaseContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void addSection();

        void removeSection();

        void addFavorite();

        void removeFavorite();
    }

    interface Presenter extends BasePresenter {

    }

}
