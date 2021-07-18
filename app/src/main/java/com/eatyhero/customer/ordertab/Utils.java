package com.eatyhero.customer.ordertab;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.eatyhero.customer.R;

public class Utils {
    public void init(TextView txtView, Context context, AttributeSet attrs) {
        try {
            Typeface typeface = getCustomFont(context, attrs);
            if (typeface != null)
                txtView.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Typeface getCustomFont(Context context, AttributeSet attrs) {
        Typeface typeface = null;
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.CustomWidget);
            for (int i = 0, count = typedArray.getIndexCount(); i < count; i++) {
                int attribute = typedArray.getIndex(i);
                if (attribute == R.styleable.CustomWidget_font_name) {
                    typeface = Typeface.createFromAsset(context.getResources()
                            .getAssets(), typedArray.getString(attribute));
                }
            }
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeface;
    }


    public static boolean checkPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            4);
                else {
                    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                }
            } else return true;
        } else return true;
        return false;
    }

}
