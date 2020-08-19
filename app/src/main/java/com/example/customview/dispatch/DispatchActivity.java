package com.example.customview.dispatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.customview.R;

/**
 * 测试事件分发机制；
 * 功能：
 * 随机填充图片；
 * 每一列都可以自由滚动，当摁在屏幕的上 1/2 内 且在 中间一竖内 滑动，
 * 整个页面都会一起滑动
 */
public class DispatchActivity extends Activity {

    private ListView lv1;
    private ListView lv2;
    private ListView lv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("事件分发");

        lv1 = findViewById(R.id.lv1);
        lv2 = findViewById(R.id.lv2);
        lv3 = findViewById(R.id.lv3);

        try {
            lv1.setAdapter(new MyAdapter());
            lv2.setAdapter(new MyAdapter());
            lv3.setAdapter(new MyAdapter());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int ids[] = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3};

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3000;
        }

        @Override
        public Object getItem(int position) {
            return 150;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView iv = (ImageView) View.inflate(getApplicationContext(), R.layout.lv_item, null);
            int resId = (int) (Math.random() * ids.length);
            iv.setImageResource(ids[resId]);
            return iv;
        }

    }
}
