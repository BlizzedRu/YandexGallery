package ru.blizzed.yandexgallery.ui.customs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ImageViewPager extends ViewPager {

    private boolean swiftEnabled = true;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swiftEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swiftEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSwiftEnabled(boolean enabled) {
        this.swiftEnabled = enabled;
    }


}
