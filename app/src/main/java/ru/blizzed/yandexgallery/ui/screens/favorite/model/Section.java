package ru.blizzed.yandexgallery.ui.screens.favorite.model;

import java.util.ArrayList;
import java.util.List;

public class Section<T> {

    private String title;

    private List<T> items;

    public Section(String title) {
        this(title, new ArrayList<>());
    }

    public Section(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

}
