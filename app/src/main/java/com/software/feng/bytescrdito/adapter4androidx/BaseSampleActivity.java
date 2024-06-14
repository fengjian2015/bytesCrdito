package com.software.feng.bytescrdito.adapter4androidx;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.software.feng.bytescrdito.R;

import pers.spj.adapter.androidx.common.SimpleAdapterFactory;
import pers.spj.adapter.androidx.common.SimpleRecyclerViewAdapter;
import pers.spj.adapter.androidx.service.ViewHolderService;
import pers.spj.custom.view.recyclerview.ExpRecyclerView;
import pers.spj.custom.common.utils.RecyclerViewUtils;


public class BaseSampleActivity extends Activity implements View.OnClickListener {
	private ExpRecyclerView mERecyclerview;
	private List<String> mDatas = new ArrayList<>();
	private SimpleRecyclerViewAdapter<String> mAdapter;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initDatas();
		initView();
	}

	private void initView() {
		setOnClick(R.id.id_add_header_button);
		setOnClick(R.id.id_add_footer_button);
		setOnClick(R.id.id_fill_data_button);
		setOnClick(R.id.id_clear_button);
		mERecyclerview=findViewById(R.id.id_recyclerView);
		mERecyclerview.addHeaderView(getHeaderView());
		mERecyclerview.addHeaderView(getHeaderView());
		mERecyclerview.addFooterView(getFooterView());
		mERecyclerview.addFooterView(getFooterView());
		mERecyclerview.addFooterView(getFooterView());
		mERecyclerview.addEmptyView(getEmptyView());
		RecyclerViewUtils.initGridLayoutManager(this,mERecyclerview, LinearLayoutManager.VERTICAL,3);
		RecyclerViewUtils.addItemDecoration(mERecyclerview,10);
		mAdapter = SimpleAdapterFactory.createRecyclerViewAdapter(this, R.layout.item_1, mDatas,
				(holder, list, item, position) -> onBindTestItem(holder, list, item, position));
		mERecyclerview.setAdapter(mAdapter);
	}

	private View getEmptyView() {
		View inflate = LayoutInflater.from(this).inflate(R.layout.test_empty, null);
		inflate.setOnClickListener(v -> {
			initDatas();
			mAdapter.notifyDataSetChanged();
		});

		return inflate;
	}

	private View getFooterView() {
		View view = LayoutInflater.from(this).inflate(R.layout.footer_view_1, null);
		view.setOnClickListener(v ->
				{
					mERecyclerview.removeFooterView(view);
					mAdapter.notifyDataSetChanged();
				}
				);
		return view;
	}

	private View getHeaderView() {
		final View view= LayoutInflater.from(this).inflate(R.layout.header_view_1,null);
		view.setOnClickListener(v -> {
			mERecyclerview.removeHeaderView(view);
			mAdapter.notifyDataSetChanged();
		});
		return view;
	}

	private void initDatas() {
		for (int i = 0; i <30; i++) {
			mDatas.add(String.valueOf(i+1));
		}
	}
	private void onBindTestItem(ViewHolderService holder, List<String> list, String item, int position) {
		holder.setText(R.id.id_title,item);
		holder.getItemView().setOnClickListener(v -> updateItem(position));
	}

	private void updateItem(int position) {
		Toast.makeText(this,"position="+position,Toast.LENGTH_SHORT).show();
		int nextInt = new Random().nextInt(1000);
		mDatas.set(position,"测试更新 随机数= "+  nextInt);
		mAdapter.notifyItemChanged(position);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.id_add_header_button){
			mERecyclerview.addHeaderView(getHeaderView());
			mAdapter.notifyItemRangeInserted(0,1);
			mERecyclerview.smoothScrollToPosition(0);
		}else if(v.getId()==R.id.id_add_footer_button){
			mERecyclerview.addFooterView(getFooterView());
			int position = mAdapter.getItemCount() - 1;
			mAdapter.notifyItemRangeInserted(position,1);
			mERecyclerview.smoothScrollToPosition(position+1);
		}else if(v.getId()==R.id.id_fill_data_button){
			initDatas();
			mAdapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.id_clear_button){
			mDatas.clear();
			mAdapter.notifyDataSetChanged();
		}
	}
	private void setOnClick(int viewId){
		View view = findViewById(viewId);
		if(view!=null){
			view.setOnClickListener(this);
		}
	}
}
