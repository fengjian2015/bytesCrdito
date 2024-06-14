package pers.spj.adapter.androidx.common;

import android.view.ViewGroup;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import pers.spj.adapter.androidx.listener.OnGetFragmentListener;
import pers.spj.adapter.androidx.service.FagmentPagerAdapterService;

/**
 * @author spj
 */

public class SimpleFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter
		implements FagmentPagerAdapterService<T> {
    private FagmentPagerAdapterServiceImpl<T> mFagmentPagerAdaterService;

	private SimpleFragmentStatePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public SimpleFragmentStatePagerAdapter(FragmentManager fm, Integer pageSize) {
		super(fm);
		mFagmentPagerAdaterService = new FagmentPagerAdapterServiceImpl<>(pageSize);;
	}

	public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<T> datas) {
		super(fm);
		mFagmentPagerAdaterService = new FagmentPagerAdapterServiceImpl<>(datas);
	}

	@Override
	public Fragment getItem(int position) {
		return mFagmentPagerAdaterService.getItemFragment(position);
	}

	@Override
	public int getCount() {
		return mFagmentPagerAdaterService.getCount();
	}

	@Override
	public Fragment instantiateItem(ViewGroup container, int position) {
		Fragment fragment= (Fragment) super.instantiateItem(container, position);
		mFagmentPagerAdaterService.instantiateItem(fragment,position);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFagmentPagerAdaterService.destroyItem(position);
	}

	@Override
	public float getPageWidth(int position) {
		return mFagmentPagerAdaterService.getPageWidth(position);
	}

	@Override
	public void setPageWidth(float pageWidth) {
		mFagmentPagerAdaterService.setPageWidth(pageWidth);
	}

	@Override
	public void setPageWidth(int position, float pageWidth) {
		mFagmentPagerAdaterService.setPageWidth(position,pageWidth);
	}

	@Override
	public long getItemId(int position) {
		return mFagmentPagerAdaterService.getItemId(position);
	}

	@Override
	public Fragment findFragmentByPosition(int position) {
		return mFagmentPagerAdaterService.findFragmentByPosition(position);
	}

	@Override
	public void setOnGetFragmentListener(OnGetFragmentListener<T> listener) {
		mFagmentPagerAdaterService.setOnGetFragmentListener(listener);
	}
}
