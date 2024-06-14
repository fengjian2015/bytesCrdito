package pers.spj.custom.view;

import android.content.Context;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatEditText;
import pers.spj.custom.view.util.FontManager;


public class CusEditText extends AppCompatEditText {
/**
 * 添加了过滤器，所以会和android:maxLength起冲突，如果要设置最大距离，需要动态设置
 * */
	public CusEditText(Context context) {
		super(context);
        FontManager.getInstance().applyTypeface(this);
	}
	public CusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontManager.getInstance().applyTypeface(this);
    }

    public CusEditText(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        FontManager.getInstance().applyTypeface(this);
    }
}
