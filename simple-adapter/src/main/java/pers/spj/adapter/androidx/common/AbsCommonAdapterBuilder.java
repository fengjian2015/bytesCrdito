package pers.spj.adapter.androidx.common;

import android.content.Context;

import java.util.List;

import pers.spj.adapter.androidx.listener.OnBindViewHolderListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewLayoutIdListener;
import pers.spj.adapter.androidx.listener.OnGetItemViewTypeListener;
import pers.spj.adapter.androidx.service.AdapterBuilderService;

/**
 * @author supeijin
 */
abstract class AbsCommonAdapterBuilder<T,R> implements AdapterBuilderService<T,R>, AdapterOptions<T> {
	private Context mContext;
	private int mDefaultLayoutResId=0;
	private List<T> mData;
	private int mViewTypeCount=-1;
	private OnGetItemViewTypeListener<T> mOnGetItemViewTypeListener;
	private OnGetItemViewLayoutIdListener mOnGetItemViewLayoutIdListener;
	private OnBindViewHolderListener<T> mOnBindViewHolderListener;
	@Override
	public Context getContext() {
		return mContext;
	}
	@Override
	public final AdapterBuilderService<T,R> setContext(Context context) {
		mContext = context;
		return this;
	}

	public final int getDefaultLayoutResId() {
		return mDefaultLayoutResId;
	}
    @Override
	public final AdapterBuilderService<T,R> setDefaultLayoutResId(int defaultLayoutResId) {
		this.mDefaultLayoutResId = defaultLayoutResId;
		return this;
	}
	@Override
	public final List<T> getData() {
		return mData;
	}
	@Override
	public final AdapterBuilderService<T,R> setData(List<T> data) {
		mData = data;
		return this;
	}

	public final int getViewTypeCount() {
		return mViewTypeCount;
	}
	@Override
	public final AdapterBuilderService<T,R> setViewTypeCount(int viewTypeCount) {
		mViewTypeCount = viewTypeCount;
		return this;
	}
	@Override
	public final AdapterBuilderService<T,R> setOnGetItemViewTypeListener(OnGetItemViewTypeListener<T> onGetItemViewTypeListener) {
		mOnGetItemViewTypeListener = onGetItemViewTypeListener;
		return this;
	}
	@Override
	public final AdapterBuilderService<T,R> setOnGetItemViewLayoutIdListener(OnGetItemViewLayoutIdListener onGetItemViewLayoutIdListener) {
		mOnGetItemViewLayoutIdListener = onGetItemViewLayoutIdListener;
		return this;
	}
	@Override
	public final OnGetItemViewTypeListener<T> getOnGetItemViewTypeListener() {
		return mOnGetItemViewTypeListener;
	}
	@Override
	public final OnGetItemViewLayoutIdListener getOnGetItemViewLayoutIdListener() {
		return mOnGetItemViewLayoutIdListener;
	}
	@Override
	public final OnBindViewHolderListener<T> getOnBindViewHolderListener() {
		return mOnBindViewHolderListener;
	}
	@Override
	public final AdapterBuilderService<T,R> setOnBindViewHolderListener(OnBindViewHolderListener<T> onBindViewHolderListener) {
		mOnBindViewHolderListener = onBindViewHolderListener;
		return this;
	}

	/**
	 * 验证参数，确保必要的参数不能为空
	 */
	protected final void validate(){
		if (getContext() == null) {
			throw new IllegalArgumentException("context can not be null");
		}
		if (getData() == null) {
			throw new IllegalArgumentException(" datas can not be null");
		}
		if (getOnBindViewHolderListener() == null) {
			throw new IllegalArgumentException("OnBindViewHolderListener can not be null");
		}
		if (getDefaultLayoutResId() == 0) {
			if (getOnGetItemViewLayoutIdListener() == null) {
				throw new IllegalArgumentException("OnGetItemTypeLayoutIdListener can not be null");
			} else if (getOnGetItemViewTypeListener() == null) {
				throw new IllegalArgumentException(" OnGetItemViewTypeListener can not be null");
			} else if(getViewTypeCount()<1){
				throw new IllegalArgumentException("The ViewTypeCount must be greater than zero");
			}
		}
	}
}

