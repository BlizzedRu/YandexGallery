package ru.blizzed.yandexgallery.ui.screens.files;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FilesPage extends DiMvpFragment implements FilesContract.View {

    public static final String TAG = "files";

    @BindView(R.id.foldersRecycler)
    RecyclerView foldersRecycler;

    @Inject
    @InjectPresenter
    FilesPresenter presenter;

    private ImagesFolderViewAdapter folderAdapter;

    private List<FileImagesFolder> folderList;

    private Unbinder unbinder;

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        DaggerFilesScreenComponent.builder()
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    public void hideContent() {

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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private int getSpanCount() {
        return getResources().getInteger(OrientationUtils.get(getActivity()) == OrientationUtils.Orientation.VERTICAL
                ? R.integer.folder_spans
                : R.integer.folder_spans_horizontal
        );
    }
}
