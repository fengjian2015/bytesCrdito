package pers.spj.adapter.androidx.listener;

/**
 * @author supeijin
 */
public interface OnGetItemViewTypeListener<T> {
	int getItemViewType(T t,int position);
}
