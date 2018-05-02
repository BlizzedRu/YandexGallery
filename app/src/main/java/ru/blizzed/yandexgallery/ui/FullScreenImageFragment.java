package ru.blizzed.yandexgallery.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureFrameLayout;

import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.model.Image;

public abstract class FullScreenImageFragment<T extends Image> extends DialogFragment {

    public static final String KEY_IMAGE = "image";

    private T image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoTitleBar);
        image = getArguments().getParcelable(KEY_IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_fullscreen, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> this.dismiss());

        ImageView img = view.findViewById(R.id.img);

        loadImage(img, image);

        GestureFrameLayout gestureLayout = view.findViewById(R.id.gestureLayout);
        gestureLayout.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
            @Override
            public void onDown(@NonNull MotionEvent event) {

            }

            @Override
            public void onUpOrCancel(@NonNull MotionEvent event) {
                FullScreenImageFragment.this.dismiss();
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent event) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent event) {

            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent event) {
                return false;
            }
        });

        return view;
    }

    protected abstract void loadImage(ImageView imageView, T image);

}
