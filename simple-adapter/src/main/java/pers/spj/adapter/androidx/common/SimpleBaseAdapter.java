package pers.spj.adapter.androidx.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import pers.spj.adapter.androidx.service.AdapterBuilderService;

/**
 * @author supeijin
 */
public final class SimpleBaseAdapter<T> extends BaseAdapter {
	private CommonAdapterImpl<T> mCommonAdapter;

	private SimpleBaseAdapter() {
	}

	private SimpleBaseAdapter(AdapterOptions<T> options) {
		mCommonAdapter = new CommonAdapterImpl<>(options);
	}

	@Override
	public int getCount() {
		return mCommonAdapter.getCount();
	}

	@Override
	public T getItem(int position) {
		return mCommonAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return mCommonAdapter.getItemId(position);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		int itemViewType = mCommonAdapter.getItemViewType(position);
		return mCommonAdapter.getView(position, itemViewType, view, viewGroup);
	}

	@Override
	public int getViewTypeCount() {
		return mCommonAdapter.getViewTypeCount();
	}

	public void updateItemView(int position) {
		updateItemView(position, null);
	}

	public void updateItemView(int position, T t) {
		mCommonAdapter.updateItemView(position, t);
	}

	static final class Builder<T> extends AbsCommonAdapterBuilder<T,SimpleBaseAdapter<T>> {
        public static <S> AdapterBuilderService<S,SimpleBaseAdapter<S>> newInstance() {
            return new Builder<S>();
        }
	    private Builder(){}
		@Override
		public SimpleBaseAdapter<T> build() {
			super.validate();
			return new SimpleBaseAdapter(this);
		}
	}
}
