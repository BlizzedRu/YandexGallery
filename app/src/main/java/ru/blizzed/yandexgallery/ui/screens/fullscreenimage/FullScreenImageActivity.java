package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.ButtonsMenuView;
import ru.blizzed.yandexgallery.utils.PermissionsUtils;

public abstract class FullScreenImageActivity<T extends Image> extends Activity implements ButtonsMenuView.OnItemClickListener {

    public static final String KEY_IMAGES = "images";
    public static final String KEY_POSITION = "position";
    public static final String KEY_REQUEST_CODE = "request_code";

    private static final int PERMISSIONS_SETTINGS_REQUEST_CODE = 808;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.downMenu)
    ButtonsMenuView downMenu;
    protected List<T> images;
    protected FullScreenImagePagerAdapter<T> adapter;
    protected int position;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private Unbinder unbinder;

    private boolean isToolbarVisible = true;
    private Intent resultIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);
        images = getIntent().getParcelableArrayListExtra(KEY_IMAGES);
        position = getIntent().getIntExtra(KEY_POSITION, 0);

        unbinder = ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> this.finish());
        toolbar.setNavigationIcon(R.drawable.ic_back);

        updateToolbarTitle();

        adapter = new FullScreenImagePagerAdapter<>(images, provideImageLoader(), this::onImageClicked);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FullScreenImageActivity.this.position = position;
                updateToolbarTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        fillDownMenu(downMenu);

    }

    public void onImageClicked(int position, T image) {
        YoYo.with(isToolbarVisible ? Techniques.FadeOutUp : Techniques.FadeInDown).duration(175).playOn(toolbar);
        YoYo.with(isToolbarVisible ? Techniques.FadeOutDown : Techniques.FadeInUp).duration(175).playOn(downMenu);
        isToolbarVisible = !isToolbarVisible;
    }

    protected abstract ImageLoader<T> provideImageLoader();

    @MenuRes
    protected abstract int getDownMenuRes();

    protected T getCurrentImage() {
        return images.get(position);
    }

    private void fillDownMenu(ButtonsMenuView downMenu) {
        PopupMenu p = new PopupMenu(this, null);
        Menu menu = p.getMenu();
        getMenuInflater().inflate(getDownMenuRes(), menu);
        MenuItem[] items = new MenuItem[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            items[i] = menu.getItem(i);
        }

        downMenu.setItems(items);
        downMenu.setOnItemClickListener(this);
    }

    private void updateToolbarTitle() {
        toolbar.setTitle(getString(R.string.full_screen_image_title, position + 1, images.size()));
    }

    protected Intent getShareIntent(String pathToImage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        shareIntent.setType("image/*");

        Uri uri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".provider",
                new File(pathToImage)
        );
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }

    protected void openPermissionsSettings() {
        PermissionsUtils.openPermissionAppSettings(this, PERMISSIONS_SETTINGS_REQUEST_CODE);
    }

    protected boolean hasPermission(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected boolean canRequestPermissions(String permission) {
        return shouldShowRequestPermissionRationale(permission);
    }

    protected void requestPermission(String permission) {
        requestPermissions(new String[]{permission}, getPermissionRequestCode());
    }

    protected int getPermissionRequestCode() {
        return getResources().getInteger(R.integer.write_external_storage_permission_request_code);
    }

    protected Snackbar getSnackbar(@StringRes int text) {
        return Snackbar.make(viewPager, text, Snackbar.LENGTH_SHORT);
    }

    protected void addToResultIntent(Intent extras) {
        if (resultIntent == null)
            resultIntent = new Intent();
        resultIntent.putExtras(extras);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra(KEY_REQUEST_CODE, getIntent().getIntExtra(KEY_REQUEST_CODE, -1));
        addToResultIntent(intent);
        setResult(RESULT_OK, resultIntent);
        super.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
