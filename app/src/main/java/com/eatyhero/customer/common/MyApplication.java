package com.eatyhero.customer.common;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.eatyhero.customer.R;
import com.teliver.sdk.core.TLog;
import com.teliver.sdk.core.Teliver;

/**
 * Created by user on 11/2/2015.
 */
public class MyApplication extends Application{

    public MyApplication()
    {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Teliver.init(this,getString(R.string.TeliverId));
        TLog.setVisible(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
