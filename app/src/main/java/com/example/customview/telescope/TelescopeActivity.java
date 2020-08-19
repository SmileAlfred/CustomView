package com.example.customview.telescope;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.MyUtils;
import com.example.customview.R;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/18 19:48
 * @description: 望远镜效果
 */
public class TelescopeActivity extends AppCompatActivity {

    private TextView tv_telescope_factor, tv_telescope_radius;
    private SeekBar sb_telescope_factor, sb_telescope_radius;
    private MyTelescopeView telescope_view;

    public int telescope_factor = 3;
    public int telescope_radius = 320;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telescope);
        initView();
    }

    private void initView() {
        MyUtils.hideWindows(this);

        tv_telescope_factor = findViewById(R.id.tv_telescope_factor);
        tv_telescope_radius = findViewById(R.id.tv_telescope_radius);

        telescope_view = findViewById(R.id.telescope_view);
        sb_telescope_factor = findViewById(R.id.sb_telescope_factor);
        sb_telescope_radius = findViewById(R.id.sb_telescope_radius);

        sb_telescope_factor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                telescope_factor = progress;
                tv_telescope_factor.setText(String.valueOf(progress));
                sb_telescope_factor.setProgress(progress);
                telescope_view.setParameter(telescope_radius, telescope_factor,true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_telescope_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                telescope_radius = progress;
                tv_telescope_radius.setText(String.valueOf(progress));
                sb_telescope_radius.setProgress(progress);
                telescope_view.setParameter(telescope_radius, telescope_factor,false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}