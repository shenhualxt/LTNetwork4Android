package com.sina.libcomponent.lcsnetwork.net.core;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.sina.libcomponent.volley.AuthFailureError;
import com.sina.libcomponent.volley.DefaultRetryPolicy;
import com.sina.libcomponent.volley.NetworkResponse;
import com.sina.libcomponent.volley.NoConnectionError;
import com.sina.libcomponent.volley.Request;
import com.sina.libcomponent.volley.RequestQueue;
import com.sina.libcomponent.volley.Response;
import com.sina.libcomponent.volley.VolleyError;
import com.sina.libcomponent.volley.toolbox.HttpStack;
import com.sina.libcomponent.volley.toolbox.StringRequest;
import com.sina.libcomponent.volley.toolbox.Volley;

import java.util.Map;


/**
 * Created by XianTing on 17/1/3.
 * Description:  网络请求代理类
 */
public class NetworkAgent {

    private static final NetworkAgent instance = new NetworkAgent();
    // TODO: use config
    private NetworkConfig mNetworkConfig;
    private RequestQueue mRequestQueue;

    private NetworkAgent() {
        mNetworkConfig = NetworkConfig.getInstance();

    }

    public static NetworkAgent getInstance() {
        return instance;
    }


    private RequestQueue getRequestQueue(Context context) {
        return getRequestQueue( context, null );
    }

    private RequestQueue getRequestQueue(Context context, HttpStack httpStack) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue( context, httpStack );
        }
        return mRequestQueue;
    }

    @SuppressWarnings("unchecked")
    void addRequest(final NetRequest request) {
        boolean isIntercept = request.getAccessoryManager().triggerAccessoriesStartCallback( request );
        if (isIntercept) {
            boolean notIntercept = !(request.getAccessoryManager().triggerAccessoriesResponseCallback( request ));
            if (notIntercept && request.requestCallback() != null) {
                request.requestCallback().onSuccess( request );
                request.getAccessoryManager().triggerAccessoriesResponseHandledFinishedCallback( request );
                request.getAccessoryManager().triggerAccessoriesFinishCallback( request );
            }
        }
        String url = buildRequestUrl( request );
        final Map<String, String> params = request.requestParams();
        final Map<String, String> headers = request.requestHeader();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                request.setResponseString( response );
                // 插件是否拦截onSuccess 调用
                boolean notIntercept = !(request.getAccessoryManager().triggerAccessoriesResponseCallback( request ));
                if (notIntercept && request.requestCallback() != null) {
                    request.requestCallback().onSuccess( request );
                    request.getAccessoryManager().triggerAccessoriesResponseHandledFinishedCallback( request );
                    request.getAccessoryManager().triggerAccessoriesFinishCallback( request );
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetError netError = volleyError2NetError( error );
                triggerFailedEvent( netError, request );
            }
        };
        Request volleyRequest = new StringRequest( request.requestMethod(), url, listener, errorListener ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params == null) {
                    return super.getParams();
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return super.getHeaders();
                }
                return headers;
            }
        };
        volleyRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyRequest.setTag( request.getCancelTag() );
        getRequestQueue( request.getContext() ).add( volleyRequest );
    }

    @SuppressWarnings("unchecked")
    public static void triggerFailedEvent(NetError netError, NetRequest request) {
        request.setError( netError );
        if (request.requestCallback() != null) {
            request.requestCallback().onFail( request );
        }

        request.getAccessoryManager().triggerAccessoriesFailedCallback( request );
        request.getAccessoryManager().triggerAccessoriesFinishCallback( request );
    }

    private NetError volleyError2NetError(VolleyError error) {
        if (error == null) {
            return new NetError( "NetworkAgent:Volley error is null" );
        }
        try {
            NetworkResponse volleyResponse = error.networkResponse;
            if (volleyResponse == null) {
                if (error instanceof NoConnectionError) {
                    return new NetError( "您的网络连接异常" );
                }
                if (!TextUtils.isEmpty(  error.getMessage())) {
                    return new NetError( error.getMessage() ) ;
                }
                Log.e( getClass().getSimpleName(), "NetworkAgent:Volley Response is null,volley error is" + error.toString() );
                return new NetError( "您的网络连接异常" );
            }

            NetError.NetworkResponse netResponse = new NetError.NetworkResponse( volleyResponse.statusCode, volleyResponse.data, volleyResponse.headers, volleyResponse.notModified, volleyResponse.networkTimeMs );
            return new NetError( netResponse );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( getClass().getSimpleName(), "volley mError convert to net mError failed,volley mError was saved in extras.Failed reason:" + e.toString() );
            NetError netError = new NetError( "您的网络连接异常" );
            netError.extras = error;
            return netError;
        }
    }

    @SuppressWarnings("unchecked")
    public void cancelRequest(NetRequest request) {
        if (mRequestQueue != null && request.getCancelTag() != null) {
            mRequestQueue.cancelAll( request.getCancelTag() );
        }
        request.getAccessoryManager().triggerAccessoriesResponseCancelledCallback( request );
        request.getAccessoryManager().triggerAccessoriesFinishCallback( request );
    }


    private String buildRequestUrl(NetRequest request) {
        String detailUrl = request.requestUrl();
        // TODO:lxt add config
        if (request.requestMethod() == NetRequest.Method.GET) {
            Map<String, String> params = request.requestParams();
            if (params != null) {
                Uri.Builder builder = Uri.parse( detailUrl ).buildUpon();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.appendQueryParameter( entry.getKey(), entry.getValue() );
                }
                detailUrl = builder.toString();
            }
        }
        request.setDebugUrl( detailUrl );
        return detailUrl;
    }

}
