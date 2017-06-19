package com.sina.libcomponent.lcsnetwork.net.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sina.libcomponent.lcsnetwork.net.plugins.AccessoryManager;
import com.sina.libcomponent.lcsnetwork.net.plugins.CacheAccessory;
import com.sina.libcomponent.lcsnetwork.net.plugins.MapAccessory;
import com.sina.libcomponent.lcsnetwork.net.plugins.RequestAccessory;
import com.sina.libcomponent.lcsnetwork.net.utils.LittleUtils;
import com.sina.libcomponent.lcsnetwork.net.utils.NetWorkUtils;

import java.util.Map;

/**
 * Created by xianting on 17/1/3.
 * Description: 网络请求对象
 */

public class NetRequest<T> extends BaseNetRequest<T> {
    // 请求地址
    protected String mUrl;
    // 请求参数
    protected Map<String, String> mParams;
    // 最终结果
    public Object mResponseObject;
    // 临时结果
    public Object mTempObject;
    // 插件管理器
    private AccessoryManager<BaseNetRequest> mAccessoryManager;

    public NetRequest(Context context, String url, Map<String, String> params) {
        super( context );
        mUrl = url;
        mParams = params;
        mAccessoryManager = new AccessoryManager<>();
    }

    public NetRequest(Context context, String url, String key, String value) {
        this( context, url, LittleUtils.createMap( key, value ) );
    }


    public void start() {
        NetworkAgent.getInstance().addRequest( this );
    }

    public void cancel() {
        NetworkAgent.getInstance().cancelRequest( this );
    }



    @SuppressWarnings("unchecked")
    public T getDataObject() {
        return (T) mTempObject;
    }

    public Object getTempObject() {
        return mTempObject;
    }

    public void setTempObject(Object tempObject) {
        mTempObject = tempObject;
    }

    @Override
    public String requestUrl() {
        return mUrl;
    }

    @Override
    public Map<String, String> requestParams() {
        return mParams;
    }

    public interface OnNetCallback<T> extends BaseNetRequest.OnNetCallback<NetRequest<T>> {

    }


    protected  <L> void installAccessory(RequestAccessory<L> accessory) {
        getAccessoryManager().installAccessory( accessory );
    }

    protected <R> R getAccessory(Class<R> clazz) {
        return getAccessoryManager().getAccessory( clazz );
    }

    @NonNull
    protected AccessoryManager<BaseNetRequest> getAccessoryManager() {
        return mAccessoryManager;
    }

    /**************************************************一些插件--我是可爱的分割线--lxt************************************/

    /**
     *   对数据进行中转
     * @param map  接口调用成功后会回调
     * @param <L>   原始数据返回类型
     * @param <K>   处理后的数据类型
     * @return  处理后的数据
     */
    public <L, K> NetRequest<T> map(MapAccessory.OnMap<L, K> map) {
        MapAccessory mapAccessory = getAccessoryManager().getAccessory( MapAccessory.class );
        if (mapAccessory == null) {
            mapAccessory = new MapAccessory();
            installAccessory( mapAccessory );
        }
        mapAccessory.map( map );
        return this;
    }

    public NetRequest<T> setCacheResult(boolean isLoadCacheOnlyNetworkUnavailable, String cacheFileName) {
        if (NetWorkUtils.isNetworkConnected( getContext() ) && isLoadCacheOnlyNetworkUnavailable) {
            return this;
        }
        CacheAccessory cacheAccessory = getAccessoryManager().getAccessory( CacheAccessory.class );
        if (cacheAccessory == null) {
            cacheAccessory = new CacheAccessory(cacheFileName);
            installAccessory( cacheAccessory );
        }
        return  this;
    }

    public NetRequest<T> setCacheResult(boolean isLoadCacheOnlyNetworkUnavailable) {
        return setCacheResult( isLoadCacheOnlyNetworkUnavailable, null );
    }

    public void toggleCacheResult(boolean isCache) {
        CacheAccessory cacheAccessory = getAccessoryManager().getAccessory( CacheAccessory.class );
        if (cacheAccessory != null) {
            cacheAccessory.setCache( isCache );
        }
    }

    /**************************************************一些插件--我是可爱的分割线--lxt************************************/
}
