package com.sheepyang.schoolmemory.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.bean.Topic;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.PLog;
import com.sheepyang.schoolmemory.util.RelativeDateFormatUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 话题Item
 * Created by SheepYang on 2016/8/4.
 */
public class TopicAdapter extends BaseAdapter {
    private Context mContext;
    private List<Topic> mTopicList;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_QUESTION = 1;
    private static final int TYPE_IMAGE1 = 2;
    private static final int TYPE_IMAGE2 = 3;
    private static final int TYPE_IMAGE3 = 4;

    public TopicAdapter(Context context, List<Topic> topicList) {
        mContext = context;
        mTopicList = topicList;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mTopicList.get(position).getType().ordinal();
        if (type == 1) {
            return TYPE_QUESTION;
        } else if (type == 2) {
            if (mTopicList.get(position).getImageList() != null && mTopicList.get(position).getImageList().size() > 0) {
                if (mTopicList.get(position).getImageList().size() == 1) {
                    return TYPE_IMAGE1;
                } else if (mTopicList.get(position).getImageList().size() == 2) {
                    return TYPE_IMAGE2;
                } else {
                    return TYPE_IMAGE3;
                }
            } else {
                return TYPE_IMAGE1;
            }
        } else {
            return TYPE_TEXT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getCount() {
        return mTopicList == null ? 0 : mTopicList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTopicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder vh = null;
        ViewHolder1 vh1 = null;
        ViewHolder2 vh2 = null;
        ViewHolder3 vh3 = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_TEXT:
                case TYPE_QUESTION:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_topic, null);
                    vh = new ViewHolder(convertView);
                    convertView.setTag(vh);
                    break;
                case TYPE_IMAGE1:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_topic_image1, null);
                    vh1 = new ViewHolder1(convertView);
                    convertView.setTag(vh1);
                    break;
                case TYPE_IMAGE2:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_topic_image2, null);
                    vh2 = new ViewHolder2(convertView);
                    convertView.setTag(vh2);
                    break;
                case TYPE_IMAGE3:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_topic_image3, null);
                    vh3 = new ViewHolder3(convertView);
                    convertView.setTag(vh3);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_TEXT:
                case TYPE_QUESTION:
                    vh = (ViewHolder) convertView.getTag();
                    break;
                case TYPE_IMAGE1:
                    vh1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_IMAGE2:
                    vh2 = (ViewHolder2) convertView.getTag();
                    break;
                case TYPE_IMAGE3:
                    vh3 = (ViewHolder3) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case TYPE_TEXT:
                vh.tvQuestion.setVisibility(View.GONE);
                setData(vh, position);
                break;
            case TYPE_QUESTION:
                vh.tvQuestion.setVisibility(View.VISIBLE);
                setData(vh, position);
                break;
            case TYPE_IMAGE1:
                setData(vh1, position);
                break;
            case TYPE_IMAGE2:
                setData(vh2, position);
                break;
            case TYPE_IMAGE3:
                setData(vh3, position);
                break;
            default:
                break;
        }
        return convertView;
    }

    private void setData(ViewHolder vh, int position) {
        Topic topic = mTopicList.get(position);
        String nick = topic.getCreator().getNick();
        if (nick != null && AppUtil.isMobileNO(nick)) {
            nick = nick.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        if (nick != null && !TextUtils.isEmpty(nick)) {
            vh.tvCreatorName.setText("题主:" + nick);
            vh.tvCreatorName.setVisibility(View.VISIBLE);
        } else {
            vh.tvCreatorName.setVisibility(View.GONE);
        }
        // 设置头像
        if (TextUtils.isEmpty(topic.getCreator().getAvatar())) {
            Glide.with(mContext.getApplicationContext())
                    .load(AppUtil.getRadomHeadView(position))
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(topic.getCreator().getAvatar())
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        }
        // 标题
        vh.tvTitle.setText(topic.getTitle());
        // 类型
        vh.tvType.setText(topic.getType().getName());
        // 话题评论数
        String postNum = "<html>共<font color=#6dcbed>" + topic.getPostNum() + "</font>条内容</html>";
        try {
            vh.tvCreateDate.setText("更新于" + RelativeDateFormatUtil.format(topic.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            vh.tvCreateDate.setText("日期格式异常");
            e.printStackTrace();
        }
        vh.tvPostNum.setText(Html.fromHtml(postNum));
        // 话题内容
        vh.tvContent.setText(topic.getContent());
    }

    private void setData(ViewHolder1 vh, int position) {
        Topic topic = mTopicList.get(position);
        String nick = topic.getCreator().getNick();
        if (nick != null && AppUtil.isMobileNO(nick)) {
            nick = nick.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        if (nick != null && !TextUtils.isEmpty(nick)) {
            vh.tvCreatorName.setText("题主:" + nick);
            vh.tvCreatorName.setVisibility(View.VISIBLE);
        } else {
            vh.tvCreatorName.setVisibility(View.GONE);
        }
        // 设置头像
        if (TextUtils.isEmpty(topic.getCreator().getAvatar())) {
            Glide.with(mContext.getApplicationContext())
                    .load(AppUtil.getRadomHeadView(position))
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(topic.getCreator().getAvatar())
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        }
        // 标题
        vh.tvTitle.setText(topic.getTitle());
        // 类型
        vh.tvType.setText(topic.getType().getName());
        // 话题评论数
        String postNum = "<html>共<font color=#6dcbed>" + topic.getPostNum() + "</font>条内容</html>";
        try {
            vh.tvCreateDate.setText("更新于" + RelativeDateFormatUtil.format(topic.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            vh.tvCreateDate.setText("日期格式异常");
            e.printStackTrace();
        }
        vh.tvPostNum.setText(Html.fromHtml(postNum));
        // 话题内容
        vh.tvContent.setText(topic.getContent());

        // 设置话题图片
        List<String> imageList = topic.getImageList();
        if (imageList != null) {
            if (imageList.size() == 1) {
                vh.ivImg11.setVisibility(View.VISIBLE);
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int windowWidth = wm.getDefaultDisplay().getWidth();
                int imageWidth = windowWidth - AppUtil.dip2px(mContext, 60);
                // 第一张图片
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) vh.ivImg11.getLayoutParams();
                lp1.width = imageWidth;
                lp1.height = imageWidth;
                vh.ivImg11.setLayoutParams(lp1);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(0))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg11);
            } else {
                vh.ivImg11.setVisibility(View.GONE);
            }
        } else {
            vh.ivImg11.setVisibility(View.GONE);
        }
    }

    private void setData(ViewHolder2 vh, int position) {
        Topic topic = mTopicList.get(position);
        String nick = topic.getCreator().getNick();
        if (nick != null && AppUtil.isMobileNO(nick)) {
            nick = nick.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        if (nick != null && !TextUtils.isEmpty(nick)) {
            vh.tvCreatorName.setText("题主:" + nick);
            vh.tvCreatorName.setVisibility(View.VISIBLE);
        } else {
            vh.tvCreatorName.setVisibility(View.GONE);
        }
        // 设置头像
        if (TextUtils.isEmpty(topic.getCreator().getAvatar())) {
            Glide.with(mContext.getApplicationContext())
                    .load(AppUtil.getRadomHeadView(position))
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(topic.getCreator().getAvatar())
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        }
        // 标题
        vh.tvTitle.setText(topic.getTitle());
        // 类型
        vh.tvType.setText(topic.getType().getName());
        // 话题评论数
        String postNum = "<html>共<font color=#6dcbed>" + topic.getPostNum() + "</font>条内容</html>";
        try {
            vh.tvCreateDate.setText("更新于" + RelativeDateFormatUtil.format(topic.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            vh.tvCreateDate.setText("日期格式异常");
            e.printStackTrace();
        }
        vh.tvPostNum.setText(Html.fromHtml(postNum));
        // 话题内容
        vh.tvContent.setText(topic.getContent());

        // 设置话题图片
        List<String> imageList = topic.getImageList();
        if (imageList != null) {
            if (imageList.size() == 2) {
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int windowWidth = wm.getDefaultDisplay().getWidth();
                int imageWidth = (windowWidth - AppUtil.dip2px(mContext, 65)) / 2;

                // 第一张图片
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) vh.ivImg21.getLayoutParams();
                lp1.width = imageWidth;
                lp1.height = imageWidth;
                vh.ivImg21.setLayoutParams(lp1);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(0))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg21);

                // 第二张图片
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) vh.ivImg22.getLayoutParams();
                lp2.width = imageWidth;
                lp2.height = imageWidth;
                vh.ivImg22.setLayoutParams(lp2);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(1))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg22);
            }
        }
    }

    private void setData(ViewHolder3 vh, int position) {
        Topic topic = mTopicList.get(position);
        String nick = topic.getCreator().getNick();
        if (nick != null && AppUtil.isMobileNO(nick)) {
            nick = nick.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        if (nick != null && !TextUtils.isEmpty(nick)) {
            vh.tvCreatorName.setText("题主:" + nick);
            vh.tvCreatorName.setVisibility(View.VISIBLE);
        } else {
            vh.tvCreatorName.setVisibility(View.GONE);
        }
        // 设置头像
        if (TextUtils.isEmpty(topic.getCreator().getAvatar())) {
            Glide.with(mContext.getApplicationContext())
                    .load(AppUtil.getRadomHeadView(position))
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(topic.getCreator().getAvatar())
                    .fitCenter()
                    .crossFade()
                    .into(vh.civAvatar);
        }
        // 标题
        vh.tvTitle.setText(topic.getTitle());
        // 类型
        vh.tvType.setText(topic.getType().getName());
        // 话题评论数
        String postNum = "<html>共<font color=#6dcbed>" + topic.getPostNum() + "</font>条内容</html>";
        try {
            vh.tvCreateDate.setText("更新于" + RelativeDateFormatUtil.format(topic.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            vh.tvCreateDate.setText("日期格式异常");
            e.printStackTrace();
        }
        vh.tvPostNum.setText(Html.fromHtml(postNum));
        // 话题内容
        vh.tvContent.setText(topic.getContent());

        // 设置话题图片
        List<String> imageList = topic.getImageList();
        if (imageList != null) {
            if (imageList.size() >= 3) {
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int windowWidth = wm.getDefaultDisplay().getWidth();
                int imageWidth = (windowWidth - AppUtil.dip2px(mContext, 70)) / 3;

                // 第一张图片
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) vh.ivImg31.getLayoutParams();
                lp1.width = imageWidth;
                lp1.height = imageWidth;
                vh.ivImg31.setLayoutParams(lp1);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(0))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg31);

                // 第二张图片
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) vh.ivImg32.getLayoutParams();
                lp2.width = imageWidth;
                lp2.height = imageWidth;
                vh.ivImg32.setLayoutParams(lp2);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(1))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg32);

                // 第三张图片
                LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) vh.ivImg33.getLayoutParams();
                lp3.width = imageWidth;
                lp3.height = imageWidth;
                vh.ivImg33.setLayoutParams(lp3);
                Glide.with(mContext.getApplicationContext())
                        .load(imageList.get(2))
                        .placeholder(R.drawable.image_progress_rorate)
                        .fitCenter()
                        .into(vh.ivImg33);
            }
        }
    }

    /**
     * 更新数据
     *
     * @param topicList
     */
    public void updataList(List<Topic> topicList) {
        mTopicList = topicList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tvCreateDate)
        TextView tvCreateDate;
        @BindView(R.id.tvQuestion)
        TextView tvQuestion;
        @BindView(R.id.tvCreatorName)
        TextView tvCreatorName;
        @BindView(R.id.civAvatar)
        CircleImageView civAvatar;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPostNum)
        TextView tvPostNum;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvContent)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder1 {
        @BindView(R.id.tvCreateDate)
        TextView tvCreateDate;
        @BindView(R.id.tvCreatorName)
        TextView tvCreatorName;
        @BindView(R.id.ivImg11)
        ImageView ivImg11;
        @BindView(R.id.civAvatar)
        CircleImageView civAvatar;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPostNum)
        TextView tvPostNum;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvContent)
        TextView tvContent;

        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 {
        @BindView(R.id.tvCreateDate)
        TextView tvCreateDate;
        @BindView(R.id.tvCreatorName)
        TextView tvCreatorName;
        @BindView(R.id.ivImg21)
        ImageView ivImg21;
        @BindView(R.id.ivImg22)
        ImageView ivImg22;
        @BindView(R.id.civAvatar)
        CircleImageView civAvatar;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPostNum)
        TextView tvPostNum;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvContent)
        TextView tvContent;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder3 {
        @BindView(R.id.tvCreateDate)
        TextView tvCreateDate;
        @BindView(R.id.tvCreatorName)
        TextView tvCreatorName;
        @BindView(R.id.ivImg31)
        ImageView ivImg31;
        @BindView(R.id.ivImg32)
        ImageView ivImg32;
        @BindView(R.id.ivImg33)
        ImageView ivImg33;
        @BindView(R.id.civAvatar)
        CircleImageView civAvatar;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPostNum)
        TextView tvPostNum;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvContent)
        TextView tvContent;

        ViewHolder3(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
