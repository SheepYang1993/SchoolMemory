package com.sheepyang.schoolmemory.bean;

import com.sheepyang.schoolmemory.R;

/**
 * 话题类型
 * Created by SheepYang on 2016/8/12.
 */
public enum TopicType {
    TEXT("文字", R.drawable.fab_edit), QUESTION("问题", R.drawable.fab_question), IMAGE("图片", R.drawable.fab_img);

    private int resId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    private TopicType(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }
}
