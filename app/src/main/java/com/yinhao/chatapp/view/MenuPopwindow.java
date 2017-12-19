package com.yinhao.chatapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.PaintDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yinhao.chatapp.R;

import java.util.List;

/**
 * Created by hp on 2017/12/19.
 */

public class MenuPopwindow extends PopupWindow {
    private View mContentView;
    private ListView mListView;

    public MenuPopwindow(Activity context, List<MenuPopwindowBean> list) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContentView = inflater.inflate(R.layout.pop_menu, null);
        mListView = (ListView) mContentView.findViewById(R.id.lv_toptitle_menu);

        Point outSize = new Point();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getSize(outSize);
        int screenWidth = outSize.x;//得到屏幕的宽度
        int screenHeight = outSize.y;//得到屏幕的高度

        //设置SelectedPicPopupWindow的View
        setContentView(mContentView);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(screenWidth / 3 - 30);
        //设置窗体可点击
        setFocusable(true);
        setOutsideTouchable(true);

        //刷新状态
        update();

        //要给popupWindow设置一个一个BackgroundDrawable，
        //如果你已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
        setBackgroundDrawable(new PaintDrawable());

        setAnimationStyle(android.support.v7.appcompat.R.style.Animation_AppCompat_Dialog);
    }

    /**
     * 封装点击单个条目
     *
     * @param onItemClickListener
     */
    public void setOnItemClick(AdapterView.OnItemClickListener onItemClickListener) {
        mListView.setOnItemClickListener(onItemClickListener);
    }

    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            showAsDropDown(parent);
        } else {
            dismiss();
        }
    }

    //适配器
    class PopAdapter extends BaseAdapter {

        private List<MenuPopwindowBean> mList;
        private LayoutInflater mInflater;

        public PopAdapter(Context context, List<MenuPopwindowBean> list) {
            mInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.pop_menu, null);
                holder = new Holder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_menu_item);
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_menu_item);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.mImageView.setImageResource(mList.get(position).getIcon());
            holder.mTextView.setText(mList.get(position).getText());
            return convertView;
        }

        class Holder {
            ImageView mImageView;
            TextView mTextView;
        }
    }

    public static class MenuPopwindowBean {

        public MenuPopwindowBean(int icon, String text) {
            Icon = icon;
            this.text = text;
        }

        private int Icon;
        private String text;

        public int getIcon() {
            return Icon;
        }

        public void setIcon(int icon) {
            Icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
