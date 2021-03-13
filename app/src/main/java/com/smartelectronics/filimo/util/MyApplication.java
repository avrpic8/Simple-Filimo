package com.smartelectronics.filimo.util;

import android.app.Application;

import com.parse.Parse;
import com.smartelectronics.filimo.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/samim_fd.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        mInstance = this;
    }

    public static synchronized MyApplication getInstance(){

        return mInstance;
    }

    //public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        //ConnectivityReceiver.connectivityReceiverListener = listener;
    //}
}
