package pers.spj.adapter.androidx.common;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import pers.spj.adapter.androidx.listener.OnBindViewHolderListener;
import pers.spj.adapter.androidx.service.AdapterBuilderService;

/**
 * @author supeijin
 */
public final class SimpleAdapterFactory {
	private SimpleAdapterFactory() {
	}

	public static <T> SimpleFragmentPagerAdapter<T> createSimpleFragmentPagerAdapter(FragmentManager fragmentManager, List<T> data) {
		return new SimpleFragmentPagerAdapter(fragmentManager, data);
	}

	public static <T> SimpleFragmentPagerAdapter<T> createSimpleFragmentPagerAdapter(FragmentManager fragmentManager, int pageSize) {
		return new SimpleFragmentPagerAdapter(fragmentManager, pageSize);
	}

	public static <T> SimpleFragmentStatePagerAdapter<T> createSimpleFragmentStatePagerAdapter(FragmentManager fragmentManager, List<T> data) {
		return new SimpleFragmentStatePagerAdapter(fragmentManager, data);
	}

	public static <T> SimpleFragmentStatePagerAdapter<T> createSimpleFragmentStatePagerAdapter(FragmentManager fragmentManager, int pageSize) {
		return new SimpleFragmentStatePagerAdapter(fragmentManager, pageSize);
	}

	public static <T> SimpleBaseAdapter<T> createBaseAdapter(@NonNull Context context, @NonNull int defaultItemId, @NonNull List<T> data, OnBindViewHolderListener<T> listener) {
		return createSimpleAdapter(context, Builder.<T>getBaseBuildService(), defaultItemId, data, listener);
	}

	public static <T> SimplePagerAdapter<T> createPagerAdapter(@NonNull Context context, @NonNull int defaultItemId, @NonNull List<T> data, OnBindViewHolderListener<T> listener) {
		return createSimpleAdapter(context,Builder.<T>getPagerBuilderService(), defaultItemId, data, listener);
	}

	public static <T> SimpleRecyclerViewAdapter<T> createRecyclerViewAdapter(@NonNull Context context, @NonNull int defaultItemId, @NonNull List<T> data, OnBindViewHolderListener<T> listener) {
		return createSimpleAdapter(context, Builder.<T>getRecyclerBuilderService(), defaultItemId, data, listener);
	}

    private static <T, R> R  createSimpleAdapter(@NonNull Context context, AdapterBuilderService<T,R> builder, @NonNull int defaultItemId, @NonNull List<T> data, OnBindViewHolderListener<T> listener){
		builder.setContext(context)
				.setData(data)
				.setDefaultLayoutResId(defaultItemId)
				.setOnBindViewHolderListener(listener);
		return builder.build();
	}
	
	public final static class Builder{
		public static <T> AdapterBuilderService<T, SimpleBaseAdapter<T>> getBaseBuildService() {
			return SimpleBaseAdapter.Builder.newInstance();
		}

		public static <T> AdapterBuilderService<T, SimpleRecyclerViewAdapter<T>> getRecyclerBuilderService() {
			return SimpleRecyclerViewAdapter.Builder.newInstance();
		}

		public static <T> AdapterBuilderService<T, SimplePagerAdapter<T>> getPagerBuilderService() {
			return SimplePagerAdapter.Builder.newInstance();
		}
	}
}
