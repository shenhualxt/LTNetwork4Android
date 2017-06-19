package com.sina.libcomponent.lcsnetwork.net.core;

import java.util.Collections;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by XianTing on 17/1/7.
 * Description:  错误处理
 */
public class NetError extends Exception {
    private final NetworkResponse networkResponse;
    public Object extras;
    private int mErrorCode = -1;
    private String mErrorMessage;

    public NetError(int errorCode, String errorMessage) {
        networkResponse = null;
        this.mErrorCode = errorCode;
        this.mErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    @SuppressWarnings("unused")
    public int getErrorCode() {
        return mErrorCode;
    }

    public NetError(NetworkResponse response) {
        networkResponse = response;
        mErrorCode = response.statusCode;
    }

    public NetError(String exceptionMessage) {
        super(exceptionMessage);
        networkResponse = null;
        mErrorMessage = exceptionMessage;
    }

    public NetError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
        mErrorMessage = exceptionMessage;
    }

    public NetError(Throwable cause) {
        super(cause);
        networkResponse = null;
    }

    @SuppressWarnings("unused")
    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

    public static class NetworkResponse {
        /**
         * Creates a new network response.
         *
         * @param statusCode    the HTTP status code
         * @param data          Response body
         * @param headers       Headers returned with this response, or null for none
         * @param notModified   True if the server returned a 304 and the data was already in cache
         * @param networkTimeMs Round-trip network time to receive network response
         */
         NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                               boolean notModified, long networkTimeMs) {
            this.statusCode = statusCode;
            this.data = data;
            this.headers = headers;
            this.notModified = notModified;
            this.networkTimeMs = networkTimeMs;
        }

        public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                               boolean notModified) {
            this(statusCode, data, headers, notModified, 0);
        }

        @SuppressWarnings("unused")
        public NetworkResponse(byte[] data) {
            this(SC_OK, data, Collections.<String, String>emptyMap(), false, 0);
        }

        @SuppressWarnings("unused")
        public NetworkResponse(byte[] data, Map<String, String> headers) {
            this(SC_OK, data, headers, false, 0);
        }

        /**
         * The HTTP status code.
         */
        final int statusCode;

        /**
         * Raw data from this response.
         */
        public final byte[] data;

        /**
         * Response headers.
         */
        public final Map<String, String> headers;

        /**
         * True if the server returned a 304 (Not Modified).
         */
        final boolean notModified;

        /**
         * Network roundtrip time in milliseconds.
         */
        final long networkTimeMs;
    }
}
