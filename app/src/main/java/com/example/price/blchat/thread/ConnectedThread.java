package com.example.price.blchat.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by price on 5/21/2017.
 */

public class ConnectedThread extends Thread {

    private static String TAG = "ConnectedThread";

    private BluetoothSocket mSocket;
    private ConnectedListener connectedListener;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectedThread(BluetoothSocket mSocket) {
        this.mSocket = mSocket;
        try {
            inputStream = mSocket.getInputStream();
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConnectedListener(ConnectedListener connectedListener) {
        this.connectedListener = connectedListener;
    }

    @Override
    public void run() {
        super.run();
        byte[] data = new byte[1024];
        int bytes;
        while(true){
            try {
                bytes = inputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ConnectedListener{
        void clear(BluetoothSocket mSocket);
    }

    public void cancel(BluetoothSocket mSocket){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "close() of connected " + " socket failed", e);
        }
    }
}
