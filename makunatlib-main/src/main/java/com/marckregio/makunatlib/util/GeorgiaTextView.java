package com.marckregio.makunatlib.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created by makregio on 27/01/2017.
 */

public class GeorgiaTextView extends AppCompatTextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public GeorgiaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public GeorgiaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {

        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        switch (textStyle) {
            case Typeface.NORMAL:
                return setNormalTypeFace(context);
            default:
                return setNormalTypeFace(context);
        }
    }

    private Typeface setNormalTypeFace(Context context){
        String fontPath = "fonts/Georgia.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    private Typeface setThinTypeFace(Context context){
        String fontPath = "fonts/Roboto-Light.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    private Typeface setBoldTypeFace(Context context){
        String fontPath = "fonts/Roboto-Bold.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }


    private Typeface setMediumTypeFace(Context context){
        String fontPath = "fonts/Roboto-Medium.ttf";
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }
}
