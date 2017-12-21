package com.yinhao.chatapp.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.FriendVO;
import com.yinhao.chatapp.VO.FriendVO.FriendData;
import com.yinhao.chatapp.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/12/19.
 */

public class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HeaderAndFooterAdapter";

    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private static final int ITEM_TYPE_NORMAL = 2;

    //模拟数据
    private List<FriendData> mList = new ArrayList<>();

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int mHeaderCount = 1;//头部item个数
    private int mFooterCount = 1;//底部item个数

    private OnItemClickListener mOnItemClickListener;

    public HeaderAndFooterAdapter(Context context, List<FriendData> friendData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = friendData;
    }

    public void setData(List<FriendData> list) {
        mList = list;
    }

    public interface OnItemClickListener {
        void onStartConversationPrivateChat(View v, int position);

        void onStartGroupActivity(View v);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * @return 返回的内容长度
     */
    public int getContentItemCount() {
        return mList.size();
    }

    /**
     * 判断当前item是否是HeadView
     *
     * @param position
     * @return
     */
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    /**
     * 判断当前item是否是HeadView
     *
     * @param position
     * @return
     */
    public boolean isFooterView(int position) {
        return mFooterCount != 0 && position >= (mHeaderCount + getContentItemCount());
    }

    //判断当前itme类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部view
            return ITEM_TYPE_HEADER;
        } else if (mFooterCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater, parent);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater, parent);
        } else if (viewType == ITEM_TYPE_NORMAL) {
            return new NormalViewHolder(mLayoutInflater, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).bind(mList.get(position - mHeaderCount).getNikeName(),
                    mList.get(position - mHeaderCount).getPortraitUri());
        }
        if (holder instanceof HeaderViewHolder) {

        }
        if (holder instanceof FooterViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + mHeaderCount + mFooterCount;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mGroupItem;

        public HeaderViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_head_item, parent, false));
            mGroupItem = (LinearLayout) itemView.findViewById(R.id.group_item_linearlayout);
            mGroupItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mContext, "群组", Toast.LENGTH_SHORT).show();
            mOnItemClickListener.onStartGroupActivity(v);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_foot_item, parent, false));

        }


    }

    private class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mContactNameTextView;
        private ImageView mHeadContactImageView;
        private LinearLayout mContactItem;


        public NormalViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_contact, parent, false));
            mContactItem = (LinearLayout) itemView;
            mHeadContactImageView = (ImageView) itemView.findViewById(R.id.contact_head_image);
            mContactNameTextView = (TextView) itemView.findViewById(R.id.contact_name_textview);
            itemView.setOnClickListener(this);
        }

        public void bind(String nikeName, String portraitUri) {
            mContactNameTextView.setText(nikeName);
            Glide.with(mContext).load(ConstantValue.URL + portraitUri).into(mHeadContactImageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition() - 1;
            //Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            mOnItemClickListener.onStartConversationPrivateChat(v, position);
        }
    }

}
