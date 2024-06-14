package pers.spj.adapter.androidx.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pers.spj.adapter.androidx.listener.OnGetItemViewTypeListener;
import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
final class CommonAdapterImpl<T>  implements CommonAdapter<T> {
	private SparseArray<ViewHolderService> mCurrentViewHolderServices;
    private AdapterOptions<T> mAdapterOptions;
	public CommonAdapterImpl(AdapterOptions<T> options) {
		mCurrentViewHolderServices = new SparseArray<>();
		mAdapterOptions=options;
	}

	@Override
	public int getCount() {
		return mAdapterOptions.getData().size();
	}

	@Override
	public long getItemId(int position) {
		return this.getItem(position).hashCode();
	}
	@Override
	public T getItem(int position) {
		return mAdapterOptions.getData().get(position);
	}
	@Override
	public int getItemViewLayoutId(int itemViewType) {
		int defaultLayoutId = mAdapterOptions.getDefaultLayoutResId();
		if(defaultLayoutId>0){
			return defaultLayoutId;
		}else{
			return mAdapterOptions.getOnGetItemViewLayoutIdListener().getItemTypeLayoutId(itemViewType);
		}
	}

	@Override
	public int getViewTypeCount() {
		if(mAdapterOptions.getDefaultLayoutResId()>-1){
			return  1;
		}else{
			return mAdapterOptions.getViewTypeCount();
		}
	}

	@Override
	public int getItemViewType(int position) {
		OnGetItemViewTypeListener<T> viewTypeListener = mAdapterOptions.getOnGetItemViewTypeListener();
		if(viewTypeListener!=null){
			T t = mAdapterOptions.getData().get(position);
			return viewTypeListener.getItemViewType(t,position);
		}
		return 0;
	}

	@Override
	public View getView(int position, int itemViewType, View itemView, ViewGroup parent) {
		int layoutId=getItemViewLayoutId(itemViewType);
		CommonViewHolder commonViewHolder =CommonViewHolder.getCommonViewHolder(getContext(parent), itemView, parent, layoutId,itemViewType, position);
		int oldPosition = commonViewHolder.getOldPosition();
		if(oldPosition!=-1){
			mCurrentViewHolderServices.remove(oldPosition);
		}
		ViewHolderService viewHolderService = commonViewHolder.getViewHolderService();
		onBindViewHolder(viewHolderService,position);
		mCurrentViewHolderServices.put(position,viewHolderService);
		return viewHolderService.getItemView();
	}

	@Override
	public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(getContext(parent)).inflate(getItemViewLayoutId(viewType), parent, false);
		RecyclerViewViewHolder viewViewHolder = new RecyclerViewViewHolder(view);
		return viewViewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolderService service, int position) {
		List<T> datas = mAdapterOptions.getData();
		if(position<datas.size()){
			mAdapterOptions.getOnBindViewHolderListener().onBindViewHolder(service,datas,datas.get(position),position);
		}
	}
	private Context getContext(View view){
		if(view!=null){
			return view.getContext();
		}
		return mAdapterOptions.getContext();
	}
	public void updateItemView(int position){
		updateItemView(position,null);
	}

	public void updateItemView(int position,T t){
		List<T> datas = mAdapterOptions.getData();
		ViewHolderService viewHolderService = mCurrentViewHolderServices.get(position);
		if(datas.size()>position && viewHolderService!=null){
			if(t==null){
				t=datas.get(position);
			}else{
				datas.set(position,t);
			}
			mAdapterOptions.getOnBindViewHolderListener().onBindViewHolder(viewHolderService, datas, t, position);
		}
	}

	public void removeViewHolderService(int position){
		mCurrentViewHolderServices.remove(position);
	}
}
