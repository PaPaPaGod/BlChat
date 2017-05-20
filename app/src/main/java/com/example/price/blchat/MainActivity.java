package com.example.price.blchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.price.blchat.adapter.BlueToothDevicesAdapter;
import com.example.price.blchat.adapter.item.BlueToothDeviceItem;
import com.example.price.blchat.broadcastreceiver.BlueToothReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        }else if(mChatService == null){
            setupChat();
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

//                    ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progressDialog.setMessage(getResources().getString(R.string.search));
//                    progressDialog.setCancelable(true);
//                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialogInterface) {
//
//                        }
//                    });
//                    progressDialog.show();
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
    public void onClick(int position) {
        Toast.makeText(this,mDevices.get(position).getAddress(),Toast.LENGTH_SHORT).show();
    }

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
