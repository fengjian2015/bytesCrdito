package pers.spj.adapter.androidx.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
final class CommonViewHolder  {
	private ViewHolderServiceImpl mViewHolderService;
	private int mOldPosition=-1;
	public static CommonViewHolder getCommonViewHolder(Context context, View itemView, ViewGroup parent, int layoutId, int itemViewType, int position) {
		if (itemView == null) {
			return new CommonViewHolder(context, parent, layoutId, itemViewType, position);
		}
		int holderKey = layoutId << 2;
		CommonViewHolder viewHolder = (CommonViewHolder) itemView.getTag(holderKey);
		viewHolder.setOldPosition(viewHolder.getAdapterPosition());
		viewHolder.setAdapterPosition(position);
		viewHolder.setItemViewType(itemViewType);
		return viewHolder;
	}
	private CommonViewHolder() {
	}

	private CommonViewHolder(Context context, ViewGroup parent, int layoutId, int itemViewType, int position) {
		View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		int holderKey = layoutId << 2;
		itemView.setTag(holderKey, this);
		mViewHolderService = new ViewHolderServiceImpl(itemView, itemViewType, position);
	}

	public void setAdapterPosition(int adapterPosition) {
		this.mViewHolderService.setPosition(adapterPosition);
	}

	public void setItemViewType(int itemViewType) {
		this.mViewHolderService.setItemViewType(itemViewType);
	}
	public int getAdapterPosition() {
		return this.mViewHolderService.getAdapterPosition();
	}

	protected void setOldPosition(int oldPosition) {
		mOldPosition = oldPosition;
	}

	public int getOldPosition() {
		return mOldPosition;
	}

	public ViewHolderService getViewHolderService() {
		return this.mViewHolderService;
	}
}
