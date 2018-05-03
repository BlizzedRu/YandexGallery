package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.blizzed.yandexgallery.data.model.FileImage;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenFileImageActivity;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenImageActivity;

public class FolderImagesFragment extends EndlessImageListFragment<FileImage> implements EndlessImageListContract.View<FileImage> {

    private static final String KEY_FOLDER = "folder";

    @Inject
    @InjectPresenter
    FolderImagePresenter presenter;

    public static FolderImagesFragment newInstance(FileImagesFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOLDER, folder);

        FolderImagesFragment fragment = new FolderImagesFragment();
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

    @Override
    public FolderImagePresenter getPresenter() {
        return presenter;
    }

    @ProvidePresenter
    public FolderImagePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected ImageLoader<FileImage> provideImageLoader() {
        return ImageLoader.FILE_IMAGE;
    }

    @Override
    public void openImage(FileImage image) {
        Intent intent = new Intent(getActivity(), FullScreenFileImageActivity.class);
        intent.putExtra(FullScreenImageActivity.KEY_IMAGES, getImages());
        intent.putExtra(FullScreenImageActivity.KEY_POSITION, getImages().indexOf(image));
        intent.putExtra(FullScreenImageActivity.KEY_REQUEST_CODE, FULL_SCREEN_REQUEST_CODE);
        startActivityForResult(intent, FULL_SCREEN_REQUEST_CODE);
    }

}
