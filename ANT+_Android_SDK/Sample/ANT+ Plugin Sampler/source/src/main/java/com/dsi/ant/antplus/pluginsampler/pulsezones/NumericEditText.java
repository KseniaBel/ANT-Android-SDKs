package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dsi.ant.antplus.pluginsampler.R;

import java.util.function.Function;

/**
 * Created by ksenia on 06.01.19.
 */

public class NumericEditText extends EditText {
    private int minValueAttr;
    private int maxValueAttr;

    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public NumericEditText(Context context) {
        super(context);
    }

    public NumericEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumericEditText);
        minValueAttr = a.getInt(R.styleable.NumericEditText_min_value, 1);
        maxValueAttr = a.getInt(R.styleable.NumericEditText_max_value, 130);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable value) {
                isValid = true;
                if (TextUtils.isEmpty(value) || Integer.parseInt(value.toString()) < minValueAttr || Integer.parseInt(value.toString()) > maxValueAttr) {
                    setError("The value should be between " + minValueAttr + " and " + maxValueAttr);
                    isValid = false;
                }
            }
        });
    }

    public NumericEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NumericEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

}
