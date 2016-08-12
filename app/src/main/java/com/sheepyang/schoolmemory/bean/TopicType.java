package com.sheepyang.schoolmemory.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 话题类型
 * Created by SheepYang on 2016/8/12.
 */
public class TopicType extends BmobObject {
    private String name;// 类型名称
    private BmobFile image;//类型图片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
