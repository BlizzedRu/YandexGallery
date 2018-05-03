package ru.blizzed.yandexgallery.ui.screens.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ru.blizzed.yandexgallery.data.model.Section;
import ru.blizzed.yandexgallery.data.model.URLImage;
import ru.blizzed.yandexgallery.di.components.RepositoriesComponent;
import ru.blizzed.yandexgallery.ui.mvp.DiMvpFragment;

public class FavoritePage extends DiMvpFragment implements FavoriteContract.View {

    public static final String TAG = "favorite";

    @BindView(R.id.sectionsRecycler)
    RecyclerView sectionsRecycler;

    @Inject
    @InjectPresenter
    FavoritePresenter presenter;

    private SectionsFavoriteViewAdapter sectionsAdapter;

    private Unbinder unbinder;
    private List<Section<URLImage>> sections;

    @Override
    protected void buildDiComponent(RepositoriesComponent repositoriesComponent) {
        DaggerFavoriteScreenComponent.builder()
                .repositoriesComponent(repositoriesComponent)
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sections = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_favorite, container, false);

        unbinder = ButterKnife.bind(this, view);

        sectionsAdapter = new SectionsFavoriteViewAdapter(sections);
        sectionsRecycler.setAdapter(sectionsAdapter);
        sectionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @ProvidePresenter
    FavoritePresenter providePresenter() {
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
    public void addSection(Section<URLImage> section) {
        sections.add(section);
        sectionsAdapter.notifyItemInserted(sections.size() - 1);
    }

    @Override
    public void addSections(List<Section<URLImage>> sections) {
        this.sections.addAll(sections);
        sectionsAdapter.notifyItemRangeInserted(this.sections.size(), sections.size());
    }

    @Override
    public void removeSection(Section<URLImage> section) {
        int ind = sections.indexOf(section);
        if (ind > -1) {
            sections.remove(section);
            sectionsAdapter.notifyItemRemoved(ind);
        }
    }

    @Override
    public void updateSection(Section<URLImage> section) {
        int ind = sections.indexOf(section);
        if (ind > -1) {
            sections.set(ind, section);
            sectionsAdapter.notifyItemChanged(ind);
        }
    }

    @Override
    public void showRemovingNotification() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
