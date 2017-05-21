package com.example.price.blchat.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.price.blchat.utils.BluetoothUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by price on 5/21/2017.
 */

public class ConnectThread extends Thread {

    private BluetoothSocket mSocket;

    private static String TAG = "createConnectThread";

    private BluetoothAdapter mAdapter;

    private ConnectedListener connectedListener;

    public void setConnectedListener(ConnectedListener connectedListener) {
        this.connectedListener = connectedListener;
    }

    public ConnectThread(BluetoothDevice device) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            mSocket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID.randomUUID().toString()));
        } catch (IOException e) {
            Log.e(TAG,"create mSocket failed..."+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        mAdapter.cancelDiscovery();

        try {
            mSocket.connect();
        } catch (IOException e) {
            try {
                mSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(TAG,"unable to close while mSocket is connecting.. "+e.getMessage());
            }
            e.printStackTrace();
            Log.e(TAG,"unable to connect while mSocket is connecting.. "+e.getMessage());
            return;
        }

        // Reset the ConnectThread because we're done
        connectedListener.clear();

        // Start the connected thread
        connectedListener.connected(mSocket);
    }

    public void cancel(){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "close() of connect " + " socket failed", e);
        }
    }

    public interface ConnectedListener{
        void connected(BluetoothSocket mmSocket);
        void clear();
    }
}
