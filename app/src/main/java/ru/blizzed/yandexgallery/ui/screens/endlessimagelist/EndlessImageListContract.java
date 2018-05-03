package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface EndlessImageListContract extends BaseContract {

    interface Model<T extends Image> extends BaseModel {
        Observable<List<T>> getImagesObservable(int offset);

        boolean hasNextImages();
    }

    interface View<T extends Image> extends BaseView {
        void showEmptyMessage();

        void hideEmptyMessage();

        void showContent();

        void hideContent();

        void addImages(List<T> images);

        void showLoading();

        void hideLoading();

        void openImage(T image);
    }

    interface Presenter<T extends Image> extends BasePresenter {
        void onDownScrolled(int lastVisibleItemPosition);

        void onImageClicked(T image);
    }

}
