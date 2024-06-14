package pers.spj.custom.common.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.Pair;
import android.view.View;

import pers.spj.custom.common.helper.FullyLinearLayoutManager;
import pers.spj.custom.common.helper.WrapGridLayoutManager;
import pers.spj.custom.common.helper.WrapLinearLayoutManager;
import pers.spj.custom.common.helper.FullyGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author supeijin
 */

public final class RecyclerViewUtils {
	private RecyclerViewUtils() {
		//no instance
	}

	public static LinearLayoutManager initLinearLayoutManager(Context context,RecyclerView recyclerView){
		return initLinearLayoutManager(context,recyclerView,LinearLayoutManager.VERTICAL);
	}

	public static LinearLayoutManager initLinearLayoutManager(Context context,RecyclerView recyclerView,@Orientation int orientation){
		WrapLinearLayoutManager layoutManager = new WrapLinearLayoutManager(context);
		layoutManager.setOrientation(orientation);
		recyclerView.setLayoutManager(layoutManager);
		return layoutManager;
	}

	public static GridLayoutManager initGridLayoutManager(Context context, RecyclerView recyclerView,int spanCount){
		return initGridLayoutManager(context,recyclerView, LinearLayoutManager.VERTICAL,spanCount);
	}

	public static GridLayoutManager initGridLayoutManager(Context context, RecyclerView recyclerView,@Orientation int orientation,int spanCount ){
		WrapGridLayoutManager layoutManager = new WrapGridLayoutManager(context,spanCount);
		layoutManager.setOrientation(orientation);
		recyclerView.setLayoutManager(layoutManager);
		return layoutManager;
	}

	public static StaggeredGridLayoutManager initStaggeredGridLayoutManager(RecyclerView recyclerView, @Orientation int orientation, int spanCount ){
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount,orientation);
		layoutManager.setOrientation(orientation);
		recyclerView.setLayoutManager(layoutManager);
		return layoutManager;
	}

	public static FullyLinearLayoutManager initFullyLinearLayoutManager(Context context, RecyclerView recyclerView){
		FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(context);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		return layoutManager;
	}

	public static GridLayoutManager initFullyGridLayoutManager(Context context,RecyclerView recyclerView,int spanCount){
		FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(context, spanCount);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		return layoutManager;
	}
	/**
	 * 记录RecyclerView当前位置
	 */
	public static Pair<Integer,Integer> getPositionAndOffset(RecyclerView recyclerView) {
		LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
		//获取可视的第一个view
		View topView = layoutManager.getChildAt(0);
		if(topView != null) {
			//获取与该view的顶部的偏移量
			int lastOffset = topView.getTop();
			//得到该View的数组位置
			int  lastPosition = layoutManager.getPosition(topView);
			return new Pair<>(lastPosition,lastOffset);
		}
		return null;
	}

	/**
	 * 让RecyclerView滚动到指定位置
	 */
	public static void scrollToPosition(RecyclerView recyclerView,int lastPosition,int lastOffset) {
		if(recyclerView.getLayoutManager() != null && lastPosition >= 0) {
			((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
		}
	}

	public static void addItemDecoration(final RecyclerView recyclerView,final int itemDecoration){
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				super.getItemOffsets(outRect, view, parent, state);
				RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
				int orientation=RecyclerView.VERTICAL;
				int spaceCount=1;
				if(manager instanceof LinearLayoutManager){
					LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
					orientation = layoutManager.getOrientation();
				}else if(manager instanceof StaggeredGridLayoutManager ){
					StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
					orientation = layoutManager.getOrientation();
					spaceCount = layoutManager.getSpanCount();
				}else if(manager instanceof GridLayoutManager ){
					GridLayoutManager layoutManager = (GridLayoutManager) manager;
					orientation = layoutManager.getOrientation();
					spaceCount = layoutManager.getSpanCount();
				}
				int position = parent.getChildAdapterPosition(view);
				System.out.println("position=="+position +" spaceCount="+spaceCount);
				if(spaceCount==1 || position>spaceCount-1){
					if(orientation==LinearLayoutManager.HORIZONTAL){
						outRect.left=itemDecoration;
					}else{
						outRect.top=itemDecoration;
					}
				}
			}
		});
	}

	@IntDef({LinearLayoutManager.VERTICAL,LinearLayoutManager.HORIZONTAL})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Orientation{}
}
