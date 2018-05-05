package ru.blizzed.yandexgallery.ui.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ru.blizzed.yandexgallery.R;

public class ButtonsMenuView extends LinearLayout {

    private OnItemClickListener listener;

    @ColorInt
    private int activeColor;

    @ColorInt
    private int inactiveColor;

    private SparseBooleanArray isActiveArray;
    private SparseIntArray posToResArray;

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
        isActiveArray = new SparseBooleanArray();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ButtonsMenuView, defStyle, 0);
        activeColor = array.getColor(R.styleable.ButtonsMenuView_colorActive, getResources().getColor(R.color.colorAccent));
        inactiveColor = array.getColor(R.styleable.ButtonsMenuView_colorInactive, getResources().getColor(R.color.white));
        array.recycle();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(MenuItem[] items) {
        posToResArray = new SparseIntArray();

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
            posToResArray.put(position, item.getItemId());
            position++;
        }

        invalidate();
    }

    public void setActive(int position, boolean isActive, boolean animate) {
        boolean intention = isActive(position) != isActive;
        if (intention) {
            isActiveArray.put(position, isActive);
            ImageView img = getChildAt(position).findViewById(R.id.img);

            int colorIntention = isActive ? activeColor : inactiveColor;

            if (!animate) {
                setColorTo(img, colorIntention);
                return;
            }

            YoYo.with(Techniques.Pulse).duration(500).onEnd((c) -> {
                setColorTo(img, colorIntention);
            }).playOn(img);
        }
    }

    public boolean isActive(int position) {
        return isActiveArray.get(position);
    }

    public int getPositionOf(@IdRes int idRes) {
        return posToResArray.indexOfValue(idRes);
    }

    private void setColorTo(ImageView imageView, @ColorInt int color) {
        imageView.getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
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
