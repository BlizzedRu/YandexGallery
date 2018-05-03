package ru.blizzed.yandexgallery.ui.screens.feed.category;

import com.arellomobile.mvp.InjectViewState;

import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListPresenter;

@InjectViewState
public class CategoryImagesPresenter extends EndlessImageListPresenter<URLImage> {

    public CategoryImagesPresenter(EndlessImageListContract.Model<URLImage> repository) {
        super(repository);
    }

}
