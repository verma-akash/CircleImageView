package com.gravity.canvasdraw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Akash Verma on 04/05/18.
 */

public class CircleImageView extends View {

    private Context mContext;
    private Paint paintOuterCircle;
    private Paint paintArc;
    private Paint paintText;
    private Paint otherPaintText;
    private Rect clipRect;
    private Rect otherTextRect;
    private Bitmap backgroundBitmap;
    private Rect imgSrcRect;
    private Rect imgDestRect;
    private Bitmap middleBitmap;
    private Rect imgSrcRect2;
    private Rect imgDestRect2;
    private Path circlePath;

    private float strokeWidth = 0f;
    private int strokeColor;
    private Drawable imageSrc;
    private Drawable backgroundSrc;
    private boolean enableBadge;
    private int badgeColor;
    private String bannerText;
    private int textColor;
    private String otherText;
    private int otherTextColor;

    public CircleImageView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);
        strokeWidth = attributes.getDimension(R.styleable.CircleImageView_outerStrokeWidth, 0f);
        strokeColor = attributes.getColor(R.styleable.CircleImageView_outerCircleColor, getResources().getColor(android.R.color.black));
        backgroundSrc = attributes.getDrawable(R.styleable.CircleImageView_backgroundSrc);
        enableBadge = attributes.getBoolean(R.styleable.CircleImageView_enableBadge, false);
        badgeColor = attributes.getColor(R.styleable.CircleImageView_badgeColor, getResources().getColor(android.R.color.black));
        bannerText = attributes.getString(R.styleable.CircleImageView_text);
        textColor = attributes.getColor(R.styleable.CircleImageView_textColor, getResources().getColor(android.R.color.white));
        otherText = attributes.getString(R.styleable.CircleImageView_text2);
        otherTextColor = attributes.getColor(R.styleable.CircleImageView_text2Color, getResources().getColor(android.R.color.black));
        imageSrc = attributes.getDrawable(R.styleable.CircleImageView_imageSrc);
        attributes.recycle();
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);
        strokeWidth = attributes.getDimension(R.styleable.CircleImageView_outerStrokeWidth, 0f);
        strokeColor = attributes.getColor(R.styleable.CircleImageView_outerCircleColor, getResources().getColor(android.R.color.black));
        backgroundSrc = attributes.getDrawable(R.styleable.CircleImageView_backgroundSrc);
        enableBadge = attributes.getBoolean(R.styleable.CircleImageView_enableBadge, false);
        badgeColor = attributes.getColor(R.styleable.CircleImageView_badgeColor, getResources().getColor(android.R.color.black));
        bannerText = attributes.getString(R.styleable.CircleImageView_text);
        textColor = attributes.getColor(R.styleable.CircleImageView_textColor, getResources().getColor(android.R.color.white));
        otherText = attributes.getString(R.styleable.CircleImageView_text2);
        otherTextColor = attributes.getColor(R.styleable.CircleImageView_text2Color, getResources().getColor(android.R.color.black));
        imageSrc = attributes.getDrawable(R.styleable.CircleImageView_imageSrc);
        attributes.recycle();
        init();
    }

    private void init() {
        paintOuterCircle = new Paint();
        paintOuterCircle.setStyle(Paint.Style.STROKE);
        paintOuterCircle.setStrokeWidth(strokeWidth);
        paintOuterCircle.setAntiAlias(true);
        paintOuterCircle.setColor(strokeColor);

        if (enableBadge) {
            paintArc = new Paint();
            paintArc.setStyle(Paint.Style.FILL);
            paintArc.setStrokeWidth(1f);
            paintArc.setAntiAlias(true);
            paintArc.setColor(badgeColor);

            paintText = new Paint();
            paintText.setTextSize(100);
            paintText.setAntiAlias(true);
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setColor(textColor);

            clipRect = new Rect();
        }

        otherPaintText = new Paint();
        otherPaintText.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                "fonts/verdanab.ttf"));
        otherPaintText.setTextSize(100);
        otherPaintText.setAntiAlias(true);
        otherPaintText.setTextAlign(Paint.Align.CENTER);
        otherPaintText.setColor(otherTextColor);
        otherTextRect = new Rect();


        if (imageSrc != null) {
            middleBitmap = ImageResourceUtil.getBitmap(mContext, imageSrc);
            imgSrcRect2 = new Rect(0, 0, middleBitmap.getWidth(), middleBitmap.getHeight());
            imgDestRect2 = new Rect();
        }

        if (backgroundSrc != null) {
            backgroundBitmap = ImageResourceUtil.getBitmap(mContext, backgroundSrc);
            imgSrcRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
            imgDestRect = new Rect();
        }

        circlePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        float centreX = width / 2;
        float centreY = height / 2;
        float marginCircle = 10f;

        float smallestRadius = (height < width) ? (height / 2f) - marginCircle : (width / 2f) - marginCircle;
        canvas.drawCircle(centreX, centreY, smallestRadius, paintOuterCircle);

        circlePath.addCircle(centreX, centreY, smallestRadius - (strokeWidth / 2) + 1f, Path.Direction.CW);
        canvas.clipPath(circlePath);

        if (backgroundSrc != null) {
            imgDestRect.set((int) (centreX - smallestRadius), (int) (centreY - smallestRadius), (int) (centreX + smallestRadius), (int) (centreY + smallestRadius));
            canvas.drawBitmap(backgroundBitmap, imgSrcRect, imgDestRect, null);
        }

        if (imageSrc != null) {
            float imageWidth = 0.60f * smallestRadius;
            imgDestRect2.set((int) (centreX - (imageWidth / 2)), (int) (centreY - (0.70 * smallestRadius)), (int) (centreX + (imageWidth / 2)), (int) (centreY + (0.05 * smallestRadius)));
            canvas.drawBitmap(middleBitmap, imgSrcRect2, imgDestRect2, null);
        }

        otherTextRect.set((int) (centreX - smallestRadius), (int) (centreY + (smallestRadius / 5)), (int) (centreX + smallestRadius), (int) (centreY + (smallestRadius / 1.75)));
        if (otherText != null && !otherText.isEmpty()) {
            float textBoxHeight = 0.23f * smallestRadius;
            adjustOtherTextSize(textBoxHeight);
            float textYOffset2;
            if (imageSrc != null) {
                textYOffset2 = ((int) centreY + (0.50f * smallestRadius));
            } else {
                textYOffset2 = ((int) centreY + (0.50f * textBoxHeight));
            }
            canvas.drawText(otherText, centreX, textYOffset2, otherPaintText);
        }


        if (enableBadge) {
            float rectHeight = 0.22f * smallestRadius;
            clipRect.set((int) (centreX - smallestRadius), (int) (centreY + (0.60f * smallestRadius)), (int) (centreX + smallestRadius), (int) (centreY + (0.60f * smallestRadius) + rectHeight));
            canvas.clipRect(clipRect);
            canvas.drawCircle(centreX, centreY, smallestRadius - strokeWidth / 2 + 1f, paintArc);
            if (bannerText != null && !bannerText.isEmpty()) {
                adjustTextSize(rectHeight);
                float textYOffset = ((int) centreY + (0.76f * smallestRadius));
                canvas.drawText(bannerText, centreX, textYOffset, paintText);
            }
        }

    }

    void adjustTextSize(float mViewHeight) {
        paintText.setTextSize(100);
        paintText.setTextScaleX(1.0f);
        paintText.getTextBounds(bannerText, 0, bannerText.length(), clipRect);
        int h = (int) (clipRect.bottom - clipRect.top);
        float target = (float) mViewHeight * .60f;
        float size = ((target / h) * 100f);
        paintText.setTextSize(size);
    }

    void adjustOtherTextSize(double mViewHeight) {
        otherPaintText.setTextSize(100);
        otherPaintText.setTextScaleX(1.0f);
        otherPaintText.getTextBounds(otherText, 0, otherText.length(), otherTextRect);
        int h = (int) (otherTextRect.bottom - otherTextRect.top);
        float target = (float) mViewHeight * 1f;
        float size = ((target / h) * 100f);
        otherPaintText.setTextSize(size);
    }
}
