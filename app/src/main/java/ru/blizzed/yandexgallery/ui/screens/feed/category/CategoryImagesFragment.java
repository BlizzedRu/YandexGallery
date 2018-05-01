package ru.blizzed.yandexgallery.ui.screens.feed.category;

import ru.blizzed.pixabaylib.params.CategoryParam;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;

public class CategoryImagesFragment extends EndlessImageListFragment {

    private CategoryParam.Category category;

    public static CategoryImagesFragment newInstance(CategoryParam.Category category) {
        CategoryImagesFragment fragment = new CategoryImagesFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    protected EndlessImageListContract.Model provideModel() {
        return new PixabayCategoryImagesRepository(category, 30);
    }

}
