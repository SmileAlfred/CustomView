package com.example.customview.getmyphonenumber;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 获取手机通讯录信息
 * 1. ContentResolver 的使用；
 * 2. 安卓 6 之后的权限申请；
 * 3. ListView 的使用；
 */
public class GetNumberActivity extends Activity {

    private ListView lv;
    private GetNumberAdapter adapter;
    private String[] permissions;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getnumber);

        lv = (ListView) findViewById(R.id.lv);
        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("通讯录列表");

        /**
         * 权限申请
         */
        permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        requestPermissions(permissions, 1);
        getNumber(this);

        adapter = new GetNumberAdapter(lists, this);
        lv.setAdapter(adapter);
    }


    /**
     * 自定义 adapter
     */
    class GetNumberAdapter extends BaseAdapter {

        private List<GetNumberPhoneInfo> lists;
        private Context context;
        private LinearLayout layout;

        public GetNumberAdapter(List<GetNumberPhoneInfo> lists, Context context) {
            this.lists = lists;
            this.context = context;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.getphonenumber_item, null);
                holder = new ViewHolder();
                holder.nametv = (TextView) convertView.findViewById(R.id.name);
                holder.numbertv = (TextView) convertView.findViewById(R.id.number);
                holder.nametv.setText(lists.get(position).getName());
                holder.numbertv.setText(lists.get(position).getNumber());
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.nametv.setText(lists.get(position).getName());
                holder.numbertv.setText(lists.get(position).getNumber());
            }
            return convertView;
        }

        private class ViewHolder {
            TextView nametv;
            TextView numbertv;
        }
    }

    /**
     * bean 文件
     */
    class GetNumberPhoneInfo {
        private String name;
        private String number;

        public GetNumberPhoneInfo(String name, String number) {
            setNumber(number);
            setName(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    /**
     * 获取手机通讯录
     */
    public List<GetNumberPhoneInfo> lists = new ArrayList<GetNumberPhoneInfo>();
    public String getNumber(Context context) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String phoneNumber, phoneName;
        while (cursor.moveToNext()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            GetNumberPhoneInfo getNumberPhoneInfo = new GetNumberPhoneInfo(phoneName, phoneNumber);
            lists.add(getNumberPhoneInfo);
        }
        return null;
    }
}
