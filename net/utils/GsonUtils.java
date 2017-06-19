package com.sina.libcomponent.lcsnetwork.net.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * Created by xianting on 2017/5/10.
 * Description:
 */

public class GsonUtils {

    @SuppressWarnings( "unchecked" )
    public  static <T> T parserJson2Object( String content, Type listType) {
        try {
            Log.d("JSON_CONTENT",content);
            return (T)parserJson2ObjectUnsafe( content, listType );
        } catch (Exception e) {
            Try.printStackTrace(e);
        }

        return null;
    }

    private static <T> T parserJson2ObjectUnsafe(String content, Type listType) {
        Gson gson = new Gson();
        JsonReader jreader = new JsonReader( new StringReader( content ) );
        jreader.setLenient( true );
        return gson.fromJson( jreader, listType );
    }


    @SuppressWarnings( "unchecked" )
    public static <T> T parserMap2Object(Object src, Type listType) {
        try {
            return  (T)parserMap2ObjectUnsafe( src, listType );
        } catch (Exception e) {
            Try.printStackTrace(e);
        }

        return null;
    }

    public static String toJson(Object src) {
        Gson gson=new Gson();
        String gsonString= gson.toJson(src);
        return gsonString;
    }

    private static  <T> T parserMap2ObjectUnsafe(Object src, Type listType) {
        if (src==null||listType==null){
            return null;
        }
        Gson gson = new Gson();
        Gson gson1=new Gson();
        String json= gson1.toJson( src );
        JsonReader jreader = new JsonReader( new StringReader( json) );
        jreader.setLenient( true );
        return gson.fromJson( jreader, listType );
    }

}
