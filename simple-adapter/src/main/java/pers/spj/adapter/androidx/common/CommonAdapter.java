package pers.spj.adapter.androidx.common;

import android.view.View;
import android.view.ViewGroup;

import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
interface CommonAdapter<T> {
	int getCount();

	long getItemId(int position);

	 T getItem(int position);

	int getViewTypeCount();

	int getItemViewType(int position);

	int getItemViewLayoutId(int itemViewType);

	View getView(int position, int viewType, View itemView, ViewGroup parent);

	RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

	void onBindViewHolder(ViewHolderService service, int position);
}
