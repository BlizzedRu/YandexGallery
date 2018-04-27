package ru.blizzed.yandexgallery;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import ru.blizzed.yandexgallery.ui.screens.feed.FeedPage;
import ru.blizzed.yandexgallery.ui.screens.files.FilesPage;

public class MainActivity extends AppCompatActivity {

    private FilesPage filesPage = new FilesPage();
    private FeedPage feedPage = new FeedPage();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_feed:
                getFragmentManager().beginTransaction().replace(R.id.pagesContainer, feedPage).commit();
                return true;
            case R.id.navigation_files:
                getFragmentManager().beginTransaction().replace(R.id.pagesContainer, filesPage).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_feed);
    }

}
