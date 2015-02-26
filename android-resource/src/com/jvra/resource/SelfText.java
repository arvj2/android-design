package com.jvra.resource;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 2/18/2015.
 */
public class SelfText extends TextView{

    public SelfText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.FaceFont);
        boolean allow = array.getBoolean( R.styleable.FaceFont_font_allow,false );
        int flag = array.getInt( R.styleable.FaceFont_font_type, -1 );

        Log.e( "***********", "allow: "+allow+", flag: "+flag );
    }
}
