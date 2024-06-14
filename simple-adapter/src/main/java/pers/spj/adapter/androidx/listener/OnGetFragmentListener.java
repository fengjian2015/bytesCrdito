package pers.spj.adapter.androidx.listener;

import androidx.fragment.app.Fragment;

/**
 * @author spj
 */
public interface OnGetFragmentListener<T> {
	/**
	 * 根据位置、以及当前位置携带的信息获取 显示的fragment
	 * @param position 所在位置
	 * @param t   输入数据类型的当前位置信息实体
	 * @return 返回 Fragment{@link Fragment}
	 */
	Fragment getItemFragment(int position, T t);
}
