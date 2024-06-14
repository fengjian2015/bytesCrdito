package pers.spj.custom.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import pers.spj.custom.view.util.FontManager;
public class CusButton extends AppCompatButton {
	public CusButton(Context context) {
		super(context);

		FontManager.getInstance().applyButtonTypeface(this);
	}
	public CusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontManager.getInstance().applyButtonTypeface(this);
    }

    public CusButton(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        FontManager.getInstance().applyButtonTypeface(this);
    }
}
