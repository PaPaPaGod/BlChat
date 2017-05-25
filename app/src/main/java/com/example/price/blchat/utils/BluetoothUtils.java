package com.example.price.blchat.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;


import com.example.price.blchat.App;
import com.example.price.blchat.Constants;
import com.example.price.blchat.thread.AcceptThread;
import com.example.price.blchat.thread.ConnectThread;
import com.example.price.blchat.thread.ConnectedThread;

import java.io.IOException;
import java.util.UUID;

import static com.example.price.blchat.Constants.STATE_CONNECTED;

/**
 * Created by price on 5/20/2017.
 */

public class BluetoothUtils implements ConnectedThread.UpdateChatMsgListener {

    private static volatile BluetoothUtils mUtils = null;

    public static BluetoothUtils getInstance(Handler mHandler) {
        if(mUtils == null){
            synchronized (BluetoothUtils.class){
                if(mUtils == null){
                    mUtils = new BluetoothUtils(mHandler);
                }
            }
        }
        return mUtils;
    }

    private Handler mHandler;

    private BluetoothAdapter mAdapter;

    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private AcceptThread acceptThread;


    public BluetoothUtils(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = handler;
    }


    public void connect(BluetoothDevice mmDevice){
        if(connectThread!=null){
            connectThread.cancel();
            connectThread = null;
        }
        if(connectedThread!=null){
            connectedThread.cancel();
            connectedThread = null;
        }
        connectThread = new ConnectThread(mmDevice);
        connectThread.setConnectedListener(new ConnectThread.ConnectedListener() {
            @Override
            public void connected(BluetoothSocket mmSocket) {
                BluetoothUtils.this.connected(mmSocket);
                App.setmState(Constants.STATE_CONNECTED);
            }

            @Override
            public void clear() {
                synchronized (BluetoothUtils.class){
                    connectThread = null;
                }
            }
        });
        connectThread.start();
        App.setmState(Constants.STATE_CONNECTING);
    }

    public void accept(){

    }

    public void connected(BluetoothSocket mSocket){
        if(connectThread!=null){
            connectThread.cancel();
            connectThread = null;
        }
        if(connectedThread!=null){
            connectedThread.cancel();
            connectedThread = null;
        }
        connectedThread = new ConnectedThread(mSocket,mHandler);
        connectedThread.setListener(this);
        connectedThread.start();
    }

    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (App.getmState() != STATE_CONNECTED) {
                return;
            }
            r = connectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    @Override
    public void update(String msg) {

    }
}
