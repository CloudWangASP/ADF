package com.cloud;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.cookie.Cookie;

import java.util.List;

import static com.cloud.BaseApplication.getApplication;

public class BaseHttpClient {

    private static AsyncHttpClient client;
    private static PersistentCookieStore cookieStore;
    private static final int REQUEST_TIMEOUT = 10 * 1000;
    
    static {
        client = new AsyncHttpClient();
        client.setTimeout(REQUEST_TIMEOUT);
        cookieStore = new PersistentCookieStore(getApplication());
        client.setCookieStore(cookieStore);
    }

    public static List<Cookie> getCookies() {
        return cookieStore.getCookies();
    }

    public static void setCookies(Cookie cookie) {
        cookieStore.addCookie(cookie);
        client.setCookieStore(cookieStore);
    }

    public static void clearCookies() {
        cookieStore.clear();
        client.setCookieStore(cookieStore);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post(url, params, responseHandler, null);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context) {

        if (getApplication().isNetworkAvailable()) {
            client.post(context, url, params, responseHandler);
        } else {
            responseHandler.onFailure(504, null, null, new Throwable("504 Gateway Timeout"));
            responseHandler.onFinish();
        }

    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {

        if (getApplication().isNetworkAvailable()) {
            client.get(url, responseHandler);
        } else {
            responseHandler.onFailure(504, null, null, new Throwable("504 Gateway Timeout"));
            responseHandler.onFinish();
        }

    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler, Context context) {
        
        if (getApplication().isNetworkAvailable()) {
            client.get(context, url, responseHandler);
        } else {
            responseHandler.onFailure(504, null, null, new Throwable("504 Gateway Timeout"));
            responseHandler.onFinish();
        }

    }

    /**
     * 结束当前的数据请求
     *
     * @param context
     */
    public static void cancelRequest(Context context) {
        client.cancelRequests(context, true);
    }
    

    /**
     * 添加header
     *
     * @param header
     * @param value
     */
    public static void addHttpHeader(String header, String value) {
        client.addHeader(header, value);
    }
    
}
