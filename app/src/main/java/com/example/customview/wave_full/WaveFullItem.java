package com.example.customview.wave_full;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.customview.R;

public class WaveFullItem extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_full);


        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("水波纹完整版");
    }
}
