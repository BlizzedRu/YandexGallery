package ru.blizzed.yandexgallery.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.blizzed.yandexgallery.di.AppContext;
import ru.blizzed.yandexgallery.di.YandexGalleryScope;

@AppContext
@Module
public class ContextModule {

    @AppContext
    private Context context;

    public ContextModule(@AppContext Context context) {
        this.context = context;
    }

    @AppContext
    @YandexGalleryScope
    @Provides
    Context provideContext() {
        return context;
    }

}