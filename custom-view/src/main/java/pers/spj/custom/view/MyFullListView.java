package pers.spj.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.LinkedList;

import androidx.annotation.Nullable;

/**
 * @author bluefox
 * @date 2017/3/20 0020.
 * 完全展开的列表(类似listview)
 */

public class MyFullListView extends LinearLayout {
    private ListAdapter mListAdapter;
    private LinkedList<View> mRecycleViews = new LinkedList<View>();//缓存未显示的View
    private LinkedList<View> mCurrentViews = new LinkedList<View>();//当前显示的view

    public MyFullListView(Context context) {
        super(context);
        init();
    }

    public MyFullListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFullListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyFullListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        int childCount = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            if (super.getOrientation()==VERTICAL) {
                maxHeight =maxHeight+ height;
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }else if(super.getOrientation()==HORIZONTAL){
                maxWidth =maxWidth+ width;
                if (height > maxHeight) {
                    maxHeight = height;
                }
            }
        }
        maxWidth = maxWidth + getPaddingLeft() + getPaddingRight();
        maxHeight = maxHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(maxWidth, maxHeight);
    }

    private void refreshListViews() {
        removeAllViews();
        if (mCurrentViews.size() > 0) {
            mRecycleViews.addAll(mCurrentViews);
            mCurrentViews.clear();
        }
        if (mListAdapter != null && mListAdapter.getCount() > 0) {
            int count = mListAdapter.getCount();
            for (int i = 0; i < count; i++) {
                View convertView = null;
                if (mRecycleViews.size() > 0) {
                    convertView = mRecycleViews.removeFirst();
                }
                View view = mListAdapter.getView(i, convertView, this);
                this.addView(view);
                mCurrentViews.add(view);
            }
            requestLayout();
        }
    }

    public View getItemView(int position){
        if(position<mCurrentViews.size()){
            return  mCurrentViews.get(position);
        }
        return null;
    }

    /**
     * 局部刷新item
     * @param position
     * @return
     */
    public boolean updateItemView(int position){
        if(position<mCurrentViews.size()){
            View view = mCurrentViews.get(position);
            mListAdapter.getView(position,view, (ViewGroup) view.getParent());
            return true;
        }
        return false;
    }

    public void setAdapter(ListAdapter listAdapter) {
        if (mListAdapter != null) {
            mListAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mListAdapter = listAdapter;
        listAdapter.registerDataSetObserver(mDataSetObserver);
        refreshListViews();
    }
    public ListAdapter getAdapter(){
        return mListAdapter;
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            refreshListViews();
        }

    };
}
