package com.example.customview.achartengine_test_item;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

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

/**
 * @author: LiuSaiSai
 * @date: 2020/08/20 08:15
 * @description: 使用 开源库 AChartEngine 进行画图
 */
public class AChartEngineTestActivity extends AppCompatActivity {
    /**
    * 坐标系的底子
    */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private double[] standardConvert;
    private double interval = 0.05;
    private GraphicalView mChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achartengine_test);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("AChartEngine画轮对");

        drawChart();
    }

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
