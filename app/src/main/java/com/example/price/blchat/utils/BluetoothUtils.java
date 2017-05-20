package com.example.price.blchat.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;


import java.io.IOException;
import java.util.UUID;

/**
 * Created by price on 5/20/2017.
 */

public class BluetoothUtils {


    private BluetoothAdapter mAdapter;

    private Thread connectThread;
    private Thread connectedThread;
    private Thread acceptThread;

    private ConnectThread connectRunable;

    public BluetoothUtils() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    class ConnectThread implements Runnable{
        private BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice mmDevice) {
            this.mmDevice = mmDevice;
            try {
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID.randomUUID().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            mAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return;
            }
            synchronized (BluetoothUtils.class){
                connectThread = null;
            }
            connected(mmSocket);
        }

        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void connected(BluetoothSocket mmSocket) {
        if(connectThread!=null){
            connectThread.;
        }
    }

    class ConnectedThread implements Runnable{

        @Override
        public void run() {

        }
    }
}
