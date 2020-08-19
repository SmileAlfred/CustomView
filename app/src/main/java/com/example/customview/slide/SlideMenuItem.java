package com.example.customview.slide;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.quick.index.Person4QuickIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description: 自定义 侧滑菜单
 * × × × × × × × 注意 .getView() 方法中，一定要用 参数的 view.findViewById();    不可以 直接 findViewById()
 * ？？？如何实现 置顶未读和删除 的点击事件呢？
 * @date :2020/03/04 17:35
 */
public class SlideMenuItem extends AppCompatActivity {

    private ListView mListView;

    private List<Person4QuickIndex> mList;

    private MyAdapter adapter;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu_item);
        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("侧滑效果");
        mListView = findViewById(R.id.list_view_slide_menu);
        /**
         * 设置适配器
         * 准备数据
         */
        mList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mList.add(new Person4QuickIndex("张晓飞"));
            mList.add(new Person4QuickIndex("杨光福"));
            mList.add(new Person4QuickIndex("胡继群"));
            mList.add(new Person4QuickIndex("刘畅"));
            mList.add(new Person4QuickIndex("钟泽兴"));
            mList.add(new Person4QuickIndex("尹革新"));
            mList.add(new Person4QuickIndex("安传鑫"));
            mList.add(new Person4QuickIndex("张骞壬"));
            mList.add(new Person4QuickIndex("温松"));
            mList.add(new Person4QuickIndex("李凤秋"));
            mList.add(new Person4QuickIndex("刘甫"));
            mList.add(new Person4QuickIndex("娄全超"));
            mList.add(new Person4QuickIndex("张猛"));
            mList.add(new Person4QuickIndex("王英杰"));
            mList.add(new Person4QuickIndex("李振南"));
            mList.add(new Person4QuickIndex("孙仁政"));
            mList.add(new Person4QuickIndex("唐春雷"));
            mList.add(new Person4QuickIndex("牛鹏伟"));
            mList.add(new Person4QuickIndex("姜宇航"));
            mList.add(new Person4QuickIndex("刘挺"));
            mList.add(new Person4QuickIndex("张洪瑞"));
            mList.add(new Person4QuickIndex("张建忠"));
            mList.add(new Person4QuickIndex("侯亚帅"));
            mList.add(new Person4QuickIndex("刘帅"));
            mList.add(new Person4QuickIndex("乔竞飞"));
            mList.add(new Person4QuickIndex("徐雨健`"));
            mList.add(new Person4QuickIndex("吴亮"));
            mList.add(new Person4QuickIndex("王兆霖"));
            mList.add(new Person4QuickIndex("阿三"));
        }

        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
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
            if (convertView == null) {
                convertView = View.inflate(SlideMenuItem.this, R.layout.activity_item_slide, null);

                mViewHolder = new ViewHolder();
                mViewHolder.itemContent = convertView.findViewById(R.id.item_content_slide);
                mViewHolder.itemMenu = convertView.findViewById(R.id.item_menu_slide);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            //根据 位置得到内容
            final Person4QuickIndex person = mList.get(position);

            mViewHolder.itemContent.setText(person.getName());

            mViewHolder.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SlideMenuItem.this, "第" + position + "条数据：" + person.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            final SlideLayout slideLayout = (SlideLayout) convertView;
            mOnStateChangeListener = new MyOnStateChangeListener();
             slideLayout.setOnStateChangeListener(mOnStateChangeListener);
            /**
             * 给策划菜单设置点击事件:绝不能写在 getView里面；也不能写在监听器的实现类中！
             * 怎么实现呢？可以使用 mList 操作数据，但是绝不能用 listView 操作
             * 0 置顶
             * 1 未读
             * 2 删除
             */
            mViewHolder.itemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.add(0, mList.get(position));
                    mList.remove(position + 1);
                    notifyDataSetChanged();
                }
            });
            mViewHolder.itemMenu.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.get(position).setName( mList.get(position).getName() + "未读");
                    notifyDataSetChanged();
                }
            });

            mViewHolder.itemMenu.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(mList.get(position));
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    private SlideLayout mSlideLayout;
    private MyOnStateChangeListener mOnStateChangeListener;

    /**
     * 解决 删除 某一 item后，下一个 item自动打开 的BUG
     */
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {
        /**
         * 如果关闭的 menu 就是 就是刚才的那个 menu，那么 释放资源
         *
         * @param layout
         */
        @Override
        public void onClose(SlideLayout layout) {
            if (mSlideLayout == layout) {
                mSlideLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (mSlideLayout != null && mSlideLayout != layout) {
                mSlideLayout.closeMenu();
            }
        }

        /**
         * 当 打开了 menu 的时候，要记录是哪一个item
         *
         * @param layout
         */
        @Override
        public void onOpen(SlideLayout layout) {
            mSlideLayout = layout;
        }
    }

    static class ViewHolder {
        TextView itemContent;
        LinearLayout itemMenu;
    }
}

