package pers.spj.adapter.androidx.common;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
final class ViewHolderServiceImpl implements ViewHolderService {
	private final SparseArray<View> mViews;
	private View mItemView;
	private int mItemViewType;
	private int mPosition;
	public ViewHolderServiceImpl(View itemView, int itemViewType, int position) {
		this.mItemView=itemView;
		this.mItemViewType = itemViewType;
		this.mPosition=position;
		mViews = new SparseArray<>();
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public void setItemViewType(int itemViewType) {
		mItemViewType = itemViewType;
	}

	@Override
	public <T extends View> T getItemView() {
		return (T) this.mItemView;
	}

	@Override
	public <T extends View> T getView(int viewId) {
		T t = (T) mViews.get(viewId);
		if(t==null){
			t= (T) this.mItemView.findViewById(viewId);
			mViews.put(viewId,t);
		}
		return t;
	}

	@Override
	public int getItemViewType() {
		return this.mItemViewType;
	}

	@Override
	public int getAdapterPosition() {
		return this.mPosition;
	}

	@Override
	public <T extends View> T setVisibility(int viewId, int visibility) {
		T t = getView(viewId);
		if(t!=null){
			t.setVisibility(visibility);
		}
		return t;
	}

	@Override
	public <T extends View> T setVisible(int viewId) {
		return this.setVisibility(viewId,View.VISIBLE);
	}

	@Override
	public <T extends View> T setInvisible(int viewId) {
		return this.setVisibility(viewId,View.INVISIBLE);
	}

	@Override
	public <T extends View> T setGone(int viewId) {
		return this.setVisibility(viewId,View.GONE);
	}

	@Override
	public <T extends View> T setAlpha(int viewId, float alpha) {
		T t = getView(viewId);
		if(t!=null){
			t.setAlpha(alpha);
		}
		return t;
	}

	@Override
	public <T extends View> T setSelected(int viewId,boolean selected) {
		T t = getView(viewId);
		if(t!=null){
			t.setSelected(selected);
		}
		return t;
	}

	@Override
	public <T extends View> T setBackgroundColor(int viewId, int color) {
		T t = getView(viewId);
		if(t!=null){
			t.setBackgroundColor(color);
		}
		return t;
	}

	@Override
	public <T extends View> T setBackgroundResource(int viewId, int resid) {
		T t = getView(viewId);
		if(t!=null){
			t.setBackgroundResource(resid);
		}
		return t;
	}

	@Override
	public <T extends TextView> T setText(int viewId, CharSequence text) {
		TextView view = getView(viewId);
		if(view!=null){
			if (!TextUtils.isEmpty(text)) {
				view.setText(text);
			} else {
				view.setText("");
			}
			return (T) view;
		}
		return null;

	}

	@Override
	public <T extends TextView> T setTextColor(int viewId, int color) {
		TextView view = getView(viewId);
		if(view!=null){
			view.setTextColor(color);
		}
		return (T) view;
	}

	@Override
	public <T extends ImageView> T setImageResource(int viewId, int resId) {
		ImageView imageView = getView(viewId);
		if(imageView!=null){
			imageView.setImageResource(resId);
		}
		return (T) imageView;
	}

	@Override
	public <T extends ImageView> T setImageURI(int viewId, Uri uri) {
		ImageView imageView = getView(viewId);
		if(imageView!=null){
			imageView.setImageURI(uri);
		}
		return (T) imageView;
	}

	@Override
	public <T extends ImageView> T setImageDrawable(int viewId, Drawable drawable) {
		ImageView imageView = getView(viewId);
		if(imageView!=null){
			imageView.setImageDrawable(drawable);
		}
		return (T) imageView;
	}

	@Override
	public <T extends ImageView> T setImageBitmap(int viewId, Bitmap bm) {
		ImageView imageView = getView(viewId);
		if(imageView!=null){
			imageView.setImageBitmap(bm);
		}
		return (T) imageView;
	}

	@Override
	public <T extends View> T setOnClickListener(int viewId, View.OnClickListener listener) {
		T t = getView(viewId);
		if(t!=null){
			t.setOnClickListener(listener);
		}
		return t;
	}

	@Override
	public <T extends View> T setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		T t = getView(viewId);
		if(t!=null){
			t.setOnLongClickListener(listener);
		}
		return t;
	}
}
