package com.sheepyang.schoolmemory.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 帖子
 * Created by SheepYang on 2016/8/4.
 */
public class Post extends BmobObject {
    private MyUser author;// 作者
    private Topic topic;// 所属话题
    private String contentImg;// 帖子图片
    private String content;// 帖子内容
    private Integer commentNum;// 评论数量
    private BmobRelation likes;// 点赞用户
    private BmobRelation agrees;// 支持者
    private BmobRelation disagree;// 反对者

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public BmobRelation getAgrees() {
        return agrees;
    }

    public void setAgrees(BmobRelation agrees) {
        this.agrees = agrees;
    }

    public BmobRelation getDisagree() {
        return disagree;
    }

    public void setDisagree(BmobRelation disagree) {
        this.disagree = disagree;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

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

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
