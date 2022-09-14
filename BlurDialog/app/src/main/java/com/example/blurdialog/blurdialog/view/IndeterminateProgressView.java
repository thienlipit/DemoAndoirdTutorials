package com.example.blurdialog.blurdialog.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Vincent Woo
 * Date: 2018/4/23
 * Time: 10:18
 */

public class IndeterminateProgressView extends View {
    private int mViewHeight, mViewWidth;
    private int mCenterX, mCenterY;
    private float mStrokeWidth = 2;
    private Paint mProgressPaint;
    private RotateAnimation mRotate;

    public IndeterminateProgressView(Context context) {
        this(context, null);
    }

    public IndeterminateProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndeterminateProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        parseAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndeterminateProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        parseAttrs(context, attrs);
    }

    private void init() {
        mProgressPaint = new Paint();
        mProgressPaint.setShader(new SweepGradient(mCenterX, mCenterY,
                Color.parseColor("#00000000"), Color.parseColor("#FF47494D")));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);//防抖动
        mProgressPaint.setFilterBitmap(true);//如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示速度，本设置项依赖于dither和xfermode的设置
        mProgressPaint.setStrokeWidth(mStrokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mRotate = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotate.setInterpolator(new LinearInterpolator());
        mRotate.setDuration(1500);//设置动画持续时间
        mRotate.setRepeatCount(-1);//设置重复次数
//                rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimation();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - mStrokeWidth, mProgressPaint);
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float px) {
        this.mStrokeWidth = px;
    }

    public void startAnimation() {
        if (mRotate != null) {
            IndeterminateProgressView.this.startAnimation(mRotate);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (View.VISIBLE == visibility) {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mRotate.reset();
        super.onDetachedFromWindow();
    }
}
