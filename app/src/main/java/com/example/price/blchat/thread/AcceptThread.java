package com.example.price.blchat.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.price.blchat.App;
import com.example.price.blchat.BluetoothChatService;
import com.example.price.blchat.Constants;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by price on 5/22/2017.
 */

public class AcceptThread extends Thread {
    private static final String TAG = "acceptThread";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // The local server socket
    private BluetoothServerSocket mmServerSocket;

    private BluetoothAdapter mAdapter;

    public AcceptThread(boolean secure) {
        BluetoothServerSocket tmp = null;
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        // Create a new listening server socket
        try {
            tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    "bluetoothChat", MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e(TAG, "Socket: " + "listen() failed", e);
        }
        mmServerSocket = tmp;
        App.setmState(Constants.STATE_LISTEN);
    }

    public void run() {
        setName("AcceptThread");

        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (App.getmState() != Constants.STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket: " + "accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (BluetoothChatService.class) {
                    switch (App.getmState()) {
                        case Constants.STATE_LISTEN:
                        case Constants.STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
//                            connected(socket, socket.getRemoteDevice());
                            break;
                        case Constants.STATE_NONE:
                        case Constants.STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }
    }
}
