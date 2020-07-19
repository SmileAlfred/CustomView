package com.example.customview.dy_loading;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.customview.R;


/**
 * @author LiuSaiSai
 * @date :2020/07/18 10:00
 * @description: bug:我正在尝试使用DialogFragment.getDialog（）获取使用扩展DialogFragment创建的对话框，但它返回null。
 * 原因：   您在DialogFragmen's生命周期中太早调用了getDialog（）。
 * getDialog（）仅从DialogFragment返回私有变量mDialog。
 * 实例化DialogFragment时，mDialog为null，然后在getLayoutInflater（Bundle savedInstanceState）内部触发onCreateDialog时
 * 将其设置，因此必须在onCreateDialog之后调用getDialog。
 * 例如，某些常用方法的顺序为onCreate，onCreateDialog和onCreateView，onStart。 因此，您可以调用getDialog并使其
 * 在onCreateView或onStart中返回某些内容，但不在onCreate或onCreateDialog中返回。
 * 尽管在用户可见片段时会调用onStart，但在那一点调整片段的布局看起来很好。例如，使用getDialog（）。
 * getWindow（）。setLayout（...）设置宽度和高度。  ，...）; 不会使片段看起来改变大小，而只是看起来具有新设置的大小。
 */
public class DyLoadingDialog extends DialogFragment {
    private View mRootView;
    private DyLoadingView mDyLoadingView;
    private static final String TAG = "DyLoadingDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_dy_loading, container, false);
        mDyLoadingView = mRootView.findViewById(R.id.dy_loading);
        Log.i("TAG", "onCreateView: ");
        return mRootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        Dialog dialog = getDialog();
        //TODO:事实上，这里的设置动画尺寸并没有执行，因为，dialog == null；这是个没有解决的BUG
        if (dialog != null) {
            Window window = getDialog().getWindow();

            Log.i("TAG", "window != null: " + window);

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0.0f;
            lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(lp);
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
        } else {
            Log.i("TAG", "dialog == null: " + dialog);
        }

        //开始动画
        mDyLoadingView.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDyLoadingView.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDyLoadingView = null;
    }
}