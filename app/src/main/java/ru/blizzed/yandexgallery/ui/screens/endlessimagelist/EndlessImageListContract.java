package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.yandexgallery.model.URLImage;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface EndlessImageListContract extends BaseContract {

    interface Model extends BaseModel {
        Observable<List<URLImage>> getImagesObservable(int offset);

        boolean hasNextImages();
    }

    interface View extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addImages(List<URLImage> images);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {
        void onDownScrolled(int lastVisibleItemPosition);
    }

}
