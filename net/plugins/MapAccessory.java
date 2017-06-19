package com.sina.libcomponent.lcsnetwork.net.plugins;


import com.sina.libcomponent.lcsnetwork.net.core.NetRequest;
import com.sina.libcomponent.lcsnetwork.net.utils.LXTDeviceManger;

import java.util.ArrayList;

/**
 * Created by xianting on 17/1/7.
 * Description:
 */

public class MapAccessory implements RequestAccessory<NetRequest> {

    public interface OnMap<T, K> {
        K map(NetRequest<T> request);
    }

    private ArrayList<OnMap> mMapListeners = new ArrayList<>();

    public <T, K> void map(OnMap<T, K> map) {
        mMapListeners.add( map );
    }

    @Override
    public boolean onRequestStart(NetRequest request) {
             return false;
    }

    @Override
    public boolean onRequestResponse(NetRequest request) {
        for (OnMap listener : mMapListeners) {
            if (LXTDeviceManger.isLXTDevice()){
                request.mTempObject = listener.map( request );
                return false;
            }
            try {
                request.mTempObject = listener.map( request );
            } catch (Exception e) {
                e.printStackTrace();
                request.mTempObject=null;
            }
        }
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
        return RequestAccessory.RequestAccessoryPriority.ACCESSORY_PRIORITY_LOW;
    }
}
