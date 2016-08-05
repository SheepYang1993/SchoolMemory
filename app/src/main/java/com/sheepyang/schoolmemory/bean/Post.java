package com.sheepyang.schoolmemory.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 帖子
 * Created by Administrator on 2016/8/4.
 */
public class Post extends BmobObject {
    private MyUser author;//作者
    private String contentImg;//帖子图片
    private String content;//帖子内容
    private List<MyUser> likesList;//点赞用户列表

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MyUser> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<MyUser> likesList) {
        this.likesList = likesList;
    }
}
