package pers.spj.custom.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import pers.spj.custom.view.util.FontManager;


public class CusTextView extends AppCompatTextView {

	public CusTextView(Context context) {
		super(context);
        FontManager.getInstance().applyTypeface(this);
	}
	public CusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontManager.getInstance().applyTypeface(this);
    }
    public CusTextView(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        FontManager.getInstance().applyTypeface(this);
    }
}
