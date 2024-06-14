package pers.spj.adapter.androidx.service;

import android.content.Context;

import java.util.List;

import pers.spj.adapter.androidx.listener.OnBindViewHolderListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewLayoutIdListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewTypeListener;

/**
 * @author supeijin
 */
public interface AdapterBuilderService<T,R>{
	AdapterBuilderService<T,R> setContext(Context context);
	AdapterBuilderService<T,R> setData(List<T> data);
	AdapterBuilderService<T,R> setDefaultLayoutResId(int defaultLayoutResId);
	AdapterBuilderService<T,R> setViewTypeCount(int viewTypeCount);
	AdapterBuilderService<T,R> setOnGetItemViewTypeListener(OnGetItemViewTypeListener<T> onGetItemViewTypeListener);
	AdapterBuilderService<T,R> setOnGetItemViewLayoutIdListener(OnGetItemViewLayoutIdListener onGetItemViewLayoutIdListener);
	AdapterBuilderService<T,R> setOnBindViewHolderListener(OnBindViewHolderListener<T> onBindViewHolderListener);
	R build();
}
