package com.sina.libcomponent.lcsnetwork.net.utils;

import android.text.TextUtils;

/**
 * Created by xianting on 17/1/11.
 * Description:
 */
public class LXTDeviceManger {
    private static LXTDeviceManger ourInstance = new LXTDeviceManger();

    public static LXTDeviceManger getInstance() {
        return ourInstance;
    }

    public static boolean isLXTDevice() {
        try {
            String serialNum = android.os.Build.SERIAL;
            boolean isLxtDevice= !TextUtils.isEmpty(serialNum) && serialNum.equals("0302f61708ea7f78");
            boolean isHuaweiDevice= !TextUtils.isEmpty(serialNum) && serialNum.equals("M3LDU15601003784");
            return isLxtDevice||isHuaweiDevice;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
