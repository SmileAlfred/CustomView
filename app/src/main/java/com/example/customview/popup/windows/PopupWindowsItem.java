package com.example.customview.popup.windows;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;
import com.example.customview.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description: 自定义控件 下拉框
 * mPopupWindow.setContentView(listView);
 * mPopupWindow.showAsDropDown(mEditTextPopup);    //还可以在 mEditTextPopup 之后设置参数 离左右的距离，均设置为 0
 * mPopupWindow.dismiss();
 * ？？？ .getView() 中 convertView = View.inflate(PopupWindowsItem.this, R.activity_main.activity_popup_drop_item, null);
 * ____________________convertView = LayoutInflater.from(parent.getContext()).inflate(R.activity_main.activity_popup_drop_item, parent, false);
 * ____________________inflate方法的主要作用就是将xml转换成一个View对象:
 * ____________________convertView = LayoutInflater.from(parent.getContext()).inflate(R.activity_main.activity_popup_drop_item, parent, false);两种均可；
 * ____________________convertView = LayoutInflater.from(parent.getContext()).inflate(R.activity_main.activity_popup_drop_item, parent, false);View.inflate 是静态方法，简单功能少；
 * ____________________LayoutInflater.from(parent.getContext()).inflate 抽象方法；功能强大，推荐使用；具体使用视情况稳定
 * @date :2020/02/29 15:22
 */
public class PopupWindowsItem extends AppCompatActivity {

    private ImageView mDownArrowImageView;
    private EditText mEditTextPopup;

    /**
     * PopupWindow 是悬浮在 页面之上，记录已经使用过的 view
     */
    private PopupWindow mPopupWindow;
    private ListView listView;
    private List<String> msgs = new ArrayList<>();
    private TextView mMsgsTextView;
    private ImageView mDeleteImageView;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_windows_item);

        mEditTextPopup = findViewById(R.id.edit_text_popup_item);
        mDownArrowImageView = findViewById(R.id.down_arrow_image_view_popup);
        TextView titlePoput = findViewById(R.id.title_text_view);

        mMsgsTextView = findViewById(R.id.msg_text_view_pop);
        mDeleteImageView = findViewById(R.id.delete_image_view_pop);
        titlePoput.setText("自定义 下拉框");

        mDownArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(PopupWindowsItem.this);
                    mPopupWindow.setWidth(mEditTextPopup.getWidth());
                    mPopupWindow.setHeight(DensityUtil.dip2px(PopupWindowsItem.this, 200));       //代码中设置尺寸单位是像素；针对不同分辨率手机，效果差别大

                    mPopupWindow.setContentView(listView);
                    mPopupWindow.setFocusable(true);    // 设置焦点，不然点击无用
                }
                mPopupWindow.showAsDropDown(mEditTextPopup);    //还可以在 mEditTextPopup 之后设置参数 离左右的距离，均设置为 0
            }
        });

        /**
         * listView 准备数据
         */
        initMsgs();
        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.list_view_background);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        /**
         * 设置 ListView 的点击事件
         * 1. 得到数据；
         * 2. 设置到输入框
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = msgs.get(position);
                mEditTextPopup.setText(msg);
                mEditTextPopup.setSelection(msg.length());    //设置完内容后，将光标移动到最后
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
            }
        });
    }

    /**
     * 设置 适配器
     */
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                //??? 这行代码出错。这和书上写的不一致。不懂 → inflate方法的主要作用就是将xml转换成一个View对象
//                convertView = View.inflate(PopupWindowsItem.this, R.activity_main.activity_popup_drop_item, null);
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_popup_drop_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.msgsText = convertView.findViewById(R.id.msg_text_view_pop);        //这里已经出现过 BUG 要从参数的 view 去findViewById；千万不能直接find
                viewHolder.deleteImage = convertView.findViewById(R.id.delete_image_view_pop);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            /**
             * 根据位置得到数据
             */
            final String msg = msgs.get(position);
            viewHolder.msgsText.setText(msg);

            /**
             *  设置删除
             *  1. msgs 集合移除数据； msgs.remove(msg)；|| msgs.remove(position)；
             *  2. 刷新 ui ; 即刷新适配器 .notifyDataSetChanged()
             */
            viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgs.remove(msg);
                    myAdapter.notifyDataSetChanged();   // 刷新后执行的 .getCount() → .getView()
                }
            });
            return convertView;
        }
    }

    /**
     * viewHolder 缓存控件等
     */
    static class ViewHolder {
        TextView msgsText;
        ImageView deleteImage;
    }

    /**
     * 初始化 下拉框消息数据
     */
    private void initMsgs() {
        long num = 19126209;
        for (int count = 0; count < 28; count++) {
            msgs.add("学号： " + num);
            num += 1;
        }
    }
}