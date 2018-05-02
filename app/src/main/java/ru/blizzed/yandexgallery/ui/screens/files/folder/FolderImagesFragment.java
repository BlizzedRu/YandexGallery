package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImage;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesFolder;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.FullScreenFileImageFragment;

public class FolderImagesFragment extends EndlessImageListFragment<FileImage> implements EndlessImageListContract.View<FileImage> {

    private static final String KEY_FOLDER = "folder";
    @InjectPresenter
    FolderImagePresenter presenter;
    private FileImagesFolder folder;

    public static FolderImagesFragment newInstance(FileImagesFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOLDER, folder);

        FolderImagesFragment fragment = new FolderImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public FolderImagePresenter getPresenter() {
        return presenter;
    }

    @ProvidePresenter
    public FolderImagePresenter providePresenter() {
        return new FolderImagePresenter(provideModel());
    }

    @Override
    protected EndlessImageListContract.Model<FileImage> provideModel() {
        folder = getArguments().getParcelable(KEY_FOLDER);
        return new FolderImagesRepository(folder);
    }

    @Override
    public void openImage(FileImage image) {
        FullScreenFileImageFragment dialog = FullScreenFileImageFragment.newInstance(getImages(), getImages().indexOf(image));
        dialog.show(getFragmentManager(), "fullscreen_file_image");
    }

}
