package com.cloud;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cloud.util.LoadingView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;

import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {

    Context context = this.getApplicationContext();
    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    public BaseApplication mApp;
    public Gson gson;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mApp = (BaseApplication) getApplication();
        gson = mApp.getGson();

        initStart();

        initLayout();

        init();

        initViewsListener();
    }

    protected void initStart() {

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
     * 跳转指定Activity
     */
    public void forwardActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }
    /**
     * 跳转指定Activity,带参
     */
    public void forwardActivity(Class<?> cls,String bundleName,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(bundleName,bundle);
        startActivity(intent);
    }

    /**
     * 关闭当前Activity
     */
    public void closeActivity() {
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    /**
     * 初始化监听
     */
    protected abstract void initViewsListener();

    @Override
    protected void onResume() {
        super.onResume();

        mApp.OnAppInForeground();

        MobclickAgent.onResume(this);

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (!isAppOnForeground()) {
            mApp.OnAppInBackground();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception ex) {

        }
    }

    public LoadingView getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = new LoadingView(this);
        }

        return mLoadingView;
    }


    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    //跳转指定Activity
//    public void forwardActivity(Class<?> cls) {
//        Intent intent = new Intent();
//        intent.setClass(this, cls);
//        startActivity(intent);
//    }
//
//    //关闭当前Activity
//    public void closeActivity() {
//        this.finish();
//    }

    //判断是否有网络连接
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断Wifi是否可用
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo mWiFiNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断移动网络是否可用
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            NetworkInfo mMobileNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断网络连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
