package ru.blizzed.yandexgallery.ui.customs.flickableimageview;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;


public class FlickableImageView extends ImageViewTouchBase {

    private static final int SNAPBACK_ANIMATION_TIME = 500;
    private static final int DISMISS_ANIMATION_TIME = 300;
    private static final long MIN_FLING_DELTA_TIME = 200;
    public View root;
    protected int mTouchSlop;
    protected int mDoubleTapDirection;
    protected ScaleGestureDetector mScaleDetector;
    protected GestureDetector mGestureDetector;
    protected GestureDetector.OnGestureListener mGestureListener;
    protected ScaleGestureDetector.OnScaleGestureListener mScaleListener;
    protected boolean mDoubleTapEnabled = true;
    protected boolean mScaleEnabled = true;
    protected boolean mScrollEnabled = true;
    private long mPointerUpTime;
    private float mScaleFactor;
    private float mPreviousPositionY;
    private boolean mDragging = false;

    private boolean mScaling = false;

    private OnFlickableImageViewDoubleTapListener mDoubleTapListener;

    private OnFlickableImageViewSingleTapListener mSingleTapListener;

    private OnFlickableImageViewFlickListener mOnFlickListener;

    private OnFlickableImageViewDraggingListener mOnDraggingListener;

    private OnFlickableImageViewZoomListener mOnZoomListener;

    public FlickableImageView(Context context) {
        super(context);
    }

    public FlickableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlickableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mGestureListener = getGestureListener();
        mScaleListener = getScaleListener();

        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        mGestureDetector = new GestureDetector(getContext(), mGestureListener, null, true);
        mDoubleTapDirection = 1;
        setQuickScaleEnabled(false);
    }

    @TargetApi(19)
    @SuppressWarnings("unused")
    public boolean getQuickScaleEnabled() {
        if (Build.VERSION.SDK_INT >= 19) {
            return mScaleDetector.isQuickScaleEnabled();
        }
        return false;
    }

    @TargetApi(19)
    public void setQuickScaleEnabled(boolean value) {
        if (Build.VERSION.SDK_INT >= 19) {
            mScaleDetector.setQuickScaleEnabled(value);
        }
    }

    public void setOnDoubleTapListener(OnFlickableImageViewDoubleTapListener listener) {
        mDoubleTapListener = listener;
    }

    public void setOnSingleTapListener(OnFlickableImageViewSingleTapListener listener) {
        mSingleTapListener = listener;
    }

    public void setOnFlickListener(OnFlickableImageViewFlickListener onFlickListener) {
        this.mOnFlickListener = onFlickListener;
    }

    public void setOnDraggingListener(OnFlickableImageViewDraggingListener onDraggingListener) {
        this.mOnDraggingListener = onDraggingListener;
    }

    public void setOnZoomListener(OnFlickableImageViewZoomListener onZoomListener) {
        this.mOnZoomListener = onZoomListener;
    }

    public void setScaleEnabled(boolean value) {
        mScaleEnabled = value;
    }

    public void setScrollEnabled(boolean value) {
        mScrollEnabled = value;
    }

    public boolean getDoubleTapEnabled() {
        return mDoubleTapEnabled;
    }

    public void setDoubleTapEnabled(boolean value) {
        mDoubleTapEnabled = value;
    }

    protected GestureDetector.OnGestureListener getGestureListener() {
        return new GestureListener();
    }

    protected ScaleGestureDetector.OnScaleGestureListener getScaleListener() {
        return new ScaleListener();
    }

    @Override
    protected void setImageDrawableInternal(final Drawable drawable, final Matrix initialMatrix, float minZoom,
                                            float maxZoom) {
        super.setImageDrawableInternal(drawable, initialMatrix, minZoom, maxZoom);
    }

    @Override
    protected void onLayoutChanged(final int left, final int top, final int right, final int bottom) {
        super.onLayoutChanged(left, top, right, bottom);
        mScaleFactor = ((getMaxScale() - getMinScale()) / 2f) + 0.5f;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getBitmapChanged()) {
            return false;
        }

        final int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_POINTER_UP) {
            mPointerUpTime = event.getEventTime();
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            snapBack();
        }

        mScaleDetector.onTouchEvent(event);

        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }

        switch (action) {
            case MotionEvent.ACTION_UP:
                return onUp(event);
        }
        return true;
    }

    protected float onDoubleTapPost(float scale, final float maxZoom, final float minScale) {
        if ((scale + mScaleFactor) <= maxZoom) {
            return scale + mScaleFactor;
        } else {
            return minScale;
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (!canScroll()) {
            if (mOnDraggingListener != null) {
                if (!mDragging) {
                    mOnDraggingListener.onStartDrag();
                    mDragging = true;
                }
            }

            mPreviousPositionY = e1.getY();
            float currY = e2.getY();

            float deltaY = currY - mPreviousPositionY;
            ViewCompat.setTranslationY(this, ViewCompat.getTranslationY(this) + deltaY);
            return true;
        }

        mUserScaled = true;
        scrollBy(-distanceX, -distanceY);
        invalidate();
        return true;
    }

    public void setRoot(View view) {
        this.root = view;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (!canScroll()) {

            View target = FlickableImageView.this;
            float translationY = target.getHeight() * 2;

            switch (Direction.decide(-ViewCompat.getTranslationY(this))) {
                case UP:
                    if (mOnFlickListener != null) {
                        mOnFlickListener.onStartFlick();
                    }
                    ViewCompat.animate(target)
                            .translationY(-translationY)
                            .setDuration(DISMISS_ANIMATION_TIME)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    if (mOnFlickListener != null) {
                                        mOnFlickListener.onFinishFlick();
                                    }
                                }
                            });

                    ViewCompat.animate(root)
                            .alpha(0.0f)
                            .setDuration(DISMISS_ANIMATION_TIME);

                    break;
                case DOWN:
                    if (mOnFlickListener != null) {
                        mOnFlickListener.onStartFlick();
                    }
                    ViewCompat.animate(target)
                            .translationY(translationY)
                            .setDuration(DISMISS_ANIMATION_TIME)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    if (mOnFlickListener != null) {
                                        mOnFlickListener.onFinishFlick();
                                    }
                                }
                            });

                    ViewCompat.animate(root)
                            .alpha(0.0f)
                            .setDuration(DISMISS_ANIMATION_TIME);
                    break;
            }
            return true;
        }

        if (Math.abs(velocityX) > (mMinFlingVelocity * 4) || Math.abs(velocityY) > (mMinFlingVelocity * 4)) {

            final float scale = Math.min(Math.max(2f, getScale() / 2), 3.f);

            float scaledDistanceX = ((velocityX) / mMaxFlingVelocity) * (getWidth() * scale);
            float scaledDistanceY = ((velocityY) / mMaxFlingVelocity) * (getHeight() * scale);

            mUserScaled = true;

            double total = Math.sqrt(Math.pow(scaledDistanceX, 2) + Math.pow(scaledDistanceY, 2));

            scrollBy(scaledDistanceX, scaledDistanceY, (long) Math.min(Math.max(300, total / 5), 800));

            postInvalidate();
            return true;
        }
        return false;
    }

    public boolean onDown(MotionEvent e) {
        return !getBitmapChanged();
    }

    public boolean onUp(MotionEvent e) {
        if (getBitmapChanged()) {
            return false;
        }
        float minScale = getMinScale();
        if (getScale() < minScale) {
            zoomTo(minScale, 50);
            if (mOnZoomListener != null) {
                if (mScaling) {
                    mOnZoomListener.onBackFromMinScale();
                    mScaling = false;
                }
            }
        }
        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return !getBitmapChanged();
    }

    public boolean canScroll() {
        if (getScale() > 1) {
            return true;
        }
        RectF bitmapRect = getBitmapRect();
        return !mViewPort.contains(bitmapRect);
    }

    private void snapBack() {
        final float currentY = ViewCompat.getY(this);

        ValueAnimator animatorCompat = ValueAnimator.ofFloat(0, 1);
        animatorCompat.setDuration(SNAPBACK_ANIMATION_TIME);
        final Interpolator interpolator = new DecelerateInterpolator();
        animatorCompat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = interpolator.getInterpolation(animation.getAnimatedFraction());
                float interpolatedValue = currentY - (currentY * fraction);
                ViewCompat.setTranslationY(FlickableImageView.this, interpolatedValue);
            }
        });
        animatorCompat.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnDraggingListener != null) {
                    if (mDragging) {
                        mOnDraggingListener.onCancelDrag();
                        mDragging = false;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorCompat.start();
    }

    public enum Direction {
        UP, DOWN;

        public static Direction decide(float value) {
            if (value >= 0) {
                return UP;
            } else {
                return DOWN;
            }
        }
    }

    public interface OnFlickableImageViewDoubleTapListener {

        void onDoubleTap();
    }

    public interface OnFlickableImageViewSingleTapListener {

        void onSingleTapConfirmed();
    }


    public interface OnFlickableImageViewFlickListener {

        void onStartFlick();

        void onFinishFlick();
    }

    public interface OnFlickableImageViewDraggingListener {

        void onStartDrag();

        void onCancelDrag();
    }

    public interface OnFlickableImageViewZoomListener {

        void onStartZoom();

        void onBackFromMinScale();

    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (null != mSingleTapListener) {
                mSingleTapListener.onSingleTapConfirmed();
            }

            return FlickableImageView.this.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mDoubleTapEnabled) {
                if (Build.VERSION.SDK_INT >= 19) {
                    if (mScaleDetector.isQuickScaleEnabled()) {
                        return true;
                    }
                }

                mUserScaled = true;

                float minScale = getMinScale();
                float scale = getScale();
                float targetScale;
                targetScale = onDoubleTapPost(scale, getMaxScale(), minScale);
                targetScale = Math.min(getMaxScale(), Math.max(targetScale, minScale));
                zoomTo(targetScale, e.getX(), e.getY(), mDefaultAnimationDuration);
            }

            if (null != mDoubleTapListener) {
                mDoubleTapListener.onDoubleTap();
            }

            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isLongClickable()) {
                if (!mScaleDetector.isInProgress()) {
                    setPressed(true);
                    performLongClick();
                }
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mScrollEnabled) {
                return false;
            }
            if (e1 == null || e2 == null) {
                return false;
            }
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) {
                return false;
            }
            if (mScaleDetector.isInProgress()) {
                return false;
            }
            return FlickableImageView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!mScrollEnabled) {
                return false;
            }
            if (e1 == null || e2 == null) {
                return false;
            }
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) {
                return false;
            }
            if (mScaleDetector.isInProgress()) {
                return false;
            }

            final long delta = (SystemClock.uptimeMillis() - mPointerUpTime);
            if (delta > MIN_FLING_DELTA_TIME) {
                return FlickableImageView.this.onFling(e1, e2, velocityX, velocityY);
            } else {
                return false;
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return FlickableImageView.this.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            stopAllAnimations();

            return FlickableImageView.this.onDown(e);
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        protected boolean mScaled = false;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float span = detector.getCurrentSpan() - detector.getPreviousSpan();
            float targetScale = getScale() * detector.getScaleFactor();

            if (mScaleEnabled) {
                if (mOnZoomListener != null) {
                    if (!mScaling) {
                        mOnZoomListener.onStartZoom();
                        mScaling = true;
                    }
                }

                if (mScaled && span != 0) {
                    mUserScaled = true;
                    targetScale = Math.min(getMaxScale(), Math.max(targetScale, getMinScale() - 0.1f));
                    zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
                    mDoubleTapDirection = 1;
                    invalidate();
                    return true;
                }

                if (!mScaled) {
                    mScaled = true;
                }
            }
            return true;
        }

    }

}