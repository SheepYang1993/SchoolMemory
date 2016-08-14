package com.sheepyang.schoolmemory.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 评论
 * Created by SheepYang on 2016/8/12.
 */
public class Comment extends BmobObject {
    private MyUser author;// 作者
    private Post post;// 所属帖子
    private String content;// 评论内容

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
