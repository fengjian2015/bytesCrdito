package pers.spj.custom.common.helper;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import pers.spj.custom.common.utils.LogUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = WrapLinearLayoutManager.class.getSimpleName();
    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
            Log.e(TAG, LogUtil.getExceptionMsg(e));
        }catch (Exception e){
            Log.e(TAG, LogUtil.getExceptionMsg(e));
        }
    }
}
