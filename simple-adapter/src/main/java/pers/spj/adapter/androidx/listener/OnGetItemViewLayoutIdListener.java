package pers.spj.adapter.androidx.listener;

/**
 * @author supeijin
 */
public interface OnGetItemViewLayoutIdListener {
	/**
	 * 根据类型获取布局id
	 * @param itemViewType
	 * @return
	 */
	int getItemTypeLayoutId(int itemViewType);
}
