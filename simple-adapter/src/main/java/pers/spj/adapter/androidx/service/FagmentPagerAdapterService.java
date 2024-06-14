package pers.spj.adapter.androidx.service;

import androidx.fragment.app.Fragment;
import pers.spj.adapter.androidx.listener.OnGetFragmentListener;

/**
 * FagmentPagerAdater相关操作接口
 * @author spj
 */
public interface FagmentPagerAdapterService<T> {
	/**
	 * 设置全局性的页面宽度
	 * @param pageWidth 页面宽度
	 */
	void setPageWidth(float pageWidth);

	/**
	 * 设置特定位置页面宽度
	 * @param position 页面所在位置
	 * @param pageWidth 宽度值
	 */
	void setPageWidth(int position, float pageWidth);

	/**
	 * 获取当前位置的id
	 * @param position 位置信息
	 * @return
	 */
	long getItemId(int position);

	/**
	 * 通过位置获取实例化后的fragment；找不到的则返回空
	 * @param position 所在的位置
	 * @return 返回fragment 实例
	 */
	Fragment findFragmentByPosition(int position);

	/**
	 * 设置获取fragment 接口 ，携带位置绑定相关信息
	 * @param onGetFragmentListener {@link OnGetFragmentListener <T> } 泛型即为绑定数据的类型
	 */
	void setOnGetFragmentListener(OnGetFragmentListener<T> onGetFragmentListener);
}
