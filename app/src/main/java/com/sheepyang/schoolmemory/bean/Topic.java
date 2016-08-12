package com.sheepyang.schoolmemory.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 话题
 * Created by SheepYang on 2016/8/12.
 */
public class Topic extends BmobObject {
    private MyUser creator;// 创建者
    private TopicType type;// 类型
    private String title;// 标题
    private Integer commentNum;// 评论数量
    private List<BmobFile> imageList;// 图片列表
    private BmobRelation collectors;// 收藏者

    public MyUser getCreator() {
        return creator;
    }

    public void setCreator(MyUser creator) {
        this.creator = creator;
    }

    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public List<BmobFile> getImageList() {
        return imageList;
    }

    public void setImageList(List<BmobFile> imageList) {
        this.imageList = imageList;
    }

    public BmobRelation getCollectors() {
        return collectors;
    }

    public void setCollectors(BmobRelation collectors) {
        this.collectors = collectors;
    }
}