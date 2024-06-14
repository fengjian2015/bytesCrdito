package pers.spj.adapter.androidx.common;

import android.content.Context;

import java.util.List;

import pers.spj.adapter.androidx.listener.OnBindViewHolderListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewLayoutIdListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewTypeListener;

/**
 * @author supeijin
 */
interface AdapterOptions<T> {
	Context getContext();
	List<T> getData();
	int getDefaultLayoutResId();
	int getViewTypeCount();
	OnGetItemViewTypeListener<T> getOnGetItemViewTypeListener();
	OnGetItemViewLayoutIdListener getOnGetItemViewLayoutIdListener();
	OnBindViewHolderListener<T> getOnBindViewHolderListener();
}
