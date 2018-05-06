package ru.blizzed.yandexgallery.ui.screens.fullscreenimage;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.Image;
import ru.blizzed.yandexgallery.ui.ImageLoader;
import ru.blizzed.yandexgallery.ui.customs.ButtonsMenuView;
import ru.blizzed.yandexgallery.ui.customs.ImageViewPager;
import ru.blizzed.yandexgallery.ui.customs.flickableimageview.FlickableImageView;
import ru.blizzed.yandexgallery.utils.PermissionsUtils;

public abstract class FullScreenImageDialogFragment<T extends Image> extends DialogFragment implements ButtonsMenuView.OnItemClickListener, ViewPager.OnPageChangeListener {

    public static final String KEY_IMAGES = "images";
    public static final String KEY_POSITION = "position";
    public static final String KEY_REQUEST_CODE = "request_code";

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnCloseListener) {
            ((OnCloseListener) parentFragment).onClose(this, position);
        }
    }

    private static final int PERMISSIONS_SETTINGS_REQUEST_CODE = 808;
    protected static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    protected List<T> images;
    protected FullScreenImagePagerAdapter<T> adapter;
    protected int position;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.downMenu)
    ButtonsMenuView downMenu;

    @BindView(R.id.pager)
    ImageViewPager viewPager;

    private View view;

    private Unbinder unbinder;
    private boolean isToolbarVisible = true;

    private List<Disposable> disposables;

    private FullScreenImagePagerAdapter.OnImageListener<T> listener = new FullScreenImagePagerAdapter.OnImageListener<T>() {
        @Override
        public void onImageClicked(int position, T image) {
            FullScreenImageDialogFragment.this.onImageClicked(position, image);
        }

        @Override
        public void onImageDoubleClicked(FlickableImageView imageView) {
            viewPager.setSwiftEnabled(imageView.getScale() > 1.2f);
        }

        @Override
        public void onStartZoom() {
            viewPager.setSwiftEnabled(false);
        }

        @Override
        public void onZoomBackToMinScale() {
            viewPager.setSwiftEnabled(true);
        }

        @Override
        public void onStartFlick() {
        }

        @Override
        public void onFinishFlick() {
            dismiss();
        }

        @Override
        public View getRoot() {
            return view;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_fullscreen, container, false);
        viewPager = view.findViewById(R.id.pager);

        images = getArguments().getParcelableArrayList(KEY_IMAGES);
        position = getArguments().getInt(KEY_POSITION, 0);

        unbinder = ButterKnife.bind(this, view);

        toolbar.setNavigationOnClickListener(v -> this.dismiss());
        toolbar.setNavigationIcon(R.drawable.ic_back);

        updateToolbarTitle();

        adapter = new FullScreenImagePagerAdapter<>(images, provideImageLoader(), listener);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);

        fillDownMenu(downMenu);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        updateToolbarTitle();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void onImageClicked(int position, T image) {
        YoYo.with(isToolbarVisible ? Techniques.FadeOutUp : Techniques.FadeInDown).duration(175).playOn(toolbar);
        YoYo.with(isToolbarVisible ? Techniques.FadeOutDown : Techniques.FadeInUp).duration(175).playOn(downMenu);
        isToolbarVisible = !isToolbarVisible;
    }

    protected void updateToolbarTitle() {
        toolbar.setTitle(getString(R.string.full_screen_image_title, position + 1, images.size()));
    }

    protected abstract ImageLoader<T> provideImageLoader();

    @MenuRes
    protected abstract int getDownMenuRes();

    protected T getCurrentImage() {
        return images.get(position);
    }

    protected Intent getShareIntent(String pathToImage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        shareIntent.setType("image/*");

        Uri uri = FileProvider.getUriForFile(
                getContext(),
                getContext().getPackageName() + ".provider",
                new File(pathToImage)
        );
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }

    protected void openPermissionsSettings() {
        PermissionsUtils.openPermissionAppSettings(this, PERMISSIONS_SETTINGS_REQUEST_CODE);
    }

    protected boolean hasPermission(String permission) {
        return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
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

    protected Snackbar createSnackbar(@StringRes int text) {
        return createSnackbar(getString(text));
    }

    protected Snackbar createSnackbar(String text) {
        return Snackbar.make(viewPager, text, Snackbar.LENGTH_SHORT);
    }

    protected Snackbar createPermissionSettingsSnackbar(@StringRes int message) {
        return createSnackbar(message)
                .setDuration(Snackbar.LENGTH_LONG)
                .setAction(R.string.image_menu_delete_error_permissions_button, v -> openPermissionsSettings());
    }

    protected void addDisposable(Disposable disposable) {
        if (disposables == null)
            disposables = new ArrayList<>();
        disposables.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if (disposables != null)
            disposables.remove(disposable);
    }

    protected void dispose() {
        if (disposables != null) {
            Observable.fromIterable(disposables).forEach(Disposable::dispose);
            disposables.clear();
        }
    }

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

    public interface OnCloseListener {
        void onClose(DialogFragment dialogFragment, int position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        dispose();
    }
}
