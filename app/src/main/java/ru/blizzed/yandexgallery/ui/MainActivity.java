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

    private FavoritePage favoritePage = new FavoritePage();
    private FilesPage filesPage = new FilesPage();
    private FeedPage feedPage = new FeedPage();

    private String currentFragmentTag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_feed:
                openFragment(feedPage, FeedPage.TAG);
                return true;
            case R.id.navigation_files:
                openFragment(filesPage, FilesPage.TAG);
                return true;
            case R.id.navigation_favorite:
                openFragment(favoritePage, FavoritePage.TAG);
                return true;
        }
        return false;
    };

    private void openFragment(Fragment fragment, String tag) {
        if (tag.equals(currentFragmentTag)) return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (currentFragmentTag != null) {
            transaction.hide(getFragmentManager().findFragmentByTag(currentFragmentTag));
            if (getFragmentManager().findFragmentByTag(tag) == null)
                transaction.add(R.id.pagesContainer, fragment, tag);
            else transaction.show(fragment);
        } else {
            transaction.add(R.id.pagesContainer, fragment, tag);
        }

        currentFragmentTag = tag;

        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_feed);

    }

}
