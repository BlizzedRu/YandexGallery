package ru.blizzed.yandexgallery.ui.customs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.blizzed.yandexgallery.R;

public class ButtonsMenuView extends LinearLayout {

    private OnItemClickListener listener;

    public ButtonsMenuView(@NonNull Context context) {
        super(context);
        init(context, null, -1);
    }

    public ButtonsMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public ButtonsMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(MenuItem[] items) {
        if (getChildCount() > 0) {
            removeAllViews();
        }

        setWeightSum(items.length);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        int position = 0;
        for (MenuItem item : items) {
            View itemView = inflater.inflate(R.layout.item_buttons_menu, this, false);
            ((ImageView) itemView.findViewById(R.id.img)).setImageDrawable(item.getIcon());
            setListenerTo(itemView, item, position);
            addView(itemView);
            position++;
        }

        invalidate();
    }

    private void setListenerTo(View view, MenuItem item, int position) {
        view.setOnClickListener(v -> {
            if (listener != null) listener.onMenuItemClicked(item, position);
        });
    }

    public interface OnItemClickListener {
        void onMenuItemClicked(MenuItem item, int position);
    }

}
