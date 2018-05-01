package ru.blizzed.yandexgallery;

import android.app.Application;
import android.arch.persistence.room.Room;

public class App extends Application {

    public static App instance;

    private GalleryDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, GalleryDatabase.class, "gallery").build();
    }

    public GalleryDatabase getDatabase() {
        return database;
    }

}
