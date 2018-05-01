package ru.blizzed.yandexgallery.ui.screens.feed;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Observable;
import ru.blizzed.yandexgallery.SelectableCategory;

@InjectViewState
public class FeedPresenter extends FeedContract.BasePresenterImpl<FeedContract.View> implements FeedContract.Presenter {

    private List<SelectableCategory> categories;

    private int lastSelectedCategoryPosition = -1;

    public FeedPresenter(FeedContract.Model repository) {
        categories = Observable.fromIterable(repository.getCategories())
                .map(SelectableCategory::new)
                .toList()
                .blockingGet();
        getViewState().setCategories(categories);
        onCategorySelected(0, categories.get(0));
    }

    @Override
    public void onCategorySelected(int position, SelectableCategory category) {
        if (position == lastSelectedCategoryPosition) return;
        category.setSelected(true);
        getViewState().selectCategory(position);

        if (lastSelectedCategoryPosition != -1) {
            categories.get(lastSelectedCategoryPosition).setSelected(false);
            getViewState().deselectCategory(lastSelectedCategoryPosition);
        }

        lastSelectedCategoryPosition = position;

        getViewState().openCategory(category);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
