package pers.spj.custom.view.recyclerview;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author bluefox
 * @date 2023/7/10.
 */

public class SwipeRefresh implements SwipeRefreshLayout.OnRefreshListener {
    private OnRefSwipeRefreshListener mOnRefreshListener;

    public SwipeRefresh() {
    }

    @Override
    public void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

   public void setOnRefreshListener(OnRefSwipeRefreshListener onRefreshListener) {
      mOnRefreshListener = onRefreshListener;
   }
   public void removeOnRefreshListener(){
       mOnRefreshListener=null;
   }
   public interface OnRefSwipeRefreshListener {
        void onRefresh();
    }

}
