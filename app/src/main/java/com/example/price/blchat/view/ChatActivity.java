package com.example.price.blchat.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.price.blchat.App;
import com.example.price.blchat.Constants;
import com.example.price.blchat.R;
import com.example.price.blchat.adapter.ChatListAdapter;
import com.example.price.blchat.adapter.item.ChatItem;
import com.example.price.blchat.utils.BluetoothUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by price on 5/22/2017.
 */

public class ChatActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "chatActivity";
    private String name = "device";

    private RecyclerView rv_chat_list;
    private ImageButton iv_send;
    private EditText ed_content;

    private String content;

    private List<ChatItem> mData;
    private ChatListAdapter mAdapter;

    private BluetoothUtils mUtils;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MESSAGE_READ:
                    mData.add(new ChatItem(name,new String((byte[])msg.obj, 0, msg.arg1),Constants.LEFT_CHAT));
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        name = intent.getStringExtra("device_name");
        setTitle(name);
        mUtils = BluetoothUtils.getInstance(mHandler);
        initView();
    }

    private void initView() {
        rv_chat_list = (RecyclerView) findViewById(R.id.rv_chat_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_chat_list.setLayoutManager(manager);
        //设置增加或删除条目的动画
        rv_chat_list.setItemAnimator(new DefaultItemAnimator());
        initData();

        iv_send = (ImageButton) findViewById(R.id.ibtn_send);
        iv_send.setOnClickListener(this);

        ed_content = (EditText) findViewById(R.id.ed_content);
    }

    private void initData() {
        mData = new ArrayList<>();
        mAdapter = new ChatListAdapter(this,mData);
        rv_chat_list.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        Log.e(TAG,"onClick");
        content = ed_content.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this,"input could not be null",Toast.LENGTH_SHORT);
            return;
        }
        mData.add(new ChatItem("me",content, Constants.RIGHT_CHAT));
        mAdapter.notifyDataSetChanged();
//        sendMsg(content);
    }

    private void sendMsg(String content) {
        byte[] bytes = content.getBytes();
        mUtils.write(bytes);
    }
}
