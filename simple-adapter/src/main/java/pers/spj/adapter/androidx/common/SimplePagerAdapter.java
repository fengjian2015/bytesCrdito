package pers.spj.adapter.androidx.common;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import pers.spj.adapter.androidx.service.AdapterBuilderService;

/**
 * @author supeijin
 */
public final class SimplePagerAdapter<T> extends PagerAdapter {
	private float mPageWidth=1.0f;
	private SparseArray<View> mRecycledViews;
	private CommonAdapterImpl<T> mCommonAdapter;
	private SimplePagerAdapter() {
	}

	private SimplePagerAdapter(AdapterOptions<T> options) {
		mRecycledViews = new SparseArray<>();
		mCommonAdapter = new CommonAdapterImpl<>(options);
	}

	@Override
	public int getCount() {
		return mCommonAdapter.getCount();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		mCommonAdapter.removeViewHolderService(position);
		if (object != null && object instanceof View) {
			View itemView= (View) object;
			int itemViewType = mCommonAdapter.getItemViewType(position);
			container.removeView(itemView);
			mRecycledViews.put(itemViewType,itemView);
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int viewType = mCommonAdapter.getItemViewType(position);
		View contentView = mRecycledViews.get(viewType);
		mRecycledViews.remove(viewType);
		contentView = mCommonAdapter.getView(position, viewType, contentView, container);
		container.addView(contentView);
		return contentView;
	}

	public void updateViewPager(int position){
		updateViewPager(position,null);
	}

	public void updateViewPager(int position,T t){
		mCommonAdapter.updateItemView(position,t);
	}
	@Override
	public float getPageWidth(int position) {
		if(mPageWidth>0){
			return mPageWidth;
		}
		return super.getPageWidth(position);
	}

	public void setPageWidth(float pageWidth) {
		mPageWidth = pageWidth;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	static final class Builder<T> extends AbsCommonAdapterBuilder<T,SimplePagerAdapter<T>> {
		public static <S> AdapterBuilderService<S,SimplePagerAdapter<S>> newInstance() {
            return new Builder<S>();
        }
        private Builder(){}
		@Override
		public SimplePagerAdapter<T> build() {
			super.validate();
			return new SimplePagerAdapter(this);
		}
	}
}
