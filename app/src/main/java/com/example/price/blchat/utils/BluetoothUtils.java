package com.example.price.blchat.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;


import com.example.price.blchat.thread.AcceptThread;
import com.example.price.blchat.thread.ConnectThread;
import com.example.price.blchat.thread.ConnectedThread;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by price on 5/20/2017.
 */

public class BluetoothUtils {

    private static BluetoothUtils mUtils = new BluetoothUtils();

    public static BluetoothUtils getInstance() {
        if(mUtils == null){
            mUtils = new BluetoothUtils();
        }
        return mUtils;
    }

    private BluetoothAdapter mAdapter;

    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private AcceptThread acceptThread;

    public BluetoothUtils() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public void connect(BluetoothDevice mmDevice){
        if(connectThread!=null){
            connectThread.cancel();
            connectThread = null;
        }
        connectThread = new ConnectThread(mmDevice);
        connectThread.setConnectedListener(new ConnectThread.ConnectedListener() {
            @Override
            public void connected(BluetoothSocket mmSocket) {
                connected(mmSocket);
            }

            @Override
            public void clear() {
                synchronized (BluetoothUtils.class){
                    connectThread = null;
                }
            }
        });
        connectThread.start();
    }

    public void accept(){

    }

}
