package com.sina.libcomponent.lcsnetwork.net.plugins;

import android.text.TextUtils;

import com.sina.libcomponent.lcsnetwork.net.core.NetRequest;
import com.sina.libcomponent.lcsnetwork.net.utils.GsonUtils;

import java.lang.reflect.Type;

/**
 * Created by xianting on 17/1/4.
 * Description: json 解析插件
 */

public class GsonAccessory implements RequestAccessory<NetRequest> {

    private Type mType;

    public GsonAccessory(Type type) {
        this.mType = type;
    }

    @Override
    public boolean onRequestStart(NetRequest request) {
        return false;
    }

    @Override
    public boolean onRequestResponse(NetRequest request) {
        String response = request.getResponseString();
        if (TextUtils.isEmpty( response )) {
            return false;
        }

        request.mResponseObject = GsonUtils.parserJson2Object( response, mType );
        return false;
    }

    @Override
    public void onRequestResponseHandledFinished(NetRequest request) {

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
        return RequestAccessory.RequestAccessoryPriority.ACCESSORY_PRIORITY_DEFAULT;
    }

}
