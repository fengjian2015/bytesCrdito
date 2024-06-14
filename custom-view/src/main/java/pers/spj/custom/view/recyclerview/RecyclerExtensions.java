package pers.spj.custom.view.recyclerview;

import android.view.View;
/**
 * @author supeijin
 */

public interface RecyclerExtensions {

	void addEmptyView(View emptyView);

	void removeEmptyView();

	void addHeaderView(View headerView);

	void addFooterView(View footerView);

	void removeHeaderView(View headerView);

	void removeHeaderViews();

	void removeFooterView(View footerView);

	void removeFooterViews();

	void setLoadMoreEnabled(boolean enabled);

	void loadMoreCompeleted();

	void setLoadMoreView(View loadMoreView);

	void setDisableLoadMore();

	void setOnLoadMreListener(ExpRecyclerView.OnLoadMoreListener listener);
}
