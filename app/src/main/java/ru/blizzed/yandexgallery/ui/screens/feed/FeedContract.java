package ru.blizzed.yandexgallery.ui.screens.feed;

import java.util.List;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.SelectableCategory;
import ru.blizzed.yandexgallery.ui.mvp.BaseContract;

public interface FeedContract extends BaseContract {
    interface Model extends BaseContract.BaseModel {
        List<CategoryParam.Category> getCategories();
    }

    interface View extends BaseView {
        void setCategories(List<SelectableCategory> categories);

        void openCategory(SelectableCategory category);

        void selectCategory(int position);

        void deselectCategory(int position);
    }

    interface Presenter extends BasePresenter {
        void onCategorySelected(int position, SelectableCategory category);
    }
}
