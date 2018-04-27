package ru.blizzed.yandexgallery.ui.customs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ru.blizzed.yandexgallery.R;

public class CategoryView extends AppCompatTextView {

    public CategoryView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CategoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CategoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int padding = context.getResources().getDimensionPixelSize(R.dimen.category_padding);
        setPadding(padding * 2, padding, padding * 2, padding);
        super.setBackgroundDrawable(getContext().getDrawable(R.drawable.view_category_background));

        setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setTextColor(getContext().getResources().getColor(selected ? R.color.pageBackground : R.color.colorAccent));
    }

    // Avoid overriding existing background
    @Override
    public void setBackground(Drawable background) {
    }

    @Override
    public void setBackgroundDrawable(Drawable backgroundDrawable) {
    }

    @Override
    public void setBackgroundResource(int resId) {
    }

}
