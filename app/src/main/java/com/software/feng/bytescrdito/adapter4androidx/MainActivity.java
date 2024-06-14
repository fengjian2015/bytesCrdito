package com.software.feng.bytescrdito.adapter4androidx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.software.feng.bytescrdito.R;

public class MainActivity extends FragmentActivity {
	private TextView mBase;
	private TextView mLoadMore;
	private TextView mSwipeRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initView();
	}

	private void initView() {
		mBase = (TextView) findViewById(R.id.base);
		mLoadMore = (TextView) findViewById(R.id.load_more);
		mSwipeRefresh = (TextView) findViewById(R.id.swipe_refresh);
		mBase.setOnClickListener(v->{
			toActivity(BaseSampleActivity.class);
		});
		mLoadMore.setOnClickListener(v->{
			toActivity(LoadMoreActivity.class);
		});
		mSwipeRefresh.setOnClickListener(v->{
			toActivity(SwipeRefreshRecyclerViewActivity.class);
		});
	}
	private void toActivity(Class<? extends Activity> cls) {
		startActivity(new Intent(this, cls));
	}
}
