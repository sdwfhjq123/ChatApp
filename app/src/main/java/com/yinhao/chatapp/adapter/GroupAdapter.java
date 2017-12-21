package com.yinhao.chatapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.FriendVO.FriendData;
import com.yinhao.chatapp.VO.GroupVO;
import com.yinhao.chatapp.VO.GroupVO.GroupData;
import com.yinhao.chatapp.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/12/19.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GroupAdapter";

    //模拟数据
    private List<GroupData> mList = new ArrayList<>();

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public GroupAdapter(Context context, List<GroupData> groupData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = groupData;
    }

    public void setData(List<GroupData> list) {
        mList = list;
    }

    public interface OnItemClickListener {
        void onStartConversationGroupChat(View v, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalViewHolder) holder).bind(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
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

        public void bind(String nikeName) {
            mContactNameTextView.setText(nikeName);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnItemClickListener.onStartConversationGroupChat(v, position);
        }
    }

}
