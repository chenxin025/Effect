package com.allen.androidcustomview.bottompop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.allen.androidcustomview.R;

import java.util.ArrayList;

/**
 * Created by chenxin on 2018/8/2.
 */

public class BottomDialog extends BaseDialogFragment {

    private static final String TAG = "BottomDialog";

    protected View fragmentRoot;

    private ArrayList<String> mList = new ArrayList<>();

    protected Dialog dialog;


    public static BottomDialog newInstance(){
        Bundle args = new Bundle();
        BottomDialog fragment = new BottomDialog();
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mList.add("test0");
        mList.add("test1");
        mList.add("test2");
        mList.add("test3");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.dialog_bottom_layout, null);

        initView(view);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        //设置没有效果
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (null != dialog) {
            //M: 设置全屏
            dialog.getWindow().setLayout(-1, -2);
        }
    }

    private void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list);

        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mList);
        listView.setAdapter(adapter);
    }


    //防止重复弹出
    public static BottomDialog showDialog(AppCompatActivity appCompatActivity) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        BottomDialog bottomDialogFragment =
                (BottomDialog) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment) {
            bottomDialogFragment = newInstance();
        }

        if (!appCompatActivity.isFinishing()
                && null != bottomDialogFragment
                && !bottomDialogFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(bottomDialogFragment, TAG)
                    .commitAllowingStateLoss();
        }

        return bottomDialogFragment;
    }
}
