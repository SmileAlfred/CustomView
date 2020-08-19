package com.example.customview.photobackcolor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/19 18:25
 * @description: 操作像素实现更换图片指定颜色；
 */
public class ReplaceBackColorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    private ImageView iv1, iv2;
    private Bitmap srcBmp, desBmp;
    private static final String TAG = ReplaceBackColorActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_back_color);
        initView();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("替换像素颜色");

        iv1 = findViewById(R.id.iv_photo_resource);
        iv2 = findViewById(R.id.iv_photo_target);

        //真奇葩，这里解析必须是 .png 图片？jpg 都不行的？
        srcBmp = BitmapFactory.decodeResource(getResources(), R.drawable.blueback);
        iv1.setImageBitmap(srcBmp);
        desBmp = srcBmp.copy(Bitmap.Config.ARGB_8888, true);
        btn = findViewById(R.id.btn_replace_pixel);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_replace_pixel:
                int tempRed = 0, tempGreen = 0, tempBlue = 0,tempAlpha = 0;
                for (int h = 0; h < srcBmp.getHeight(); h++) {
                    for (int w = 0; w < srcBmp.getWidth(); w++) {
                        int originColor = srcBmp.getPixel(w, h);

                        int red = Color.red(originColor);
                        int alpha = Color.alpha(originColor);
                        int green = Color.green(originColor);
                        int blue = Color.blue(originColor);
                        if (w == 0 && h == 0) {
                            tempAlpha = alpha;
                            tempRed = red;
                            tempGreen = green;
                            tempBlue = blue;
                            Log.i(TAG, "onClick: temp alpha = " +tempAlpha + " ;tempRed = "+tempRed + " ;tempGreen = " + tempGreen+" ;tempBlue = " + tempBlue);
                        }
                        if (w == 314 && h == 128) {
                            Log.i(TAG, "onClick: alpha = " +alpha + " ;red = "+red + " ;green = " + green+" ;blue = " + blue);
                        }

                        if (red > (tempRed - 110) && red < (tempRed + 30) &&
                                green > (tempGreen - 110) && green < (tempGreen + 20) &&
                                blue > (tempBlue - 110) && blue < (tempBlue + 20)
                                || alpha < 200) {
                            blue = 30;
                            green = 30;
                            red = 222;
                        }
                        desBmp.setPixel(w, h, Color.argb(alpha, red, green, blue));
                    }
                }
                iv2.setImageBitmap(desBmp);
                break;
            default:
                break;
        }
    }
}
