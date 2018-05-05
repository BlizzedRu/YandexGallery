package ru.blizzed.yandexgallery.data.model;

public class FavoriteImageEvent {

    private URLImage image;
    private Type type;

    public FavoriteImageEvent(URLImage image, Type type) {
        this.image = image;
        this.type = type;
    }

    public URLImage getImage() {
        return image;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ADDED, REMOVED
    }

}
