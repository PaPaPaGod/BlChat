package com.example.price.blchat.adapter.item;

/**
 * Created by price on 5/22/2017.
 */

public class ChatItem {
    public ChatItem(String name, String content, int flag) {
        this.name = name;
        this.content = content;
        this.flag = flag;
    }

    private String name;
    private String content;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
