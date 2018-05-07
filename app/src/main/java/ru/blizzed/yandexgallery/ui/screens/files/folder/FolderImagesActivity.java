package ru.blizzed.yandexgallery.ui.screens.files.folder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;

public class FolderImagesActivity extends Activity {

    public static final String KEY_FOLDER = "folder";

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

        Fragment fragment = getFragmentManager().findFragmentByTag(FileImagesFragment.TAG);
        if (fragment == null) {
            fragment = FileImagesFragment.newInstance(folder);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, FileImagesFragment.TAG)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
