package com.example.customview.opencv_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.touch_eventtest.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCvTestActivity extends AppCompatActivity {

    public static final String TAG = OpenCvTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv_test);

       // iniLoadOpenCV();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //载入OpenCv初始化引擎，并载入库，可以看到initAsync，使用的是异步加载
        //因此需要提供回调函数
        //OpenCVLoader.OPENCV_VERSION_3_2_0指的是版本号，请根据你自己的版本选择

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        convert2GraySimple();
    }
    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    Log.e("OpenCv", "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.e("OpenCv", "加载失败");
                    break;
            }
        }
    };


    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if (success) {
            Log.i(TAG, "OpenCV Libraries loaded...");
        } else {
            Toast.makeText(this.getApplicationContext(), "WARNING: Could not load OpenCV Libraries!", Toast.LENGTH_LONG).show();
        }
    }

    private void convert2GraySimple() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(dst, bitmap);
        ImageView iv = (ImageView) this.findViewById(R.id.sample_img);
        iv.setImageBitmap(bitmap);
        src.release();
        dst.release();
    }
}