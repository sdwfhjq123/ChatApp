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

import com.yinhao.chatapp.R;

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
    public String[] texts = {"java", "python", "C++", "Php", ".NET", "js", "Ruby", "Swift", "OC"};

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int mHeaderCount = 1;//头部item个数
    private int mFooterCount = 1;//底部item个数

    public HeaderAndFooterAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * @return 返回的内容长度
     */
    public int getContentItemCount() {
        return texts.length;
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
        Log.i(TAG, "getItemViewType: ");
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
        Log.i(TAG, "onCreateViewHolder: " + viewType);
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
            ((NormalViewHolder) holder).bind(texts[position - mHeaderCount]);
        }
        if (holder instanceof HeaderViewHolder) {

        }
        if (holder instanceof FooterViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return texts.length + mHeaderCount + mFooterCount;
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
            Toast.makeText(mContext, "群组", Toast.LENGTH_SHORT).show();
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

        private String text;

        public NormalViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_contact, parent, false));
            mContactItem = (LinearLayout) itemView;
            mHeadContactImageView = (ImageView) itemView.findViewById(R.id.contact_head_image);
            mContactNameTextView = (TextView) itemView.findViewById(R.id.contact_name_textview);
            itemView.setOnClickListener(this);
        }

        public void bind(String text) {
            this.text = text;
            mContactNameTextView.setText(text);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    }

}
