package com.example.price.blchat.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.price.blchat.R;
import com.example.price.blchat.adapter.item.BlueToothDeviceItem;

import java.util.List;

/**
 * Created by price on 5/18/2017.
 */

public class BlueToothDevicesAdapter extends RecyclerView.Adapter {
    private static String TAG = "bldeviceAdapter";


    private Context mContext;
    private List<BluetoothDevice> mDevices;

    private BLDeviceOnClickListener listener;

    public BlueToothDevicesAdapter(Context mContext, List<BluetoothDevice> mDevices) {
        this.mContext = mContext;
        this.mDevices = mDevices;
    }

    public void setListener(BLDeviceOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public BLDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device,null);
        BLDeviceViewHolder viewHolder = new BLDeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        BLDeviceViewHolder viewHolder = (BLDeviceViewHolder) holder;
        Log.e(TAG,mDevices.get(position).getAddress());
        if(TextUtils.isEmpty(mDevices.get(position).getName())){
            viewHolder.tv_device_name.setText("Unknown Device");
        }else{
            viewHolder.tv_device_name.setText(mDevices.get(position).getName());
        }
        viewHolder.tv_device_mac.setText(mDevices.get(position).getAddress());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(holder.getLayoutPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    class BLDeviceViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_device_name;
        private TextView tv_device_mac;

        public BLDeviceViewHolder(View itemView) {
            super(itemView);
            tv_device_name = (TextView) itemView.findViewById(R.id.item_device_name);
            tv_device_mac = (TextView) itemView.findViewById(R.id.item_device_mac);
        }
    }

    public interface BLDeviceOnClickListener{
        void onItemClick(int position);
    }
}
