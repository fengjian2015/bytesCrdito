package pers.spj.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 等间距容器
 * @author supeijin
 */

public class EquidistanceLinearLayout extends LinearLayout {
	private int mHorizontalSpace;
	private int mChildCount;
	public EquidistanceLinearLayout(Context context) {
		super(context);
	}

	public EquidistanceLinearLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public EquidistanceLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public EquidistanceLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setOrientation(HORIZONTAL);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mChildCount = getChildCount();
		if(mChildCount>1){
			int maxChildWidth=0;
			for (int i=0;i<mChildCount;i++){
				View childView = getChildAt(i);
				this.measureChild(childView, widthMeasureSpec, heightMeasureSpec);
				maxChildWidth=maxChildWidth+childView.getMeasuredWidth();
			}
			int totalSpace= getMeasuredWidth() - maxChildWidth;
			int spaceCount=mChildCount-1;
			mHorizontalSpace=spaceCount==1?totalSpace:totalSpace/spaceCount;
			for (int i = 1; i <mChildCount ; i++) {
				View childView = getChildAt(i);
				LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
				layoutParams.leftMargin=mHorizontalSpace;
			}
		}
	}
}
