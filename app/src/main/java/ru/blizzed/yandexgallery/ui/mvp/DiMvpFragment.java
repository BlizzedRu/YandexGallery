package ru.blizzed.yandexgallery.ui.mvp;

import android.os.Bundle;

import com.arellomobile.mvp.MvpFragment;

import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;

public abstract class DiMvpFragment extends MvpFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        buildDiComponent(App.getRepositoriesComponent());
        super.onCreate(savedInstanceState);
    }

    protected abstract void buildDiComponent(RepositoriesComponent repositoriesComponent);

}
