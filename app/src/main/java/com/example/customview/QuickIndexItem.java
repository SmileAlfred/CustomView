package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description: 注意 ListView 中 .setSelection(int position) 方法；传入的position 是其 item 从头数的个数。
 * @date :2020/03/04 11:05
 */
public class QuickIndexItem extends AppCompatActivity {

    private ListView mListView;
    private TextView mTextView;
    private IndexView mIndexView;
    /**
     * 延时操作都是使用的 handler；并且 如果handler 运行在 主线程，那么延时操作也运行在 主线程
     */
    private Handler mHandler = new Handler();
    /**
     * 联系人 集合
     */
    private List<Person4QuickIndex> persons;

    private IndexAdapter adapter;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_index_item);

        TextView titleQuickIndex = findViewById(R.id.title_text_view);
        titleQuickIndex.setText("快速检索联系人");

        mListView = findViewById(R.id.list_view_quick_index);
        mTextView = findViewById(R.id.word_text_view_quick_index);
        mIndexView = findViewById(R.id.words_index_view);

        /**
         * 设置监听字母下标 索引的变化
         */
        mIndexView.setOnIndexChangeListener(new MyOnIndexChangeListener());

        /**
         * 准备数据
         */
        initData();

        /**
         * 设置适配器
         */
        adapter = new IndexAdapter();
        mListView.setAdapter(adapter);
    }

    class IndexAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return persons.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(QuickIndexItem.this, R.layout.activity_person_item_quick_index, null);

                mViewHolder = new ViewHolder();
                mViewHolder.wordText = convertView.findViewById(R.id.word_text_view_Item);
                mViewHolder.nameText = convertView.findViewById(R.id.name_text_view_Item);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            String word = persons.get(position).getPinyin().substring(0, 1);
            String name = persons.get(position).getName();
            mViewHolder.wordText.setText(word);
            mViewHolder.nameText.setText(name);

            if (position == 0) {
                mViewHolder.wordText.setVisibility(View.VISIBLE);
            } else {
                /**
                 * 得到前一个位置对应的字母，如果当前的字母和上一个相同，隐藏；否则就显示
                 */
                String preWord = persons.get(position - 1).getPinyin().substring(0, 1);
                if (word.equals(preWord)) {
                    mViewHolder.wordText.setVisibility(View.GONE);
                } else {
                    mViewHolder.wordText.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView wordText;
        TextView nameText;
    }

    class MyOnIndexChangeListener implements IndexView.OnIndexChangeListener {

        /**
         * 返回的是 # ~ Z 的字母
         *
         * @param word 字母
         */
        @Override
        public void onIndexChange(String word) {
            upDateWord(word);
            upDateListView(word);

        }
    }

    private void upDateWord(String word) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(word);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {// 是在 主线程中进行的
                mTextView.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void upDateListView(String word) {
        for (int i = 0; i < persons.size(); i++) {
            String listWord = persons.get(i).getPinyin().substring(0, 1);
            if (word.equals(listWord)) {
                mListView.setSelection(i);  //注意这个 .setSelection() ;
                return;
            }
        }
    }


    /**
     * 初始化 人名
     */
    private void initData() {
        persons = new ArrayList<>();
        persons.add(new Person4QuickIndex("张晓飞"));
        persons.add(new Person4QuickIndex("杨光福"));
        persons.add(new Person4QuickIndex("胡继群"));
        persons.add(new Person4QuickIndex("刘畅"));

        persons.add(new Person4QuickIndex("钟泽兴"));
        persons.add(new Person4QuickIndex("尹革新"));
        persons.add(new Person4QuickIndex("安传鑫"));
        persons.add(new Person4QuickIndex("张骞壬"));

        persons.add(new Person4QuickIndex("温松"));
        persons.add(new Person4QuickIndex("李凤秋"));
        persons.add(new Person4QuickIndex("刘甫"));
        persons.add(new Person4QuickIndex("娄全超"));
        persons.add(new Person4QuickIndex("张猛"));

        persons.add(new Person4QuickIndex("王英杰"));
        persons.add(new Person4QuickIndex("李振南"));
        persons.add(new Person4QuickIndex("孙仁政"));
        persons.add(new Person4QuickIndex("唐春雷"));
        persons.add(new Person4QuickIndex("牛鹏伟"));
        persons.add(new Person4QuickIndex("姜宇航"));

        persons.add(new Person4QuickIndex("刘挺"));
        persons.add(new Person4QuickIndex("张洪瑞"));
        persons.add(new Person4QuickIndex("张建忠"));
        persons.add(new Person4QuickIndex("侯亚帅"));
        persons.add(new Person4QuickIndex("刘帅"));

        persons.add(new Person4QuickIndex("乔竞飞"));
        persons.add(new Person4QuickIndex("徐雨健"));
        persons.add(new Person4QuickIndex("吴亮"));
        persons.add(new Person4QuickIndex("王兆霖"));

        persons.add(new Person4QuickIndex("阿三"));
        persons.add(new Person4QuickIndex("李博俊"));


        //排序
        Collections.sort(persons, new Comparator<Person4QuickIndex>() {
            @Override
            public int compare(Person4QuickIndex lhs, Person4QuickIndex rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

    }

}
