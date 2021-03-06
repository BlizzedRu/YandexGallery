package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface EndlessImageListContract extends BaseContract {

    interface Model<T extends Image> extends BaseModel {
        Observable<List<T>> getImagesObservable(int offset);

        Single<Boolean> hasNextImages(int alreadyLoaded);
    }

    interface View<T extends Image> extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addImages(List<T> images);

        void removeImages(List<T> images);

        void showLoading();

        void hideLoading();

        void showErrorMessage();

        void hideErrorMessage();

        void openImage(T image);

        void closeImage();

        void scrollTo(int position);

    }

    interface Presenter<T extends Image> extends BasePresenter {
        void onDownScrolled(int lastVisibleItemPosition);

        void onImageClosed(int position);

        void onImageClicked(T image);

        void onErrorClicked();

        void onImagesRemoved(List<T> images);
    }

}
