package com.patelapp.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.patelapp.R;

/**
 * Created by AndroidDevloper on 4/11/2016.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            String font_name = a.getString(R.styleable.CustomTextView_custom_font);

            if (font_name != null) {
                Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + font_name);
                setTypeface(mTypeface);

            }
            a.recycle();

        }

    }
}
