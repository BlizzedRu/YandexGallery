package ru.blizzed.yandexgallery;

import ru.blizzed.pixabaylib.params.CategoryParam;

public class SelectableCategory {
    private CategoryParam.Category instance;
    private boolean selected;

    public SelectableCategory(CategoryParam.Category instance) {
        this.instance = instance;
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public CategoryParam.Category get() {
        return instance;
    }
}
