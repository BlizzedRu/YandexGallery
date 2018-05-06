package ru.blizzed.yandexgallery.ui.screens.endlessimagelist;

import android.app.DialogFragment;
import android.content.DialogInterface;

import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenURLImageDialogFragment;

public abstract class EndlessURLImageListFragment extends EndlessImageListFragment<URLImage> implements DialogInterface.OnDismissListener {

    @Override
    protected ImageLoader<URLImage> provideImageLoader() {
        return ImageLoader.URL_IMAGE_PREVIEW;
    }

    @Override
    protected DialogFragment provideFullScreenDialogFragment() {
        return new FullScreenURLImageDialogFragment();
    }
}
