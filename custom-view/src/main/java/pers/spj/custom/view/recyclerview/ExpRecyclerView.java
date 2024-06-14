package pers.spj.custom.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import pers.spj.custom.view.R;

/**
 * it add some funtion 。(like as addHeader,addFooter,addEmptyView,loadmore)
 * @author supeijin
 */
public class ExpRecyclerView extends RecyclerView implements RecyclerExtensions {
	private static final int HEADER_VIEW_TYPE=800000;
	private static final int FOOTER_VIEW_TYPE=900000;
	private static final int EMPTY_VIEW_TYPE=1000000;
	private static final int LOAD_MORE_VIEW_TYPE=1200000;
	private SparseArray<CustomViewHolder> mHeaderViews = new SparseArray<>();
	private SparseArray<CustomViewHolder> mFooterViews = new SparseArray<>();
	private CustomViewHolder mEmptyViewHolder;
	private CustomViewHolder mLoadMoreViewHolder;
	private WrapAdapter mWrapAdapter;
	private DataObserver mDataObserver = new DataObserver();
	private boolean loadMoreEnabled;
	private boolean isLoadingData = false;
	private boolean activateLoadMoreEvent;
	private OnLoadMoreListener mOnLoadMoreListener;
	public ExpRecyclerView(Context context) {
		this(context,null);
	}

	public ExpRecyclerView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs,0);
	}

	public ExpRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs,defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		loadMoreEnabled=false;
		View loadMoreView = LayoutInflater.from(context).inflate(R.layout.load_more,null ,false);
		mLoadMoreViewHolder = new CustomViewHolder(loadMoreView,LOAD_MORE_VIEW_TYPE);
	}

	@Override
	public final void setLayoutManager(LayoutManager layout) {
		super.setLayoutManager(layout);
		if(layout!=null && layout instanceof GridLayoutManager){
			final GridLayoutManager gridManager = ((GridLayoutManager) layout);
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
				@Override
				public int getSpanSize(int position) {
					//SpanSize表示一个item的跨度，跨度了多少个span。
					int spanSize = gridManager.getSpanCount();
					if(mWrapAdapter!=null){
						int itemViewType = mWrapAdapter.getItemViewType(position);
						boolean isHeaderOrFooter=itemViewType>HEADER_VIEW_TYPE;
						return isHeaderOrFooter?spanSize:1;
					}
					return spanSize;
				}
			});
		}
	}

	@Override
	public Adapter getAdapter() {
		if (mWrapAdapter != null) {
			return mWrapAdapter.getWrapAdapter();
		}
		return null;
	}

	@Override
	public void setAdapter(Adapter adapter) {
		if(adapter==null){
			throw new NullPointerException("adapter == null");
		}
		if(mWrapAdapter!=null){
			mWrapAdapter.getWrapAdapter().unregisterAdapterDataObserver(mDataObserver);
		}
		mWrapAdapter = new WrapAdapter(adapter);
		adapter.registerAdapterDataObserver(mDataObserver);
		super.setAdapter(mWrapAdapter);
	}
	@Override
	public void onScrollStateChanged(int state) {
		super.onScrollStateChanged(state);
		if(!isLoadingData&&SCROLL_STATE_IDLE ==state && mOnLoadMoreListener!=null && activateLoadMoreEvent){
			isLoadingData=true;
			mLoadMoreViewHolder.itemView.setVisibility(VISIBLE);
			mOnLoadMoreListener.onLoadMore();
		}
	}

	@Override
	public final void addEmptyView(View emptyView) {
		if (emptyView == null) {
			return;
		}
		mEmptyViewHolder = new CustomViewHolder(emptyView, EMPTY_VIEW_TYPE);
	}
    @Override
	public final void removeEmptyView(){
		mEmptyViewHolder=null;
	}
	@Override
	public final void addHeaderView(View headerView){
		addCustomView(HEADER_VIEW_TYPE,mHeaderViews,headerView);
	}
	@Override
	public final void addFooterView(View footerView){
		addCustomView(FOOTER_VIEW_TYPE,mFooterViews,footerView);
	}
    public final int getHeaderCount(){
		return  mHeaderViews.size();
	}
	public final int getFooterCount(){
		return  mFooterViews.size();
	}
	private final void addCustomView(int startBoundary,SparseArray<CustomViewHolder> sparseArray,View view){
		Pair<Integer, CustomViewHolder> pair = getViewInfo(sparseArray, view);
		if(pair==null){
			int maxKey = getMaxKey(sparseArray);
			if(maxKey<0){
				maxKey=startBoundary+sparseArray.size()+1;
			}else{
				maxKey=maxKey+1;
			}
			CustomViewHolder footViewHolder = new CustomViewHolder(view, maxKey);
			sparseArray.put(maxKey,footViewHolder);
		}else{
			throw new IllegalArgumentException("The view has already existed");
		}
	}
	private final void removeCustomViews(SparseArray<CustomViewHolder> sparseArray,boolean isHeader){
		int size = sparseArray.size();
		if(size >0){
			int itemCount = mWrapAdapter.getItemCount();
			sparseArray.clear();
			if(mWrapAdapter!=null){
				int starIndex;
				if(isHeader){
					starIndex=0;
				}else{
					starIndex= itemCount -size;
				}
				mWrapAdapter.notifyItemRangeRemoved(starIndex,size);
			}
		}
	}
	private void removeCustomView(SparseArray<CustomViewHolder> sparseArray,View view){
		ViewHolder viewHolder = findContainingViewHolder(view);
		if(viewHolder!=null && viewHolder instanceof CustomViewHolder){
			int customType = ((CustomViewHolder) viewHolder).customType;
			for (int i = 0,size = sparseArray.size(); i < size; i++) {
				int keyAt = sparseArray.keyAt(i);
				if(keyAt==customType){
					sparseArray.removeAt(i);
					if(mWrapAdapter!=null){
						mWrapAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
	@Override
	public final void removeHeaderView(View headerView) {
		removeCustomView(mHeaderViews, headerView);
	}
	@Override
	public final void removeHeaderViews(){
		removeCustomViews(mHeaderViews,true);
	}
	@Override
	public final void removeFooterView(View footerView) {
		removeCustomView(mFooterViews, footerView);
	}
	@Override
	public final void removeFooterViews(){
		removeCustomViews(mFooterViews,false);
	}
	@Override
	public void setLoadMoreEnabled(boolean enabled){
		loadMoreEnabled=enabled;
	}
	@Override
    public final void loadMoreCompeleted(){
		isLoadingData=false;
		if(mLoadMoreViewHolder!=null && mLoadMoreViewHolder.itemView!=null){
			mLoadMoreViewHolder.itemView.setVisibility(INVISIBLE);
		}
	}
	@Override
	public final void setLoadMoreView(View loadMoreView){
    	if(loadMoreView!=null){
			loadMoreView.setVisibility(INVISIBLE);
			mLoadMoreViewHolder=new CustomViewHolder(loadMoreView,LOAD_MORE_VIEW_TYPE);
		}
	}
	@Override
    public final void setDisableLoadMore(){
		stopScroll();
		int itemCount = mWrapAdapter.getItemCount();
		mWrapAdapter.notifyItemRemoved(itemCount-1);
		loadMoreEnabled=false;
	}
	@Override
	public final void setOnLoadMreListener(OnLoadMoreListener listener) {
		mOnLoadMoreListener = listener;
	}
    private int getMaxKey(SparseArray<CustomViewHolder> sparseArray){
		int key = -1;
		int size = sparseArray.size();
		for (int i = 0; i < size; i++) {
			key = Math.max(key,sparseArray.keyAt(i));
		}
		return key;
	}
	private Pair<Integer,CustomViewHolder> getViewInfo(SparseArray<CustomViewHolder> sparseArray, View view){
		if(view==null || sparseArray==null || sparseArray.size()==0 ){
			return null;
		}
		int size = sparseArray.size();
		for (int i = 0; i < size; i++) {
			CustomViewHolder customViewHolder = sparseArray.valueAt(i);
			if(customViewHolder.itemView.equals(view)){
				return new Pair<>(i,sparseArray.valueAt(i));
			}
		}
		return null;
	}

	private final class WrapAdapter extends Adapter{
		private Adapter wAdapter;
		public WrapAdapter(Adapter adapter) {
			wAdapter=adapter;
		}
		@Override
		public long getItemId(int position) {
			int itemViewType = getItemViewType(position);
			if(itemViewType>HEADER_VIEW_TYPE){
				return itemViewType+position;
			}
			return wAdapter.getItemId(position);
		}
		@Override
		public int getItemCount() {
			int realAdapterItemCount = wAdapter.getItemCount();
			int adapterCount = getContentAdapterCount();
			int itemCount = adapterCount + mHeaderViews.size() + mFooterViews.size();
			if(loadMoreEnabled && mLoadMoreViewHolder!=null && realAdapterItemCount>0){
				return itemCount+1;
			}
			return itemCount;
		}

		@Override
		public int getItemViewType(int position) {
			int headerCount = mHeaderViews.size();
			int adapterCount = getContentAdapterCount();
			if(headerCount!=0 && position<headerCount){
				//header type
				CustomViewHolder viewHolder = mHeaderViews.valueAt(position);
				return viewHolder.customType;
			}else if( mFooterViews.size()>0 && position+1>headerCount+adapterCount){
				//footer type
				int index = position - (headerCount + adapterCount);
				CustomViewHolder viewHolder = mFooterViews.valueAt(index);
				return viewHolder.customType;
			}else if(position>=headerCount+adapterCount+mFooterViews.size()){
				//load more Type
				return LOAD_MORE_VIEW_TYPE;
			}else if(wAdapter.getItemCount()==0 && mEmptyViewHolder!=null && position==headerCount){
				//empty type
				return EMPTY_VIEW_TYPE;
			}
			int itemViewType = wAdapter.getItemViewType(getRealPosition(position));
			if(itemViewType>=HEADER_VIEW_TYPE){
				throw new RuntimeException("The itemViewType must always be smaller than "+String.valueOf(HEADER_VIEW_TYPE));
			}
			return itemViewType;
		}
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if(viewType==LOAD_MORE_VIEW_TYPE){
				return mLoadMoreViewHolder;
			}else if(viewType==EMPTY_VIEW_TYPE){
				return mEmptyViewHolder;
			}else if(viewType>FOOTER_VIEW_TYPE){
				return mFooterViews.get(viewType);
			}else if(viewType>HEADER_VIEW_TYPE){
				return mHeaderViews.get(viewType);
			}
			return wAdapter.createViewHolder(parent, viewType);
		}
		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			if (holder instanceof CustomViewHolder) {
				//custom viewType
				ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
				if (getLayoutManager() instanceof StaggeredGridLayoutManager){
					int orientation = ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation();
					layoutParams=adjustLayoutParams(holder, layoutParams,orientation);
				}
				if(holder==mEmptyViewHolder){
					adjustEmptyLayoutParams(holder, layoutParams);
				}
			} else  {
				wAdapter.onBindViewHolder(holder, getRealPosition(position));
			}
		}

		@Override
		public void onViewAttachedToWindow(ViewHolder holder) {
			super.onViewAttachedToWindow(holder);
			if(holder==mLoadMoreViewHolder){
				activateLoadMoreEvent=true;
			}
		}

		@Override
		public void onViewDetachedFromWindow(ViewHolder holder) {
			super.onViewDetachedFromWindow(holder);
			if(holder==mLoadMoreViewHolder){
				activateLoadMoreEvent=false;
			}
		}

		private int getContentAdapterCount() {
			int adapterCount = wAdapter.getItemCount();
			if(adapterCount==0 && mEmptyViewHolder!=null){
				adapterCount=1;
			}
			return adapterCount;
		}

		private int getRealPosition(int position){
			int headerCount = mHeaderViews.size();
			if(headerCount==0){
				return position;
			}
			return position-headerCount;
		}

		private StaggeredGridLayoutManager.LayoutParams adjustLayoutParams(ViewHolder holder, ViewGroup.LayoutParams layoutParams,int orientation) {
			StaggeredGridLayoutManager.LayoutParams params;
			if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
				params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
			}else{
				int width;
				int height;
				if(orientation==StaggeredGridLayoutManager.VERTICAL){
					width=StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT;
					height=StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT;
				}else{
					width=StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT;
					height=StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT;
				}
				params = new StaggeredGridLayoutManager.LayoutParams(width,height);
			}
			params.setFullSpan(true);
			holder.itemView.setLayoutParams(params);
			return params;
		}
		private void adjustEmptyLayoutParams(ViewHolder holder, ViewGroup.LayoutParams layoutParams) {
			if(layoutParams==null){
				layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			holder.itemView.measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);
			layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT;
			layoutParams.height=ViewGroup.LayoutParams.MATCH_PARENT;
			holder.itemView.setLayoutParams(layoutParams);
		}
		public WrapAdapter getWrapAdapter() {
			return mWrapAdapter;
		}
	}
	private class DataObserver extends AdapterDataObserver {
		@Override
		public void onChanged() {
			if (mWrapAdapter != null) {
				mWrapAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeInserted(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeChanged(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
			mWrapAdapter.notifyItemRangeChanged(getRealPosition(positionStart), itemCount, payload);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeRemoved(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			mWrapAdapter.notifyItemMoved(getRealPosition(fromPosition),  getRealPosition(toPosition));
		}
		private int getRealPosition(int position){
			return mHeaderViews.size()+position;
		}
	}
    private static class CustomViewHolder extends ViewHolder{
    	public int customType;
		public CustomViewHolder(View itemView,int customType) {
			super(itemView);
			this.customType=customType;
		}
	}
	public interface OnLoadMoreListener{
		void onLoadMore();
	}
}
