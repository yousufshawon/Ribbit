package com.yousuf.shawon.ribbit.ribbit.utility;

/**
 * Created by user on 1/20/2016.
 */
public class Log {

    private static boolean isLogVisible = true;

    private String TAG = getClass().getSimpleName();


    public void setLogVisible(boolean logVisible) {
        isLogVisible = logVisible;
    }


    public static void d(String tag, String message){
        if ( isLogVisible )
            android.util.Log.d(tag, message);
    }


    public static void e(String tag, String message){
        if ( isLogVisible )
            android.util.Log.e(tag, message);
    }

    public static void w(String tag, String message){
        if ( isLogVisible )
            android.util.Log.w(tag, message);
    }

    public static void i(String tag, String message){
        if ( isLogVisible )
            android.util.Log.i(tag, message);
    }

    public static void v(String tag, String message){
        if ( isLogVisible )
            android.util.Log.v(tag, message);
    }

    public static void wtf(String tag, String message){
        if ( isLogVisible )
            android.util.Log.wtf(tag, message);
    }


}
