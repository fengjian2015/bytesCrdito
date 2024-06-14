package com.software.feng.bytescrdito.adapter4androidx;

import android.os.Bundle;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.software.feng.bytescrdito.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pers.spj.adapter.androidx.common.SimpleAdapterFactory;
import pers.spj.adapter.androidx.common.SimpleRecyclerViewAdapter;
import pers.spj.adapter.androidx.service.ViewHolderService;
import pers.spj.custom.view.recyclerview.ExpRecyclerView;
import pers.spj.custom.common.utils.RecyclerViewUtils;


public class LoadMoreActivity extends FragmentActivity {
	private List<String> mDatas = new ArrayList<>();
	private ExpRecyclerView mERecyclerview;
	private SimpleRecyclerViewAdapter<String> mAdapter;
	private int pageCount=1;
	private int totalPageCount=5;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadmore);
		initDatas();
		mERecyclerview=findViewById(R.id.id_recyclerView);
		mERecyclerview.setLoadMoreEnabled(true);
		mERecyclerview.setOnLoadMreListener(()->loadMoreEvent());
		RecyclerViewUtils.initGridLayoutManager(this,mERecyclerview, LinearLayoutManager.VERTICAL,3);
		mAdapter = SimpleAdapterFactory.createRecyclerViewAdapter(this, R.layout.item_1, mDatas,
				(holder, list, item, position) -> onBindTestItem(holder, list, item, position));
		mERecyclerview.setAdapter(mAdapter);
	}
	private void loadMoreEvent() {
		if(pageCount>=totalPageCount){
			mERecyclerview.setDisableLoadMore();
			Toast.makeText(this,"disable load more",Toast.LENGTH_SHORT).show();
			return ;
		}
		pageCount++;
		Toast.makeText(this,"loadMore",Toast.LENGTH_SHORT).show();
		System.out.println("loadMoreEvent:"+mDatas.size());
		Observable.just(20)
				.subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(integer -> getDatas(integer))
				.delaySubscription(3, TimeUnit.SECONDS,AndroidSchedulers.mainThread())
				.subscribe((list -> updateDatas(list)));
	}

	private List<String> getDatas(Integer integer) {
		System.out.println("getDatas time="+ System.currentTimeMillis());
		int size = mDatas.size();
		ArrayList<String> strings = new ArrayList<>();
		for (int i = 0; i <integer ; i++) {
			strings.add("追加"+(size+i));
		}
		return strings;
	}

	private void updateDatas(List<String> list) {

		System.out.println("updateDatas time="+ System.currentTimeMillis());
		System.out.println("Name Thread.currentThread().getName() ="+Thread.currentThread().getName());
		mERecyclerview.loadMoreCompeleted();
		mDatas.addAll(list);
		mAdapter.notifyDataSetChanged();
	}
	private void onBindTestItem(ViewHolderService holder, List<String> list, String item, int position) {
		holder.setText(R.id.id_title, item);
	}
	private void initDatas() {
		for (int i = 0; i < 10; i++) {
			mDatas.add(String.valueOf(i + 1));
		}
	}
}
