package ru.blizzed.yandexgallery.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.screens.favorite.FavoritePage;
import ru.blizzed.yandexgallery.ui.screens.feed.FeedPage;
import ru.blizzed.yandexgallery.ui.screens.files.FilesPage;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_TAG = "fragment_tag";

    private String currentFragmentTag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_feed:
                openFragment(FeedPage.TAG);
                return true;
            case R.id.navigation_files:
                openFragment(FilesPage.TAG);
                return true;
            case R.id.navigation_favorite:
                openFragment(FavoritePage.TAG);
                return true;
        }
        return false;
    };

    private void openFragment(String tag) {
        if (tag.equals(currentFragmentTag)) return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (currentFragmentTag != null) {
            transaction.hide(getFragmentManager().findFragmentByTag(currentFragmentTag));
        }
        transaction.show(getFragmentManager().findFragmentByTag(tag)).commit();

        currentFragmentTag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragments();
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_feed);
    }

    private void addFragments() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        addFragment(new FeedPage(), FeedPage.TAG, transaction);
        addFragment(new FilesPage(), FilesPage.TAG, transaction);
        addFragment(new FavoritePage(), FavoritePage.TAG, transaction);

        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    private void addFragment(Fragment fragment, String tag, FragmentTransaction transaction) {
        transaction.add(R.id.pagesContainer, fragment, tag).hide(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TAG, currentFragmentTag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragmentTag = savedInstanceState.getString(KEY_TAG, null);
    }

}
