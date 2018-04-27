package ru.blizzed.yandexgallery.ui.mvp;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

public interface BaseContract {

    interface BaseModel {
    }

    interface BaseView extends MvpView {
    }

    interface BasePresenter {
    }

    abstract class BasePresenterImpl<V extends BaseView> extends MvpPresenter<V> implements BasePresenter {
    }

}
