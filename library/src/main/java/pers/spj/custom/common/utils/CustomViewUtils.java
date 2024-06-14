package pers.spj.custom.common.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author bluefox
 * @date 2023/7/20.
 */

public class CustomViewUtils {
   public static void setMargins(View view, int left, int top, int right, int bottom){
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if(layoutParams instanceof LinearLayout.LayoutParams){
         LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutParams;
         params.setMargins(left,top,right,bottom);
      } else  if(layoutParams instanceof FrameLayout.LayoutParams){
         FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
         params.setMargins(left,top,right,bottom);
      }else  if(layoutParams instanceof RelativeLayout.LayoutParams){
         RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutParams;
         params.setMargins(left,top,right,bottom);
      }
   }

}
