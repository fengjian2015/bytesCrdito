package pers.spj.adapter.androidx.common;

import android.util.SparseArray;

import java.util.List;

import androidx.fragment.app.Fragment;
import pers.spj.adapter.androidx.listener.OnGetFragmentListener;
import pers.spj.adapter.androidx.service.FagmentPagerAdapterService;

/**
 * FragmentStatePagerAdapter相关辅助工具，帮助初始化
 * @author bluefox
 * @date 2017/12/26.
 */

final class FagmentPagerAdapterServiceImpl<T> implements FagmentPagerAdapterService<T> {
	/**
	 * 保存全局的页面宽度信息
	 */
    private float mPageWidth;
	/**
	 * 保存特定位置的宽度信息
	 */
	private SparseArray<Float> mPageWidths;
    private boolean specialItem;

    private int mPageSize;
    private List<T> mDatas;
    private OnGetFragmentListener<T> mOnGetFragmentListener;
    private SparseArray<Fragment> mFragments;

	public FagmentPagerAdapterServiceImpl(int pageSize) {
		mPageSize = pageSize;
		mDatas=null;
		initHelper();
	}

	public FagmentPagerAdapterServiceImpl(List<T> datas) {
		mPageSize=0;
		mDatas = datas;
		initHelper();
	}

	private void initHelper() {
		specialItem=false;
		mPageWidth=1.0f;
		mPageWidths = new SparseArray<>();
		mFragments = new SparseArray<>();
	}

	public Fragment getItemFragment(int position) {
		if(mOnGetFragmentListener!=null){
			if(mDatas!=null && mDatas.size()>position){
				return mOnGetFragmentListener.getItemFragment(position,mDatas.get(position));
			}else {
				return mOnGetFragmentListener.getItemFragment(position,null);
			}
		}
		return null;
	}

	public int getCount() {
		return mDatas==null?mPageSize:mDatas.size();
	}
	public void instantiateItem(Fragment fragment, int position){
		mFragments.put(position,fragment);
	}
	public void destroyItem(int position) {
		mFragments.remove(position);
	}
	public float getPageWidth(int position) {
		return specialItem?mPageWidths.get(position,mPageWidth):mPageWidth;
	}

	@Override
	public void setPageWidth(float pageWidth){
		mPageWidth=pageWidth;
	}
	@Override
	public void setPageWidth(int position, float pageWidth){
		specialItem=true;
		mPageWidths.put(position,pageWidth);
	}


	@Override
	public void setOnGetFragmentListener(OnGetFragmentListener<T> onGetFragmentListener) {
		mOnGetFragmentListener = onGetFragmentListener;
	}
	/**
	 * 通过位置寻找fragment
	 * @param position
	 * @return 返回指定位置的fragment ，当不存在时返回null
	 */
	@Override
	public Fragment findFragmentByPosition(int position){
		return mFragments.get(position);
	}

	@Override
	public long getItemId(int position){
		return mDatas==null?position:mDatas.get(position).hashCode();
	}
}
