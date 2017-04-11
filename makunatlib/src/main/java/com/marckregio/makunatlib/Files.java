package com.marckregio.makunatlib;

import android.os.Environment;

import java.io.File;

/**
 * Created by makregio on 11/04/2017.
 */

public abstract class Files {

    private static String OUTPUT = "";

    private static void checkAndMkdir(String dirName) {
        File dirFile = new File(dirName);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dirFile.mkdirs();
        }
    }

    public static String getMainDir() {
        String retVal = Environment.getExternalStorageDirectory().getAbsolutePath();
        retVal = retVal + "/" + OUTPUT+ "/";
        checkAndMkdir(retVal);
        return retVal;
    }

    public static void cleanDirectory(){
        for(File file: new File(getMainDir()).listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    public static void setOUTPUT(String folder){
        OUTPUT = folder;
    }

}
