package com.sina.libcomponent.lcsnetwork.net.plugins;

import android.text.TextUtils;

import com.sina.libcomponent.lcsnetwork.net.core.NetRequest;
import com.sina.libcomponent.lcsnetwork.net.utils.CacheUtil;

import java.util.Map;

/**
 * Created by xianting on 17/1/18.
 * Description:
 */

public class CacheAccessory implements RequestAccessory<NetRequest> {

    boolean  mIsCache=true;

    private String mCacheFileName;


    private boolean isCahchedResult;

    public CacheAccessory(String cacheFileName) {
        mCacheFileName = cacheFileName;
    }

    @Override
    public boolean onRequestStart(NetRequest request) {
        isCahchedResult= false;
        if (!mIsCache) {
            return false;
        }
        String cacheFileName=getCacheFileName( request );
        String cacheObject = (String) CacheUtil.readObject( request.getContext(), cacheFileName );
        if (cacheObject != null) {
            isCahchedResult=true;
            request.setResponseString( cacheObject );
            return true;
        }
        return false;
    }

    @Override
    public boolean onRequestResponse(NetRequest request) {
        return false;
    }

    @Override
    public void onRequestResponseHandledFinished(NetRequest request) {
        // 等正常加载数据后，再缓存
        if (mIsCache){
            String cacheFileName=getCacheFileName(request);
            CacheUtil.saveObject( request.getContext(), request.getResponseString(), cacheFileName );
        }
    }

    @Override
    public void onRequestFailed(NetRequest request) {

    }

    @Override
    public void onRequestCancelled(NetRequest request) {

    }

    @Override
    public void onRequestFinish(NetRequest request) {

    }

    @Override
    public int getPriority() {
        return 0;
    }

    private String getCacheFileName(NetRequest request) {
        if (!TextUtils.isEmpty(mCacheFileName)){
            return CacheUtil.md5(mCacheFileName);
        }
        String requestUrl = request.requestUrl();
        Map<String, String> params = request.requestParams();
        String requestInfo = "Method:" + request.requestMethod() + "HOST:" + "Url:" + requestUrl;
        String cacheFileName = CacheUtil.md5( requestInfo );
        return cacheFileName;
    }

    public void setCache(boolean cache) {
        mIsCache = cache;
    }

    public boolean isCahchedResult() {
        return isCahchedResult;
    }

}
