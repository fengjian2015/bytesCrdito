package pers.spj.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 圆形图片、圆角图片
 */
public class ExpandImageView extends AppCompatImageView {
    private Paint mPaint;
    private Path mRoundRectPath;
    private Path mCirclePath;
    private Paint mBorderPaint;
    private int mWidth;
    private int mHeight;
    private float mBorderWidth;
    private int mBorderColor;
    private int mBackgroundColor;
    private float mCornerRadius;
    private float mCornerRadiusTopLeft;
    private float mCornerRadiusTopRight;
    private float mCornerRadiusBottomLeft;
    private float mCornerRadiusBottomRight;
    private float[] mCornerRadiusArr = new float[8];
    private boolean onSizeChanged=false;
    //默认颜色
    private static final int DEFAULT_BACKGROUND_CORLOR=0;
    private boolean isOval;
    public ExpandImageView(@NonNull Context context) {
        super(context);
    }

    public ExpandImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ExpandImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandImageView, defStyleAttr, 0);
        mBorderWidth = typedArray.getDimension(R.styleable.ExpandImageView_expiv_border_width, 0);
        mBorderColor = typedArray.getColor(R.styleable.ExpandImageView_expiv_border_color, Color.parseColor("#00ff00"));
        mBackgroundColor = typedArray.getColor(R.styleable.ExpandImageView_expiv_background_color, 0);
        mCornerRadius = typedArray.getDimension(R.styleable.ExpandImageView_expiv_corner_radius, 0);
        mCornerRadiusTopLeft = typedArray.getDimension(R.styleable.ExpandImageView_expiv_corner_radius_top_left, 0);
        mCornerRadiusTopRight = typedArray.getDimension(R.styleable.ExpandImageView_expiv_corner_radius_top_right, 0);
        mCornerRadiusBottomLeft = typedArray.getDimension(R.styleable.ExpandImageView_expiv_corner_radius_bottom_left, 0);
        mCornerRadiusBottomRight = typedArray.getDimension(R.styleable.ExpandImageView_expiv_corner_radius_bottom_right, 0);
        updateRadiusArrs();
        isOval = typedArray.getBoolean(R.styleable.ExpandImageView_expiv_oval, false);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(mBackgroundColor);
        mPaint.setAntiAlias(true);
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth=w;
        onSizeChanged=true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBackgroundColor==DEFAULT_BACKGROUND_CORLOR){
            clipCanvas(canvas,isOval);
            super.onDraw(canvas);
        }else{
            super.onDraw(canvas);
            drawOverlay(canvas);
        }
    }

    private void clipCanvas(Canvas canvas,boolean isCircle){
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        if(isCircle){
            mCornerRadius=Math.min(mWidth,mHeight)/2;
        }
        RectF rectF = new RectF(rect);
        Path path = new Path();
        if (mCornerRadius > 0) {
            path.addRoundRect(rectF,mCornerRadius,mCornerRadius,Path.Direction.CW);
        } else {
            path.addRoundRect(rectF, mCornerRadiusArr, Path.Direction.CW);
        }
        path.addRect(0,0,mWidth,mHeight,Path.Direction.CW);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.clipPath(path);
        canvas.drawPath(path,paint);
        canvas.save();
    }
    private void drawOverlay(Canvas canvas) {
        if (mWidth!=0 && mHeight!=0) {
            if (isOval) {
                drawCircleOverlay(canvas);
            } else  {
                drawRoundOverlay(canvas);
            }
        }
    }

    private void drawCircleOverlay(Canvas canvas) {
        int min = Math.min(mWidth, mHeight);
        float radius = min / 2;
        drawCircleOverlay(canvas, radius);
        if(mBorderWidth>0){
            drawBorderOverlay(canvas, radius);
        }
    }

    private void drawBorderOverlay(Canvas canvas, float radius) {
        canvas.drawCircle(radius, radius, radius - mBorderWidth, mBorderPaint);
    }

    private void drawCircleOverlay(Canvas canvas, float radius) {
        if(mCirclePath==null || onSizeChanged){
            onSizeChanged=false;
            mCirclePath = new Path();
            mCirclePath.addRect(0, 0, mWidth, mHeight, Path.Direction.CW);
            mCirclePath.addCircle(radius, radius, radius - mBorderWidth, Path.Direction.CW);
            mCirclePath.setFillType(Path.FillType.EVEN_ODD);
        }
        canvas.drawPath(mCirclePath, mPaint);
    }

    public void drawRoundOverlay(Canvas canvas) {
        if(mRoundRectPath==null || onSizeChanged){
            onSizeChanged=false;
            mRoundRectPath = new Path();
            mRoundRectPath.addRect(0, 0, mWidth, mHeight, Path.Direction.CW);
            RectF rectF = new RectF(0, 0, mWidth, mHeight);
            if (mCornerRadius > 0) {
                mRoundRectPath.addRoundRect(rectF, mCornerRadius, mCornerRadius, Path.Direction.CW);
            } else {
                mRoundRectPath.addRoundRect(rectF, mCornerRadiusArr, Path.Direction.CW);
            }
            mRoundRectPath.setFillType(Path.FillType.EVEN_ODD);
        }
        canvas.drawPath(mRoundRectPath, mPaint);
    }
    private void updateRadiusArrs() {
        mCornerRadiusArr[Corner.TOP_LEFT] = mCornerRadiusTopLeft;
        mCornerRadiusArr[Corner.TOP_LEFT + 1] = mCornerRadiusTopLeft;

        mCornerRadiusArr[Corner.TOP_RIGHT] = mCornerRadiusTopRight;
        mCornerRadiusArr[Corner.TOP_RIGHT + 1] = mCornerRadiusTopRight;

        mCornerRadiusArr[Corner.BOTTOM_LEFT] = mCornerRadiusBottomLeft;
        mCornerRadiusArr[Corner.BOTTOM_LEFT + 1] = mCornerRadiusBottomLeft;

        mCornerRadiusArr[Corner.BOTTOM_RIGHT] = mCornerRadiusBottomRight;
        mCornerRadiusArr[Corner.BOTTOM_RIGHT + 1] = mCornerRadiusBottomRight;
    }
    private static class Corner {
        protected static int TOP_LEFT = 0;
        protected static int TOP_RIGHT = 2;
        protected static int BOTTOM_RIGHT = 4;
        protected static int BOTTOM_LEFT = 6;
    }
}
