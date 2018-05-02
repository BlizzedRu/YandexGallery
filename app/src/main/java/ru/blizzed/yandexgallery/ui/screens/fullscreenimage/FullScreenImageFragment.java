package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;

public abstract class FullScreenImageFragment<T extends Image> extends DialogFragment {

    public static final String KEY_IMAGES = "images";
    public static final String KEY_POSITION = "position";

    private List<T> images;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreen);
        images = getArguments().getParcelableArrayList(KEY_IMAGES);
        position = getArguments().getInt(KEY_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_fullscreen, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> this.dismiss());
        toolbar.setNavigationIcon(R.drawable.ic_back);

        updateToolbarTitle(toolbar, position, images.size());

        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImagePagerAdapter<>(images, provideImageLoader()));
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateToolbarTitle(toolbar, position, images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    protected abstract ImageLoader<T> provideImageLoader();

    private void updateToolbarTitle(Toolbar toolbar, int position, int amount) {
        toolbar.setTitle(getString(R.string.full_screen_image_title, position + 1, amount));
    }

}
