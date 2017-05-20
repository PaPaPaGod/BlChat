package com.example.price.blchat.broadcastreceiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.price.blchat.adapter.item.BlueToothDeviceItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BlueToothReceiver extends BroadcastReceiver {
    private static String TAG = "bluetoothreceiver";


    private SearchBlueToothListener listener;

    private List<BlueToothDeviceItem> mDevicesList; //列表呈现所用的数据
    private List<BluetoothDevice> mDevices; //设备

    public BlueToothReceiver() {
        mDevices = new ArrayList<>();
        mDevicesList = new ArrayList<>();
    }

    public void setListener(SearchBlueToothListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(BluetoothDevice.ACTION_FOUND)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            mDevices.add(device);
//            BlueToothDeviceItem item = new BlueToothDeviceItem();
//            item.setDeviceName("No name");
//            if(!TextUtils.isEmpty(device.getName())){
//                item.setDeviceName(device.getName());
//            }
//            item.setDeviceAddress(device.getAddress());
//            mDevicesList.add(item);
//            if(!TextUtils.isEmpty(item.getDeviceName()))
//                Log.e(TAG,item.getDeviceName());
        }
        listener.getDevice(mDevices);
    }

//    private boolean isLock(BluetoothDevice device) {
//        boolean isLockName = (device.getName()).equals(lockName);
//        boolean isSingleDevice = mDevices.indexOf(device.getName()) == -1;
//        return isLockName && isSingleDevice;
//    }

    public interface SearchBlueToothListener{
        void getDevice(List<BluetoothDevice> mDiveces);
//        void getDevice(List<BluetoothDevice> mDiveces,List<BlueToothDeviceItem> mDevices);
    }
}
