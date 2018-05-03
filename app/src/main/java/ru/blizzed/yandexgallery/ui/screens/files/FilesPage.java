package ru.blizzed.yandexgallery.ui.screens.files;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.data.model.FileImagesFolder;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.mvp.DiMvpFragment;
import ru.blizzed.yandexgallery.ui.screens.files.folder.FolderImagesActivity;
import ru.blizzed.yandexgallery.utils.OrientationUtils;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class FilesPage extends DiMvpFragment implements FilesContract.View {

    public static final String TAG = "files";

    private static final int SETTINGS_REQUEST_CODE = 873;

    @BindView(R.id.foldersRecycler)
    RecyclerView foldersRecycler;

    @BindView(R.id.noPermissions)
    View noPermissionsView;

    @Inject
    @InjectPresenter
    FilesPresenter presenter;

    private ImagesFolderViewAdapter folderAdapter;

    private List<FileImagesFolder> folderList;

    private Unbinder unbinder;

    private int externalStoragePermissionRequestCode;

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        DaggerFilesScreenComponent.builder()
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);

        presenter.setPermissionsHelper(new FilesContract.PermissionsHelper() {
            @Override
            public boolean hasPermissions(String permission) {
                return checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
            }

            @Override
            public boolean canRequestPermissions(String permission) {
                return shouldShowRequestPermissionRationale(permission);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        externalStoragePermissionRequestCode = getResources().getInteger(R.integer.read_external_storage_permission_request_code);
        folderList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_files, container, false);
        unbinder = ButterKnife.bind(this, view);

        folderAdapter = new ImagesFolderViewAdapter(folderList, (position, item) -> presenter.onFolderClicked(item));
        foldersRecycler.setAdapter(folderAdapter);
        foldersRecycler.setLayoutManager(new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL));

        initNoPermissionsView();

        return view;
    }

    @ProvidePresenter
    public FilesPresenter providePresenter() {
        return presenter;
    }

    @Override
    public void showEmptyMessage() {

    }

    @Override
    public void hideEmptyMessage() {

    }

    @Override
    public void showContent() {
        foldersRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        foldersRecycler.setVisibility(View.GONE);

    }

    @Override
    public void addFolder(FileImagesFolder folder) {
        folderList.add(folder);
        folderAdapter.notifyItemInserted(folderList.size() - 1);
    }

    @Override
    public void openFolder(FileImagesFolder folder) {
        Intent intent = new Intent(getActivity(), FolderImagesActivity.class);
        intent.putExtra(FolderImagesActivity.KEY_FOLDER, folder);
        startActivity(intent);
    }

    @Override
    public void showNoPermissionsMessage() {
        noPermissionsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoPermissionsMessage() {
        noPermissionsView.setVisibility(View.GONE);
    }

    @Override
    public void requestPermissions(String permission) {
        requestPermissions(new String[]{permission}, externalStoragePermissionRequestCode);
    }

    @Override
    public void openPermissionAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == externalStoragePermissionRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsGranted();
            } else {
                presenter.onPermissionsDenied();
            }
        }
    }

    /* Catching coming back from Settings (permissions may be changed) */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE) {
            presenter.onFirstViewAttach();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initNoPermissionsView() {
        ((TextView) noPermissionsView.findViewById(R.id.title)).setText(R.string.no_external_storage_permission_title);
        ((TextView) noPermissionsView.findViewById(R.id.description)).setText(R.string.no_external_storage_permission_description);
        ((Button) noPermissionsView.findViewById(R.id.button)).setText(R.string.no_external_storage_permission_button);
        noPermissionsView.findViewById(R.id.button).setOnClickListener(v -> presenter.onPermissionsGrantClicked());
    }

    private int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.folder_spans
                : R.integer.folder_spans_horizontal
        );
    }
}
