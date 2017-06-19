package com.sina.libcomponent.lcsnetwork.net.batchrequest;

import android.content.Context;
import android.util.Log;

import com.sina.libcomponent.lcsnetwork.net.core.BaseNetRequest;
import com.sina.libcomponent.lcsnetwork.net.plugins.AccessoryManager;
import com.sina.libcomponent.lcsnetwork.net.plugins.RequestAccessory;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by XianTing on 17/1/7.
 * Description:  http 批量请求
 */
@SuppressWarnings("unused")
public class BatchRequest implements BaseNetRequest.OnNetCallback<BaseNetRequest> {

    private int mFinishedCount = 0;

    private BaseNetRequest mFailedRequest;

    private ArrayList<BaseNetRequest> mRequestList = new ArrayList<>();

    private OnBatchRequestCallback mBatchRequestCallback;

    private AccessoryManager<BatchRequest> mAccessoryManager;

    private ArrayList<RequestAccessory<BatchRequest>> mRequestAccessories = new ArrayList<>();

    private Context mContext;

    @SuppressWarnings("unused")
    public BatchRequest(Context context) {
        mAccessoryManager = new AccessoryManager<>();
        mContext=context;
    }

    @SuppressWarnings("unused")
    public BatchRequest(Context context, ArrayList<BaseNetRequest> requestList) {
        this(context);
        mRequestList = requestList;
    }

    @SuppressWarnings("unused")
    public BatchRequest(Context context, BaseNetRequest[] requestList) {
        this(context);
        Collections.addAll( mRequestList, requestList );
    }


    public void request(OnBatchRequestCallback callback) {
        mBatchRequestCallback = callback;
        start();
    }

    @SuppressWarnings("unused")
    public void addRequest(BaseNetRequest request) {
        mRequestList.add( request );
    }

    public void start() {
        if (mFinishedCount > 0) {
            Log.e( this.getClass().getSimpleName(), "Batch Request has already stated" );
            return;
        }
        mFailedRequest = null;
        getAccessoryManager().triggerAccessoriesStartCallback(this);
        for (BaseNetRequest netRequest : mRequestList) {
            netRequest.setCallback( this );
            netRequest.start();
        }
    }

    public void cancel() {
        for (BaseNetRequest netRequest : mRequestList) {
            netRequest.cancel();
        }
        getAccessoryManager().triggerAccessoriesResponseCancelledCallback(this);
        getAccessoryManager().triggerAccessoriesFinishCallback(this);
    }


    /**
     * OnNetCallback 接口方法
     */
    @Override
    public void onSuccess(BaseNetRequest request) {
        mFinishedCount++;
        if (mFinishedCount % mRequestList.size()==0) {
            getAccessoryManager().triggerAccessoriesResponseCallback(this);
            if (mBatchRequestCallback != null) {
                mBatchRequestCallback.onSuccess( this );
            }
            getAccessoryManager().triggerAccessoriesResponseHandledFinishedCallback(this);
            getAccessoryManager().triggerAccessoriesFinishCallback(this);
        }
    }

    @Override
    public void onFail(BaseNetRequest request) {
        this.mFailedRequest = request;
        for (BaseNetRequest netRequest : mRequestList) {
            netRequest.cancel();
        }
        if (mBatchRequestCallback != null) {
            mBatchRequestCallback.onFail( this );
        }
        getAccessoryManager().triggerAccessoriesFailedCallback(this);
        getAccessoryManager().triggerAccessoriesFinishCallback(this);
    }


    @SuppressWarnings("unused")
    public BaseNetRequest getFailedRequest() {
        return mFailedRequest;
    }

    public interface OnBatchRequestCallback {

        void onSuccess(BatchRequest request);

        void onFail(BatchRequest request);
    }

    public ArrayList<BaseNetRequest> getRequestList() {
        return mRequestList;
    }


    public <L> void installAccessory(RequestAccessory<L> accessory) {
        getAccessoryManager().installAccessory( accessory );
    }

//    public <R> R getAccessory(Class<R> clazz) {
//        return getAccessoryManager().getAccessory( clazz );
//    }

    public AccessoryManager<BatchRequest> getAccessoryManager() {
        return mAccessoryManager;
    }

    public Context getContext() {
        return mContext;
    }

}
