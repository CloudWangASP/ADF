package com.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cloud.util.LoadingView;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;


public abstract class BaseFragment extends Fragment {

    public BaseApplication mApp;
    public Gson gson;

    private LoadingView mLoadingView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mApp = (BaseApplication) getActivity().getApplication();
        gson = mApp.getGson();

        initLayout();

        init();

        initViewsListener();
    }

    /**
     * 初始化View
     */
    protected abstract void initLayout();

    /**
     * 初始化数据
     */
    protected abstract void init();


    /**
     * 初始化监听
     */
    protected abstract void initViewsListener();

    public LoadingView getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = ((BaseActivity) getActivity()).getLoadingView();
        }
        return mLoadingView;
    }

    /**
     * 跳转指定Activity
     */
    public void forwardActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * 带参跳转指定Activity
     */
    public void forwardActivity(Class<?> cls,String bundleName,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtra(bundleName,bundle);
        startActivity(intent);
    }

    /**
     * 关闭当前Activity
     */
    public void closeActivity() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLoadingView != null) {
            mLoadingView.hideLoding();
            mLoadingView.hideTipInfo();
        }

        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception ex) {

        }
    }

}
