package com.example.customview.achartengine_test_item;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.customview.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/20 08:15
 * @description: 使用 开源库 AChartEngine 进行画图
 * View 的宽高其实是在 measure 时确定的；和 AppCompatActivity 的生命周期无关！
 */
public class AChartEngineTestActivity extends AppCompatActivity {
    private static final String TAG = AChartEngineTestActivity.class.getSimpleName();

    /**
     * 画图 - 坐标系的底子
     */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private double[] standardConvert;
    private double interval = 0.05;
    private GraphicalView mChartView;
    /**
     * BottomSheetBehavior Test - 从底部划出另一个布局
     */
    private BottomSheetBehavior bottomSheetBehavior;
    private ScrollView scv_bottom_sheet_behavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achartengine_test);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("AChartEngine画轮对");

        UpLoadMenu();

        drawChart();
    }

    /**
     * 上划 调出菜单
     */
    @SuppressLint("ClickableViewAccessibility")
    private void UpLoadMenu() {
        scv_bottom_sheet_behavior = findViewById(R.id.scv_bottom_sheet_behavior);
        bottomSheetBehavior = BottomSheetBehavior.from(scv_bottom_sheet_behavior);

        TextView tv_test = findViewById(R.id.tv_test);
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AChartEngineTestActivity.this, "测试点击按钮", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 如果 ScrollView 位于列表顶端那么获取事件；否则释放
         */
        scv_bottom_sheet_behavior.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                if (!scv_bottom_sheet_behavior.canScrollVertically(-1)) {
                    scv_bottom_sheet_behavior.requestDisallowInterceptTouchEvent(false);
                } else {
                    scv_bottom_sheet_behavior.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });


        Display display = getWindowManager().getDefaultDisplay();
        //设置高度        app:behavior_peekHeight="600dp"
        int height = display.getHeight() * 2 / 3;
        //实际高度；如果 ScrollView 的高度没有达到 2/3 时的实际高度；
        final int[] factHeight = new int[1];
        scv_bottom_sheet_behavior.post(new Runnable() {
            @Override
            public void run() {
                factHeight[0] = scv_bottom_sheet_behavior.getHeight();
            }
        });

        bottomSheetBehavior.setPeekHeight(height);

        if (height >= factHeight[0]) {
            CoordinatorLayout.LayoutParams coordParams = (CoordinatorLayout.LayoutParams) scv_bottom_sheet_behavior.getLayoutParams();
            coordParams.height = height;
            scv_bottom_sheet_behavior.setLayoutParams(coordParams);
        }
        //设置默认先隐藏
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    @Override
    public void finish() {
        if (bottomSheetBehavior.getState() == STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.finish();
        }
    }

    /**
     * 通过 相上滑动手势 打开底部弹窗
     */
    int originY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                originY = (int) event.getY();
                if (bottomSheetBehavior.getState() == STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            case MotionEvent.ACTION_MOVE:
                int lastY = (int) event.getY();
                if (originY - lastY > 50) {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.setState(STATE_COLLAPSED);
                    }
                }
                if (bottomSheetBehavior.getState() == STATE_COLLAPSED && lastY - originY > 50) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 用 achartengine 画出轮对轮廓
     */
    private void drawChart() {
        initMRender(mRenderer);

        InputStream inputStream = getResources().openRawResource(R.raw.a);
        String stringFromTxt = getString(inputStream);
        String[] standard = stringFromTxt.split(" ");
        standardConvert = new double[standard.length];

        for (int i = 0; i < standard.length; i++) {
            try {
                String s = standard[i];
                standardConvert[i] = -Double.parseDouble(s.substring(0, 7));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        XYSeries series = new XYSeries("轮对轮廓");
        for (int k = standard.length - 1; k >= 0; k--) {
            // 填x,y值
            series.add(interval * (standard.length - k), -standardConvert[k] + 30);
        }


        mDataset.addSeries(series);

        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();

        mRenderer.addSeriesRenderer(renderer);

        // set some renderer properties
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(0.001f);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValues(false);
        renderer.setDisplayChartValuesDistance(30);
        renderer.setColor(Color.RED);


        LinearLayout ll_chart = (LinearLayout) findViewById(R.id.ll_chart);

        mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        ll_chart.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

    }

    /**
     * 初始化 XYMultipleSeriesRenderer
     */
    private void initMRender(XYMultipleSeriesRenderer renderer) {
        renderer.setMarginsColor(android.R.color.white);
        renderer.setAxisTitleTextSize(30);
        renderer.setChartTitleTextSize(40);
        renderer.setLabelsTextSize(30);
        renderer.setLegendTextSize(30);
        renderer.setMargins(new int[]{20, 60, 60, 20});
        renderer.setZoomButtonsVisible(true);
        renderer.setPointSize(1f);
        renderer.setClickEnabled(true);
        renderer.setSelectableBuffer(10);
        renderer.setXAxisMax(140);
        renderer.setXAxisMin(-10);
        renderer.setYAxisMax(85);
        renderer.setYAxisMin(0);
        renderer.setXTitle("X/mm");
        renderer.setYTitle("Y/mm");
        renderer.setZoomEnabled(false, false);
        renderer.setShowGrid(true);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
    }

    /**
     * 将 输入流 转换成 String
     *
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
