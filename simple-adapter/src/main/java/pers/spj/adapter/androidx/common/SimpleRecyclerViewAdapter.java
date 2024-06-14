package pers.spj.adapter.androidx.common;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import pers.spj.adapter.androidx.service.AdapterBuilderService;

/**
 * @author supeijin
 */
public final class SimpleRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewViewHolder> {
	private CommonAdapterImpl<T> mCommonAdapter;
	private SimpleRecyclerViewAdapter() {
	}
	private SimpleRecyclerViewAdapter(AdapterOptions<T> options) {
		mCommonAdapter=new CommonAdapterImpl<>(options);
	}
	@Override
	public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	   return mCommonAdapter.onCreateViewHolder(parent,viewType);
	}

	@Override
	public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
		mCommonAdapter.onBindViewHolder(holder.getViewHolderService(),position);
	}

	@Override
	public int getItemCount() {
		return mCommonAdapter.getCount();
	}
	@Override
	public int getItemViewType(int position) {
		return mCommonAdapter.getItemViewType(position);
	}

	static final class Builder<T> extends AbsCommonAdapterBuilder<T,SimpleRecyclerViewAdapter<T>> {
		public static <S> AdapterBuilderService<S,SimpleRecyclerViewAdapter<S>> newInstance() {
			return new Builder<S>();
		}
		private Builder(){}
		@Override
		public SimpleRecyclerViewAdapter<T> build() {
			super.validate();
			return new SimpleRecyclerViewAdapter(this);
		}
	}
}
