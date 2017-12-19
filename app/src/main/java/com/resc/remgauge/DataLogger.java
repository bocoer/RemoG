package com.resc.remgauge;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rob on 8/4/2015.
 */
public class DataLogger {

    static long bytesWritten = 0;
    static float mbf = 0.0f;
    static RandomAccessFile logFile2 = null;
    //One binary gigabyte equals 1,073,741,824 bytes.
    static final double bytesPerGb = 1073741824.0f;

    static private double sdCardRoom() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdAvailSize = (double)stat.getAvailableBlocks()
                * (double)stat.getBlockSize();
        double gigaAvailable = sdAvailSize / bytesPerGb;
        return gigaAvailable;
    }

    static private String timeFormat2() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String test = sdf.format(cal.getTime());
        return test;
    }
    static private String timeFormat() {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return month+"/"+day+"/"+year+"_"+hour+":"+minutes+":"+seconds;
    }

    static public void closeLog() {
        try {
            logFile2.close();
            logFile2 = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public void appendLogMax(String txt) throws IOException {
       // RandomAccessFile logFile2 = null;
        try {
            if ( logFile2 == null ) {
                logFile2 = new RandomAccessFile(new File("/sdcard/yourFile.txt"), "rw");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if ( mbf > 48.0f  || mbf == 0) {
                logFile2.seek(0); // to the beginning
                bytesWritten = 0;
                mbf = 0.0f;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ts = timeFormat2() + txt + "\n";
        byte[] a = ts.getBytes();
        mbf +=  (float) a.length / 1.0e6;
        try {
            logFile2.write(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public void appendLog(String text)
    {
       // File traceFile = new File(((Context)this).getExternalFilesDir(null), "TraceFile.txt");
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(timeFormat2() + text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
