package com.sina.libcomponent.lcsnetwork.net.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xianting on 17/1/19.
 * Description:
 */

public class CacheUtil {
    public static void saveObject(final Context context, final Serializable ser, final String file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = context.openFileOutput( file, context.MODE_PRIVATE );
                    oos = new ObjectOutputStream( fos );
                    oos.writeObject( ser );
                    oos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        oos.close();
                    } catch (Exception e) {
                    }
                    try {
                        fos.close();
                    } catch (Exception e) {
                    }
                }
            }
        }).start();

    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     */
    public static Serializable readObject(Context context, String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput( file );
            ois = new ObjectInputStream( fis );
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static boolean isCacheDataFailure(Context context, String cachefile) {
        boolean failure = false;
        long cache_time = 24 * 60 * 60 * 1000;
        File data = context.getFileStreamPath( cachefile );
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > cache_time)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance( "MD5" );
            digest.update( s.getBytes() );
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append( Integer.toHexString( 0xFF & messageDigest[i] ) );
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
