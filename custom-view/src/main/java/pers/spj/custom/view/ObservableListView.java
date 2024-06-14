package pers.spj.custom.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 可以获取滚动的方向（向下或向上）
 */
public class ObservableListView extends ListView {
    private boolean isScrollUp;//是否是向上滑动
    private float mDownY;//手指触摸起始点
    public ObservableListView(Context context) {
        super(context);
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrollUp=false;
                mDownY = event.getY();
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float dy = event.getY() - mDownY;
                if (dy <0) {
                    isScrollUp = true;
                } else {
                    isScrollUp = false;
                }
                mDownY=-1;
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * true 表示向上
     * false 表示向下
     * @return
     */
    public boolean getScrollOrientation(){
        return isScrollUp;
    }
}
