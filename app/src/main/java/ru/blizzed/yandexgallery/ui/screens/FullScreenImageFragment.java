package ru.blizzed.yandexgallery.ui.screens;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import ru.blizzed.pixabaylib.model.PixabayImage;
import ru.blizzed.yandexgallery.R;

public class FullScreenImageFragment extends DialogFragment {

    public static final String KEY_IMAGE = "image";

    private PixabayImage image;

    public static FullScreenImageFragment newInstance(PixabayImage image) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_IMAGE, image);
        FullScreenImageFragment fragment = new FullScreenImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoTitleBar);
        image = (PixabayImage) getArguments().getSerializable(KEY_IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_fullscreen, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(image.getType());
        toolbar.setNavigationOnClickListener(v -> this.dismiss());

        Picasso.get()
                .load(image.getLargeImageURL())
                .into((ImageView) view.findViewById(R.id.img));

        return view;
    }
}
