package com.example.price.blchat;

import android.app.Application;
import android.content.Context;

import com.example.price.blchat.utils.BluetoothUtils;

/**
 * Created by price on 5/22/2017.
 */

public class App extends Application {
    private Context mContext;

    public static int getmState() {
        return mState;
    }

    public static void setmState(int mState) {
        App.mState = mState;
    }

    private static int mState = Constants.STATE_NONE;

    public Context getmContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
