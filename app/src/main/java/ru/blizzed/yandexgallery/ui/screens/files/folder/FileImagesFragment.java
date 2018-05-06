package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.app.DialogFragment;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenFileImageDialogFragment;

public class FileImagesFragment extends EndlessImageListFragment<FileImage> implements EndlessImageListContract.View<FileImage>, OnFileImageRemovedListener {

    public static final String TAG = "folder_fragment";
    private static final String KEY_FOLDER = "folder";

    @Inject
    @InjectPresenter
    FolderImagesPresenter presenter;

    public static FileImagesFragment newInstance(FileImagesFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOLDER, folder);

        FileImagesFragment fragment = new FileImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        FileImagesFolder folder = getArguments().getParcelable(KEY_FOLDER);
        DaggerFolderScreenComponent.builder()
                .filesFolder(folder)
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);
    }

    @ProvidePresenter
    @Override
    public FolderImagesPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected ImageLoader<FileImage> provideImageLoader() {
        return ImageLoader.FILE_IMAGE;
    }

    @Override
    public void onImageRemoved(FileImage image) {
        //presenter.onImagesRemoved(Collections.singletonList(image));
        getImagesAdapter().notifyDataSetChanged();
    }

    @Override
    protected DialogFragment provideFullScreenDialogFragment() {
        return new FullScreenFileImageDialogFragment();
    }

}
