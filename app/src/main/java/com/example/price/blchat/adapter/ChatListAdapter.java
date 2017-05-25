package com.example.price.blchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.price.blchat.Constants;
import com.example.price.blchat.R;
import com.example.price.blchat.adapter.item.ChatItem;

import java.util.List;

/**
 * Created by price on 5/22/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private Context mContext;
    private List<ChatItem> mData;

    public ChatListAdapter(Context mContext, List<ChatItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        ChatViewHolder viewHolder;
        if(viewType == Constants.LEFT_CHAT){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_left_chat,null);
            viewHolder = new ChatViewHolder(view,viewType);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.item_right_chat,null);
            viewHolder = new ChatViewHolder(view,viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_content.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getFlag() == Constants.LEFT_CHAT){
            return Constants.LEFT_CHAT;
        }else{
            return Constants.RIGHT_CHAT;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_content;
        private TextView tv_name;

        public ChatViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == Constants.LEFT_CHAT){
                tv_content = (TextView) itemView.findViewById(R.id.left_content);
                tv_name = (TextView) itemView.findViewById(R.id.left_name);
            }else{
                tv_content = (TextView) itemView.findViewById(R.id.right_content);
                tv_name = (TextView) itemView.findViewById(R.id.right_name);
            }
        }
    }
}
