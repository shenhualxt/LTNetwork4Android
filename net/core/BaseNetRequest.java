package com.sina.libcomponent.lcsnetwork.net.core;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by XianTing on 17/1/7.
 * Description:  http 请求
 */
public abstract class BaseNetRequest<T> {

    private Context mContext;

    private String mCancelTag = "";

    private String mResponseString;

    private NetError mError;

    private String mDebugUrl;

    private OnNetCallback mCallback;

    private int requestMethod= Method.GET;

    public BaseNetRequest(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
        mCancelTag = context.getClass().getSimpleName();
        Log.i( "BaseNetReq:mCancelTag", mCancelTag );
    }

    @SuppressWarnings("unused")
    public void setCancelTag(String cancelTag) {
        mCancelTag = cancelTag;
    }

    public void setCallback(OnNetCallback<T> callback) {
        mCallback = callback;
    }

    public void request(OnNetCallback<BaseNetRequest<T>> callback) {
        this.mCallback = callback;
        if (getContext() != null) {
            start();
        }
    }

    public abstract void start();

    public abstract void cancel();

    public abstract String requestUrl();

    public abstract Map<String, String> requestParams();

    public Context getContext() {
        return mContext;
    }

    public Map<String, String> requestHeader() {
        return null;
    }

    public int requestMethod() {
        return requestMethod;
    }

    OnNetCallback<T> requestCallback() {
        return mCallback;
    }

    /**
     * 回调接口
     */
    public interface OnNetCallback<T> extends OnSuccessListener<T>,OnFailedListener<T> {
    }

    interface OnSuccessListener<T> {
        void onSuccess(T request);
    }

    interface OnFailedListener<T> {
        void onFail(T request);
    }


    public String getResponseString() {
        return mResponseString;
    }

    public void setResponseString(String responseString) {
        mResponseString = responseString;
    }


    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * Supported request methods.
     */
    // TODO: support more methods
    @SuppressWarnings("unused")
    public interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    String getCancelTag() {
        return mCancelTag;
    }

    public void setError(NetError error) {
        mError = error;
    }

    public NetError getError() {
        return mError;
    }

    public void setDebugUrl(String debugUrl) {
        mDebugUrl = debugUrl;
    }

    @SuppressWarnings("unused")
    public String getDebugUrl() {
        return mDebugUrl;
    }

    public OnNetCallback getCallback() {
        return mCallback;
    }
}
