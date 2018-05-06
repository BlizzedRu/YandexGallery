package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListContract;
import ru.blizzed.yandexgallery.ui.screens.endlessimagelist.EndlessImageListFragment;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.activity.FullScreenFileImageActivity;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.activity.FullScreenImageActivity;
import ru.blizzed.yandexgallery.ui.screens.fullscreenimage.dialogfragment.FullScreenFileImageDialogFragment;

import static android.app.Activity.RESULT_OK;

public class FileImagesFragment extends EndlessImageListFragment<FileImage> implements EndlessImageListContract.View<FileImage>, OnFileImageRemovedListener {

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

    @Override
    public FolderImagesPresenter getPresenter() {
        return presenter;
    }

    @ProvidePresenter
    public FolderImagesPresenter providePresenter() {
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
    public void openImage(FileImage image) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(FullScreenImageActivity.KEY_IMAGES, getImages());
        args.putInt(FullScreenImageActivity.KEY_POSITION, getImages().indexOf(image));
        args.putInt(FullScreenImageActivity.KEY_REQUEST_CODE, FULL_SCREEN_REQUEST_CODE);

        DialogFragment dialog = new FullScreenFileImageDialogFragment();
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), "fullscreen");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FULL_SCREEN_REQUEST_CODE) {
                ArrayList<FileImage> removedImages = data.getParcelableArrayListExtra(FullScreenFileImageActivity.KEY_REMOVED);
                if (removedImages != null && !removedImages.isEmpty()) {
                    getPresenter().onImagesRemoved(removedImages);
                }
            }
        }
    }
}
