package com.sheepyang.schoolmemory.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.MyToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 帖子Item
 * Created by SheepYang on 2016/8/4.
 */
public class TestPostAdapter extends BaseAdapter {
    @BindView(R.id.ivThumb)
    ImageView ivThumb;
    private Context mContext;
    private List<Post> mPostList;
    private int mSize = 5;

    public TestPostAdapter(Context context, List<Post> postList) {
        mContext = context;
        mPostList = postList;
    }

    @Override
    public int getCount() {
        return mPostList == null ? 0 : mPostList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPostList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = mPostList.get(position);
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_post, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvName.setText(post.getAuthor().getNick());
        if (TextUtils.isEmpty(post.getAuthor().getAvatar())) {
            Glide.with(mContext.getApplicationContext())
                    .load(AppUtil.getRadomHeadView(position))
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(post.getAuthor().getAvatar())
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        }
        Glide.with(mContext.getApplicationContext())
                .load(post.getContentImg())
                .fitCenter()
                .crossFade()
                .into(vh.ivContentImg);
        vh.tvContent.setText(post.getContent());
//        List<MyUser> likesList = post.getLikesList();
        String likes = "";
//        if (likesList != null && likesList.size() > 0) {
//            for (int i = 0; i < likesList.size(); i++) {
//                if (!TextUtils.isEmpty(likes)) {
//                    likes += "、";
//                }
//                MyUser user = likesList.get(i);
//                likes += user.getNick();
//            }
//        }
        if (TextUtils.isEmpty(likes)) {
            vh.tvThumbName.setVisibility(View.INVISIBLE);
            vh.tvThumbNameLast.setVisibility(View.INVISIBLE);
        } else {
            vh.tvThumbName.setText(likes);
        }
        vh.tvPageSize.setText("第" + (position / mSize + 1) + "页 " + "第" + (position % mSize + 1) + "条");
        return convertView;
    }

    public void setPageSize(int size) {
        if (size > 0) {
            mSize = size;
        }
    }

    /**
     * 更新数据
     *
     * @param postList
     */
    public void upDataList(List<Post> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }

    @OnClick({R.id.ivThumb, R.id.llUser})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivThumb:
                MyToast.showMessage(mContext, "点赞");
                break;
            case R.id.llUser:
                MyToast.showMessage(mContext, "跳转至用户个人信息界面");
                break;
            default:
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.civAvatar)
        CircleImageView civAvatar;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.llUser)
        LinearLayout llUser;
        @BindView(R.id.ivContentImg)
        ImageView ivContentImg;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.ivThumb)
        ImageView ivThumb;
        @BindView(R.id.tvThumbName)
        TextView tvThumbName;
        @BindView(R.id.tvThumbNameLast)
        TextView tvThumbNameLast;
        @BindView(R.id.tvPageSize)
        TextView tvPageSize;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
