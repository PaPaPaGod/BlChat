package com.example.price.blchat.thread;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.price.blchat.App;
import com.example.price.blchat.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by price on 5/21/2017.
 */

public class ConnectedThread extends Thread {

    private static String TAG = "ConnectedThread";

    private BluetoothSocket mSocket;

    public void setListener(UpdateChatMsgListener listener) {
        this.listener = listener;
    }

    private UpdateChatMsgListener listener;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Handler mHandler;

    public void connected(final UpdateChatMsgListener listener){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
//                byte[] data = new byte[1024];
//                int bytes;
                while(App.getmState() == Constants.STATE_CONNECTED){
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                inputStream));
                        String line = null;
                        StringBuffer result = new StringBuffer();
                        while((line = bufferedReader.readLine())!=null){
                            result.append(line);
                        }
                        listener.update(line);
//                        mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, data)
//                                .sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
    }

    public ConnectedThread(BluetoothSocket mSocket,Handler mHandler) {
        this.mSocket = mSocket;
        this.mHandler = mHandler;
        try {
            inputStream = mSocket.getInputStream();
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        byte[] data = new byte[1024];
        int bytes;
        while(App.getmState() == Constants.STATE_CONNECTED){
            try {
                bytes = inputStream.read();
                mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, data)
                        .sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "close() of connected " + " socket failed", e);
        }
    }

    public void write(byte[] out){
        try {
            outputStream.write(out);
            //notice to update the ui

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface UpdateChatMsgListener{
        void update(String msg);
    }
}
