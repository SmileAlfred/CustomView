package com.example.customview.color_matrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/17 20:30
 * @description: ColorMatrix 色彩变换，来自启舰《自定义控件》S11
 * 将色彩的 ARGB 参数解析成矩阵，通过矩阵的运算，对不同色彩进行加强或转变。
 */
public class ColorMatrixActivity extends AppCompatActivity {
    private Bitmap originBitmap, tempBitmap;
    private ImageView iv_color_saturation;
    private static final String TAG = ColorMatrixActivity.class.getClass().getSimpleName();

    private TextView tv_saturation;
    private TextView tv_red, tv_red_rot;
    private TextView tv_green, tv_green_rot;
    private TextView tv_blue, tv_blue_rot;

    private SeekBar sb_color_matrix;
    private SeekBar sb_red_matrix, sb_red_rot;
    private SeekBar sb_green_matrix, sb_green_rot;
    private SeekBar sb_blue_matrix, sb_blue_rot;
    int rotateAxis = 0;

    /**
     * 实现对多 SeekBar 的监听
     */
    private int saturation = 1;
    private float redScale = 1f;
    private float greenScale = 1f;
    private float blueScale = 1f;
    private int rotate = 180;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_matrix);

        initView();
    }

    private void initView() {
        iv_color_saturation = findViewById(R.id.iv_color_saturation);
        tv_saturation = findViewById(R.id.tv_saturation);
        tv_red = findViewById(R.id.tv_red);
        tv_green = findViewById(R.id.tv_green);
        tv_blue = findViewById(R.id.tv_blue);
        tv_red_rot = findViewById(R.id.tv_red_rot);
        tv_green_rot = findViewById(R.id.tv_green_rot);
        tv_blue_rot = findViewById(R.id.tv_blue_rot);

        sb_color_matrix = findViewById(R.id.sb_color_matrix);
        sb_red_matrix = findViewById(R.id.sb_red_matrix);
        sb_green_matrix = findViewById(R.id.sb_green_matrix);
        sb_blue_matrix = findViewById(R.id.sb_blue_matrix);
        sb_red_rot = findViewById(R.id.sb_red_rot);
        sb_green_rot = findViewById(R.id.sb_green_rot);
        sb_blue_rot = findViewById(R.id.sb_blue_rot);


        //调用函数 改变色彩饱和度；0 - 黑白；1 - 原图；max - 色彩鲜明；
        originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.advance_forest);
        tempBitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        sb_color_matrix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saturation = progress;
                //不能直接设置 progress，要转换成 String
                tv_saturation.setText(String.valueOf(progress));
                Bitmap bitmap = handleColorMatrixBmp(progress, redScale, greenScale, blueScale, rotateAxis, 1);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_red_matrix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //不能直接设置 progress，要转换成 String
                redScale = progress / 10f;
                tv_red.setText(String.valueOf(redScale));
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 2);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_green_matrix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenScale = progress / 10f;
                //不能直接设置 progress，要转换成 String
                tv_green.setText(String.valueOf(greenScale));
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 2);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_blue_matrix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueScale = progress / 10f;
                //不能直接设置 progress，要转换成 String
                tv_blue.setText(String.valueOf(blueScale));
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 2);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_red_rot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotate = progress;
                //不能直接设置 progress，要转换成 String
                tv_red_rot.setText(String.valueOf(rotate));
                rotateAxis = 0;
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 3);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_green_rot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotate = progress;
                //不能直接设置 progress，要转换成 String
                tv_green_rot.setText(String.valueOf(rotate));
                rotateAxis = 1;
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 3);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_blue_rot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotate = progress;
                //不能直接设置 progress，要转换成 String
                tv_blue_rot.setText(String.valueOf(rotate));
                rotateAxis = 2;
                Bitmap bitmap = handleColorMatrixBmp(saturation, redScale, greenScale, blueScale, rotateAxis, 3);
                iv_color_saturation.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 设置饱和度 & 处理图片 色彩等信息；二选一，
     *
     * @param saturation 饱和度
     * @param red        红色 缩放
     * @param green      红色 缩放
     * @param blue       红色 缩放
     * @param rotateAxis 绕哪个轴旋转          0 - Red；1 - Green；2 - Blue
     * @param i          0 - 改变图片饱和度；1 - 进行色彩缩放；2 - 色彩旋转
     * @return
     */
    private Bitmap handleColorMatrixBmp(int saturation, float red, float green, float blue, int rotateAxis, int i) {
        // 创建一个相同尺寸的可变的位图区，用于绘制调色后的图片 - 饱和度
        Canvas canvas = new Canvas(tempBitmap);
        Paint paint = new Paint();

        ColorMatrix mColorMatrix = new ColorMatrix();
        if (i == 1) {
            tv_red.setText("1.0");
            sb_red_matrix.setProgress(10);
            tv_green.setText("1.0");
            sb_green_matrix.setProgress(10);
            tv_blue.setText("1.0");
            sb_blue_matrix.setProgress(10);
            tv_red_rot.setText("180");
            tv_green_rot.setText("180");
            tv_blue_rot.setText("180");
            sb_red_rot.setProgress(180);
            sb_green_rot.setProgress(180);
            sb_blue_rot.setProgress(180);

            //设置饱和度
            mColorMatrix.setSaturation(saturation);
        } else if (i == 2) {
            tv_saturation.setText("1");
            sb_color_matrix.setProgress(1);
            tv_red_rot.setText("180");
            tv_green_rot.setText("180");
            tv_blue_rot.setText("180");
            sb_red_rot.setProgress(180);
            sb_green_rot.setProgress(180);
            sb_blue_rot.setProgress(180);

            //生成色彩变化矩阵，
            mColorMatrix.setScale(red, green, blue, 1);
        } else if (i == 3) {
            tv_saturation.setText("1");
            sb_color_matrix.setProgress(1);
            tv_red.setText("1.0");
            sb_red_matrix.setProgress(10);
            tv_green.setText("1.0");
            sb_green_matrix.setProgress(10);
            tv_blue.setText("1.0");
            sb_blue_matrix.setProgress(10);

            //色彩旋转 0 - 红色旋转；1 - 绿色旋转；2 - 蓝色旋转；
            /**
             * 如果 依次 设置setRotate 那就会使前面的失效，只有最后一个有效；待办事项：能叠加吗？
             */
            mColorMatrix.setRotate(rotateAxis, rotate - 180);
        }
        paint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));

        //将色彩变换后的图片输出到新创建的位图区
        canvas.drawBitmap(originBitmap, 0, 0, paint);

        return tempBitmap;
    }
}