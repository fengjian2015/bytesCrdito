package pers.spj.custom.common.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import pers.spj.custom.common.utils.LogUtil;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class WrapStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private static final String TAG = WrapStaggeredGridLayoutManager.class.getSimpleName();
    public WrapStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG,LogUtil.getExceptionMsg(e));
        }catch (Exception e){
            Log.e(TAG, LogUtil.getExceptionMsg(e));
        }
    }
}
