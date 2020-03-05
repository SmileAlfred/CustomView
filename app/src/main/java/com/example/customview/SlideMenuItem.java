package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description: 自定义 侧滑菜单
 * × × × × × × × 注意 .getView() 方法中，一定要用 参数的 view.findViewById();    不可以 直接 findViewById()
 * @date :2020/03/04 17:35
 */
public class SlideMenuItem extends AppCompatActivity {

    private ListView mListViewiew;

    private List<Person4QuickIndex> mList;

    private MyAdapter adapter;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu_item);
//        setContentView(R.layout.activity_item_slide);

        mListViewiew = findViewById(R.id.list_view_slide_menu);
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
        mListViewiew.setAdapter(adapter);
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

            final TextView selectedTextView = mViewHolder.itemContent;
            mViewHolder.itemContent.setText(person.getName());

            mViewHolder.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SlideMenuItem.this, "第" + position + "条数据：" + person.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            TextView toTop = mViewHolder.itemMenu.findViewById(R.id.to_top_slide_menu);
            TextView unRead = mViewHolder.itemMenu.findViewById(R.id.unread_slide_menu);
            TextView delete = mViewHolder.itemMenu.findViewById(R.id.delete_slide_menu);

            toTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.add(0, person);
                    mList.remove(position + 1);
                    mListViewiew.getChildAt(0).setBackgroundColor(Color.YELLOW);
                    notifyDataSetChanged();
                }
            });
            unRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTextView.setTextColor(Color.RED);
                    notifyDataSetChanged();     //必须要刷新
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(person);
                    notifyDataSetChanged();
                }
            });

            SlideLayout SlideLayout = (SlideLayout) convertView;
            mOnStateChangeListener = new MyOnStateChangeListener();
            SlideLayout.setOnStateChangeListener(mOnStateChangeListener);
            return convertView;
        }
    }

    private SlideLayout mSlideLayout;
    private MyOnStateChangeListener mOnStateChangeListener;

    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {

        @Override
        public void onClose(SlideLayout layout) {
            if (mSlideLayout != layout) {
                mSlideLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (mSlideLayout != null && mSlideLayout != layout) {
                mSlideLayout.closeMenu();
            }
        }

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