package pers.spj.custom.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author supeijin
 * @date 2018/1/3.
 */

public class SwipeRefreshRecyclerView extends SwipeRefreshLayout implements RecyclerExtensions {
    private ExpRecyclerView mRecyclerView;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRecyclerView = new ExpRecyclerView(context, attrs);
        addView(mRecyclerView);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    public ExpRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void addEmptyView(View emptyView) {
        mRecyclerView.addEmptyView(emptyView);
    }

    @Override
    public void removeEmptyView() {
        mRecyclerView.removeEmptyView();
    }

    @Override
    public void addHeaderView(View headerView) {
        mRecyclerView.addHeaderView(headerView);
    }

    @Override
    public void addFooterView(View footerView) {
        mRecyclerView.addFooterView(footerView);
    }

    @Override
    public void removeHeaderView(View headerView) {
        mRecyclerView.removeHeaderView(headerView);
    }

    @Override
    public void removeHeaderViews() {
        mRecyclerView.removeHeaderViews();
    }

    @Override
    public void removeFooterView(View footerView) {
        mRecyclerView.removeFooterView(footerView);
    }

    @Override
    public void removeFooterViews() {
        mRecyclerView.removeFooterViews();
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        mRecyclerView.setLoadMoreEnabled(enabled);
    }

    @Override
    public void loadMoreCompeleted() {
        mRecyclerView.loadMoreCompeleted();
    }

    @Override
    public void setLoadMoreView(View loadMoreView) {
        mRecyclerView.setLoadMoreView(loadMoreView);
    }

    @Override
    public void setDisableLoadMore() {
        mRecyclerView.setDisableLoadMore();
    }

    @Override
    public void setOnLoadMreListener(ExpRecyclerView.OnLoadMoreListener listener) {
        mRecyclerView.setOnLoadMreListener(listener);
    }


    @Deprecated
    @Override
    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    public void setOnRefreshListener(@NonNull SwipeRefresh swipeRefresh) {
        super.setOnRefreshListener(swipeRefresh);
    }
}
