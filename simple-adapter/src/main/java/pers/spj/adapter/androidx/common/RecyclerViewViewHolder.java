package pers.spj.adapter.androidx.common;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
final class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
	private ViewHolderServiceImpl mViewHolderService;
	public RecyclerViewViewHolder(View itemView) {
		super(itemView);
		mViewHolderService = new ViewHolderServiceImpl(itemView,super.getItemViewType(),super.getAdapterPosition());
	}

	public ViewHolderService getViewHolderService() {
		this.mViewHolderService.setPosition(super.getAdapterPosition());
		this.mViewHolderService.setItemViewType(super.getItemViewType());
		return this.mViewHolderService;
	}
}
