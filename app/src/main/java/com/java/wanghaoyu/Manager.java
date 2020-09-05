package com.java.wanghaoyu;

import android.content.Context;

import java.io.IOException;

public class Manager {
    private static Manager managerInstance = null;

    public static synchronized Manager getInstance(Context context)
    {
        if(managerInstance != null) return managerInstance;
        try{
            managerInstance = new Manager(context);
        }  catch (IOException e){
            e.printStackTrace();
            throw new AssertionError();
        }
        return managerInstance;
    }

    private Manager(Context context) throws IOException
    {

    }
}
