package com.software.feng.bytescrdito.adapter4androidx;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.software.feng.bytescrdito.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pers.spj.adapter.androidx.common.SimpleAdapterFactory;
import pers.spj.adapter.androidx.common.SimpleRecyclerViewAdapter;
import pers.spj.adapter.androidx.service.ViewHolderService;
import pers.spj.custom.view.recyclerview.SwipeRefresh;
import pers.spj.custom.view.recyclerview.SwipeRefreshRecyclerView;


/**
 * @author bluefox
 * @date 2018/1/8.
 */

public class SwipeRefreshRecyclerViewActivity extends FragmentActivity {
	private SwipeRefreshRecyclerView mRefreshRecyclerView;
	private List<String> mDatas = new ArrayList<>();
	private SimpleRecyclerViewAdapter<String> mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe_refresh_recycler_view_activity);
		initDatas();
		initView();
	}

	private void initDatas() {
		for (int i = 0; i <100 ; i++) {
			mDatas.add(String.valueOf(i+1));
		}
	}

	private void initView() {
		mRefreshRecyclerView=findViewById(R.id.id_recyclerView);
		mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = SimpleAdapterFactory.createRecyclerViewAdapter(this, R.layout.item_1, mDatas,
				(holder, list, item, position) -> onBindTestItem(holder, list, item, position));
		mRefreshRecyclerView.setAdapter(mAdapter);
		SwipeRefresh swipeRefresh = new SwipeRefresh();
		mRefreshRecyclerView.setOnRefreshListener(swipeRefresh);
		swipeRefresh.setOnRefreshListener(new SwipeRefresh.OnRefSwipeRefreshListener() {
			@Override
			public void onRefresh() {
				System.out.println("onRefresh() ");
				Observable.just(2)
						.delay(5, TimeUnit.SECONDS)
						.subscribeOn(Schedulers.io())
						.unsubscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.map(new Function<Integer, List<String>>() {

							@Override
							public List<String> apply(Integer integer) throws Exception {
								Random random = new Random();
								ArrayList<String> strings = new ArrayList<>();
								for (int i = 1; i <= 50; i++) {
									strings.add(String.valueOf(random.nextInt(100))+ " item"+i);
								}
								return strings;
							}
						})
						.subscribe(new Consumer<List<String>>() {

							@Override
							public void accept(List<String> list) throws Exception {
								mDatas.clear();
								mDatas.addAll(list);
								mAdapter.notifyDataSetChanged();
								mRefreshRecyclerView.setRefreshing(false);
							}
						});
			}
		});
	}
	private void onBindTestItem(ViewHolderService holder, List<String> list, String item, int position) {
		holder.setText(R.id.id_title, item);
	}
}
