package com.sina.libcomponent.lcsnetwork.net.utils;

import android.util.Log;

/**
 * Created by xianting on 17/2/22.
 * Description: 在测试换件下 try catch 不生效，方便检测出bug
 */

public  class Try {

    public static void printStackTrace(Exception e){
           if (LXTDeviceManger.isLXTDevice()){
               e.printStackTrace();
               Log.e( "lxt_tag", e.toString());
               throw new Error(e.toString());
           } else {
              e.printStackTrace();
           }
    }

}
