package ru.blizzed.yandexgallery.ui.screens.files;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.screens.files.model.FilesImageRepository;
import ru.blizzed.yandexgallery.ui.screens.files.model.ImagesFolder;

public class FilesPage extends Fragment {

    @BindView(R.id.foldersRecycler)
    RecyclerView foldersRecycler;

    private ImagesFolderViewAdapter folderAdapter;

    private FilesImageRepository repository;
    private Disposable repositoryDisposable;

    private List<ImagesFolder> folderList;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new FilesImageRepository();
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

        repositoryDisposable = repository.getImageFolders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(folder -> {
                    folderList.add(folder);
                    folderAdapter.notifyItemInserted(folderList.size() - 1);
                });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        repositoryDisposable.dispose();
    }
}
