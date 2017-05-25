package com.example.price.blchat.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.price.blchat.BluetoothChatService;
import com.example.price.blchat.R;
import com.example.price.blchat.adapter.BlueToothDevicesAdapter;
import com.example.price.blchat.broadcastreceiver.BlueToothReceiver;
import com.example.price.blchat.utils.BluetoothUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BlueToothReceiver.SearchBlueToothListener, BlueToothDevicesAdapter.BLDeviceOnClickListener {

    private RecyclerView rv_device;
    private Button btn_search;

    private BluetoothAdapter bluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothChatService mChatService = null;

    private static final String TAG = "BluetoothChatFragment";

    private boolean BTIsUseful = false;

    private BlueToothReceiver receiver;


    private BlueToothDevicesAdapter mAdapter;
//    private List<BlueToothDeviceItem> mDevicesItem;
    private List<BluetoothDevice> mDevices;

    private BluetoothUtils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUtils = BluetoothUtils.getInstance();
        initView();
        checkBlueToothAdapter();
    }

    private void checkBlueToothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            AlertDialog dialog = makeDialogAndClose(getResources().getString(R.string.BT_IS_NOT_AVAILABLE));
            dialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }else if(mUtils == null){
//            setupChat();
            mUtils = BluetoothUtils.getInstance();
        }
    }

    private void setupChat() {

    }

    private void initView() {
        rv_device = (RecyclerView) findViewById(R.id.device_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_device.setLayoutManager(manager);
        //设置增加或删除条目的动画
        rv_device.setItemAnimator(new DefaultItemAnimator());

        initData();

        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
    }

    private void initData() {
//        mDevicesItem = new ArrayList<>();
        mDevices = new ArrayList<>();
//        BlueToothDeviceItem item = new BlueToothDeviceItem();
//        item.setDeviceName("hehe");
//        item.setDeviceAddress("123456789");
//        mDevicesItem.add(item);
        mAdapter = new BlueToothDevicesAdapter(this,mDevices);
        mAdapter.setListener(this);
        rv_device.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search:
                boolean isDiscovery = bluetoothAdapter.startDiscovery();
                if(isDiscovery){
                    Log.e(TAG,"dicoverying...");
                    IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    receiver = new BlueToothReceiver();
                    receiver.setListener(this);
                    registerReceiver(receiver,intentFilter);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    BTIsUseful = true;
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.e(TAG, "BT not enabled");
                    AlertDialog dialog = makeDialogAndClose(getResources().getString(R.string.bt_not_enabled_leaving));
                    dialog.show();
                }
                break;
        }
    }

    private AlertDialog makeDialogAndClose(String msg){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                    }
                })
                .create();
        return dialog;
    }

    @Override
    public void getDevice(List<BluetoothDevice> mDiveces) {
//        this.mDevicesItem.addAll(mDevicesItem);
        this.mDevices.addAll(mDiveces);
        rv_device.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        // Get the device MAC address
        String address = mDevices.get(position).getAddress();
        // Get the BluetoothDevice object
        BluetoothDevice device = mDevices.get(position);
        mUtils.connect(device);
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("device_name",device.getName());
        startActivity(intent);
    }

    //set device visible
    private void setBlVisible(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }
}
