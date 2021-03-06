package com.allen.androidcustomview.bottompop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by bobomee on 16/1/11.
 */
public abstract class BaseDialogFragment extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected View fragmentRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == fragmentRoot) {
            fragmentRoot = initFragmentView(inflater, container, savedInstanceState);
        }

        if (null != fragmentRoot) {
            ViewGroup parent = (ViewGroup) fragmentRoot.getParent();
            if (null != parent)
                parent.removeAllViews();
        }else {
            fragmentRoot = super.onCreateView(inflater, container, savedInstanceState);
        }



        return fragmentRoot;
    }

    public abstract View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
