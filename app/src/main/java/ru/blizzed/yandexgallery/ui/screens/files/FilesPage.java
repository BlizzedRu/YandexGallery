package ru.blizzed.yandexgallery.ui.screens.files;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesFolder;
import ru.blizzed.yandexgallery.ui.screens.files.model.FileImagesRepository;

public class FilesPage extends MvpFragment implements FilesContract.View {

    @BindView(R.id.foldersRecycler)
    RecyclerView foldersRecycler;

    @InjectPresenter
    FilesPresenter presenter;

    private ImagesFolderViewAdapter folderAdapter;

    private List<FileImagesFolder> folderList;

    private Unbinder unbinder;

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

        folderAdapter = new ImagesFolderViewAdapter(folderList, (position, item) -> {
        });
        foldersRecycler.setAdapter(folderAdapter);
        foldersRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @ProvidePresenter
    public FilesPresenter providePresenter() {
        return new FilesPresenter(new FileImagesRepository());
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
