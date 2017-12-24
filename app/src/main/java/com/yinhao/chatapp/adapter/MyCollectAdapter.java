package com.yinhao.chatapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.GroupVO.GroupData;
import com.yinhao.chatapp.model.Collect;
import com.yinhao.chatapp.utils.ConstantValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hp on 2017/12/19.
 */

public class MyCollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MyCollectAdapter";
    private static final int TYPE_TEXT_CONTENT = 0;
    private static final int TYPE_IMAGE_CONTENT = 1;

    //模拟数据
    private List<Collect> mList = new ArrayList<>();

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private OnLongItemClickListener mOnLongItemClickListener;

    public interface OnLongItemClickListener {
        void onLongClickDelete(View v, int position);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    public MyCollectAdapter(Context context, List<Collect> groupData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mList = groupData;
    }

    public void setData(List<Collect> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mList.get(position).getType();
        //如果type为0，返回文本类型,1为图片类型
        if (type == 0) {
            return TYPE_TEXT_CONTENT;
        } else if (type == 1) {
            return TYPE_IMAGE_CONTENT;
        }
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT_CONTENT) {
            return new TextViewHolder(mLayoutInflater, parent);
        } else if (viewType == TYPE_IMAGE_CONTENT) {
            return new ImageViewHolder(mLayoutInflater, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).bind(mList.get(position).getUrl(), mList.get(position).getCreateTime());
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(mList.get(position).getUrl(), mList.get(position).getCreateTime());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class TextViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mContentTextView;
        private TextView mCreateTime;

        public TextViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_collect_text, parent, false));
            mContentTextView = (TextView) itemView.findViewById(R.id.content_text_view);
            mCreateTime = (TextView) itemView.findViewById(R.id.date_text_view);

            itemView.setOnLongClickListener(this);
        }

        public void bind(String content, Date date) {
            mContentTextView.setText(content);
            mCreateTime.setText(date2String(date));
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongItemClickListener.onLongClickDelete(v, getAdapterPosition());
            return true;
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ImageView mContentImageView;
        private TextView mCreateTime;

        public ImageViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_collect_pic, parent, false));
            mContentImageView = (ImageView) itemView.findViewById(R.id.content_image_view);
            mCreateTime = (TextView) itemView.findViewById(R.id.date_text_view);
            itemView.setOnLongClickListener(this);
        }

        public void bind(String picContent, Date date) {
            Glide.with(mContext).load(Uri.parse(picContent)).into(mContentImageView);
            mCreateTime.setText(date2String(date));
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongItemClickListener.onLongClickDelete(v, getAdapterPosition());
            return true;
        }

    }

    public String date2String(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
