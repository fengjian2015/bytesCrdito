package pers.spj.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;
import pers.spj.custom.view.util.FontManager;
public class CusCheckBox extends AppCompatCheckBox {

	public CusCheckBox(Context context) {
		super(context);
        FontManager.getInstance().applyTypeface(this);
	}
	public CusCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontManager.getInstance().applyTypeface(this);
    }

    public CusCheckBox(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        FontManager.getInstance().applyTypeface(this);
    }
}
