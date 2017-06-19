package com.sina.libcomponent.lcsnetwork.net.utils;

import java.util.HashMap;

/**
 * Created by xianting on 2017/5/18.
 * Description:
 */

public class LittleUtils {

    public static HashMap<String,String> createMap(String key, String value){
        HashMap<String,String> hashMap=new HashMap<>(  );
        hashMap.put( key,value );
        return hashMap;
    }
}
