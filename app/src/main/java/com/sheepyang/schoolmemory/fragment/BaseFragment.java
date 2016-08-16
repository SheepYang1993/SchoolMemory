package com.sheepyang.schoolmemory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.util.MyToast;
import com.sheepyang.schoolmemory.view.dialog.CustomProgressDialog;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/11.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    public CustomProgressDialog mLoadingPD;
    public MyUser mCurrentUser;
    public Intent mIntent;

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mLoadingPD = new CustomProgressDialog(getActivity(), "loading");
        mCurrentUser = MyUser.getCurrentUser(MyUser.class);
        initView();
        initData();
    }

    public void showToast(int resId) {
        if (!getActivity().isFinishing()) {
            MyToast.showMessage(getActivity(), resId);
        }
    }

    public void showToast(String msg) {
        if (!getActivity().isFinishing()) {
            MyToast.showMessage(getActivity(), msg);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
