package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;

public class FolderImagesActivity extends Activity implements OnFileImageRemovedListener {

    public static final String KEY_FOLDER = "folder";

    private FileImagesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_images);

        FileImagesFolder folder = getIntent().getParcelableExtra(KEY_FOLDER);
        if (folder == null) throw new IllegalArgumentException("Folder flag in intent expected.");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(folder.getTitle());
        }

        fragment = (FileImagesFragment) getFragmentManager().findFragmentByTag(FileImagesFragment.TAG);
        if (fragment == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, FileImagesFragment.newInstance(folder), FileImagesFragment.TAG)
                    .commit();
        }

    }

    @Override
    public void onImageRemoved(FileImage image) {
        if (fragment != null)
            fragment.onImageRemoved(image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
