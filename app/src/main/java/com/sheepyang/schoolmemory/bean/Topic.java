package com.sheepyang.schoolmemory.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 话题
 * Created by SheepYang on 2016/8/12.
 */
public class Topic extends BmobObject {
    private MyUser creator;// 创建者
    private TopicType type;// 类型
    private String title;// 标题
    private String content;// 内容
    private Integer postNum;// 帖子数量
    private List<String> imageList;// 图片列表
    private BmobRelation collectors;// 收藏者

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPostNum() {
        return postNum;
    }

    public void setPostNum(Integer postNum) {
        this.postNum = postNum;
    }

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

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public BmobRelation getCollectors() {
        return collectors;
    }

    public void setCollectors(BmobRelation collectors) {
        this.collectors = collectors;
    }
}