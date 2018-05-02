package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.ButtonsMenuView;

public abstract class FullScreenImageFragment<T extends Image> extends DialogFragment implements ButtonsMenuView.OnItemClickListener {

    public static final String KEY_IMAGES = "images";
    public static final String KEY_POSITION = "position";

    private List<T> images;
    private int position;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.downMenu)
    ButtonsMenuView downMenu;

    private Unbinder unbinder;

    private boolean isToolbarVisible = true;

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

        unbinder = ButterKnife.bind(this, view);

        toolbar.setNavigationOnClickListener(v -> this.dismiss());
        toolbar.setNavigationIcon(R.drawable.ic_back);

        updateToolbarTitle(toolbar, position, images.size());

        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImagePagerAdapter<>(images, provideImageLoader(), this::onImageClicked));
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

        fillDownMenu(downMenu);

        return view;
    }

    public void onImageClicked(int position, T image) {
        YoYo.with(isToolbarVisible ? Techniques.FadeOutUp : Techniques.FadeInDown).duration(175).playOn(toolbar);
        YoYo.with(isToolbarVisible ? Techniques.FadeOutDown : Techniques.FadeInUp).duration(175).playOn(downMenu);
        isToolbarVisible = !isToolbarVisible;
    }

    protected abstract ImageLoader<T> provideImageLoader();

    @MenuRes
    protected abstract int getDownMenuRes();

    private void fillDownMenu(ButtonsMenuView downMenu) {
        PopupMenu p = new PopupMenu(getActivity(), null);
        Menu menu = p.getMenu();
        getActivity().getMenuInflater().inflate(getDownMenuRes(), menu);
        MenuItem[] items = new MenuItem[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            items[i] = menu.getItem(i);
        }

        downMenu.setItems(items);
        downMenu.setOnItemClickListener(this);
    }

    private void updateToolbarTitle(Toolbar toolbar, int position, int amount) {
        toolbar.setTitle(getString(R.string.full_screen_image_title, position + 1, amount));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
