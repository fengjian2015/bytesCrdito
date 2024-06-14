package pers.spj.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatTextView;
import pers.spj.custom.view.util.FontManager;

/**
 * 一个可以去除顶部跟底部间距的文本控件
 *
 * @author supeijin
 * @date 2017/11/29.
 */

public class ExpandTextView extends AppCompatTextView {
    /**
     * 标记是否启用文间距
     */
    private boolean useSpacing;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FontManager.getInstance().applyTypeface(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, defStyleAttr, 0);
        useSpacing = array.getBoolean(R.styleable.ExpandTextView_use_spacing, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if(mode!=MeasureSpec.EXACTLY && !useSpacing){
            int oldMeasuredHeight = getMeasuredHeight();
            TextPaint textPaint = getPaint();
            float lineSpacing = textPaint.getFontSpacing() - textPaint.getTextSize();
            int measuredHeight = (int) (oldMeasuredHeight  - lineSpacing * 2);
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
            int space = (int) (lineSpacing);
            scrollTo(0, space);
        }
    }
}
