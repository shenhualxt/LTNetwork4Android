package com.sina.libcomponent.lcsnetwork.net.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xianting on 17/1/3.
 */

public interface RequestAccessory<T> {

    @Retention(RetentionPolicy.SOURCE)
    public @interface  RequestAccessoryPriority{
        int ACCESSORY_PRIORITY_HIGH = 1000;
        int ACCESSORY_PRIORITY_MIDDLE = 0;
        int ACCESSORY_PRIORITY_LOW = -1000;
        int ACCESSORY_PRIORITY_DEFAULT = ACCESSORY_PRIORITY_MIDDLE;
    }

    boolean onRequestStart(T request);

    boolean onRequestResponse(T request);

    void onRequestResponseHandledFinished(T request);

    void onRequestFailed(T request);

    void onRequestCancelled(T request);

    void onRequestFinish(T request);

    @RequestAccessoryPriority int getPriority();
}
