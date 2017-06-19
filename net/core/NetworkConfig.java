package com.sina.libcomponent.lcsnetwork.net.core;

/**
 * Created by xianting on 17/1/3.
 */

public class NetworkConfig {
    private static final NetworkConfig instance = new NetworkConfig();

    private NetworkConfig(){

    }

    public static NetworkConfig getInstance(){
        return instance;
    }



}
