package ru.blizzed.yandexgallery;

import android.app.Activity;
import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import ru.blizzed.yandexgallery.di.components.DaggerRepositoriesComponent;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.di.modules.ContextModule;
import ru.blizzed.yandexgallery.di.modules.DBImagesRepositoryModule;
import ru.blizzed.yandexgallery.di.modules.FileImagesRepositoryModule;
import ru.blizzed.yandexgallery.di.modules.PixabayRepositoryModule;

public class App extends Application implements HasActivityInjector {

    public static App instance;

    private static RepositoriesComponent repositoriesComponent;

    public static App getInstance() {
        return instance;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static RepositoriesComponent getRepositoriesComponent() {
        return repositoriesComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());

        repositoriesComponent = DaggerRepositoriesComponent.builder()
                .contextModule(new ContextModule(this))
                .dBImagesRepositoryModule(new DBImagesRepositoryModule())
                .fileImagesRepositoryModule(new FileImagesRepositoryModule())
                .pixabayRepositoryModule(new PixabayRepositoryModule())
                .build();

        repositoriesComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
