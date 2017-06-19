package com.sina.libcomponent.lcsnetwork.net.plugins;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xianting on 17/1/8.
 * Description:  插件管理类
 */

public class AccessoryManager<T> {

    private ArrayList<RequestAccessory> mRequestAccessories;
    private HashMap<Class<?>,RequestAccessory> mRequestAccessoryMap;

    public <L> void installAccessory(RequestAccessory<L> accessory) {
        RequestAccessory checkAccessory=getAccessory( accessory.getClass() );
        if (checkAccessory!=null){
            return;
        }
        if (mRequestAccessoryMap==null){
            mRequestAccessoryMap=new HashMap<>(  );
        }
        mRequestAccessoryMap.put( accessory.getClass(),accessory );

        if (mRequestAccessories == null) {
            mRequestAccessories = new ArrayList<>();
        }
        //  根据优先级添加
        if (mRequestAccessories.size() == 0) {
            mRequestAccessories.add( accessory );
            return;
        }

        int locateIndex = mRequestAccessories.size()-1;
        for (int i = 0; i < mRequestAccessories.size(); i++) {
            boolean isLowPriority = mRequestAccessories.get( i ).getPriority() > accessory.getPriority();
            if (!isLowPriority) {
                locateIndex = i;
                break;
            }
        }
        boolean isLastOne = locateIndex == mRequestAccessories.size() - 1;
        if (isLastOne) {
            mRequestAccessories.add( accessory );
        } else {
            mRequestAccessories.add( locateIndex + 1, accessory );
        }
    }

    /**
     * RequestAccessory
     */
    public boolean triggerAccessoriesStartCallback(T request) {
        boolean isIntercept = false;
        for (RequestAccessory accessory : mRequestAccessories) {
            boolean isInterceptOnStartCallback= accessory.onRequestStart( request );
            if (isInterceptOnStartCallback) {
                isIntercept = true;
            }
        }
        return isIntercept;
    }

    public void triggerAccessoriesFinishCallback(T request) {
        for (RequestAccessory accessory : mRequestAccessories) {
            accessory.onRequestFinish( request );
        }
    }

    public boolean triggerAccessoriesResponseCallback(T request) {
        boolean isIntercept = false;
        for (RequestAccessory accessory : mRequestAccessories) {
            boolean isInterceptOnSuccessCall = accessory.onRequestResponse( request );
            if (isInterceptOnSuccessCall) {
                isIntercept = true;
            }
        }
        return isIntercept;
    }

    public void triggerAccessoriesResponseHandledFinishedCallback(T request) {
        for (RequestAccessory accessory : mRequestAccessories) {
            accessory.onRequestResponseHandledFinished( request );
        }
    }

    public void triggerAccessoriesResponseCancelledCallback(T request) {
        for (RequestAccessory accessory : mRequestAccessories) {
            accessory.onRequestCancelled( request );
        }
    }

    public void triggerAccessoriesFailedCallback(T request) {
        for (RequestAccessory accessory : mRequestAccessories) {
            accessory.onRequestFailed( request );
        }
    }


    @SuppressWarnings( "unchecked" )
    public <R> R getAccessory(Class<R> clazz) {
        if (mRequestAccessoryMap==null){
            return null;
        }
        return (R)mRequestAccessoryMap.get( clazz );
    }
}
