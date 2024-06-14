package pers.spj.adapter.androidx.service;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author supeijin
 */
public interface ViewHolderService {
	<T extends View> T getItemView();
	<T extends View> T getView(int viewId);
	int getItemViewType();
	int getAdapterPosition();

	<T extends View> T setVisibility(int viewId,int visibility);
	<T extends View> T setVisible(int viewId);
	<T extends View> T setInvisible(int viewId);
	<T extends View> T setGone(int viewId);
	<T extends View> T setAlpha(int viewId,float alpha);
	<T extends View> T setSelected(int viewId,boolean selected);
	<T extends View> T setBackgroundColor(int viewId, int color);
	<T extends View> T setBackgroundResource(int viewId,int resid);

	<T extends TextView> T setText(int viewId, CharSequence text);
	<T extends TextView> T setTextColor(int viewId, int color);

	<T extends ImageView> T setImageResource(int viewId, int resId);
	<T extends ImageView> T setImageURI(int viewId, Uri uri);
	<T extends ImageView> T setImageDrawable(int viewId,Drawable drawable);
	<T extends ImageView> T setImageBitmap(int viewId,Bitmap bm);

	<T extends View> T setOnClickListener(int viewId, View.OnClickListener listener);
	<T extends View> T setOnLongClickListener(int viewId,View.OnLongClickListener listener);

}
