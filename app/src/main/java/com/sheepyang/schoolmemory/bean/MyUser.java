package com.sheepyang.schoolmemory.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyUser extends BmobUser {
    private String nick;
    private String avatar;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
