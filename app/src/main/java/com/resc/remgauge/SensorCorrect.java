package com.resc.remgauge;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rob on 7/24/15.
 */
public class SensorCorrect {

    public LinkedHashMap <Float,Float> luMap;
    static float [] xRes;
    static float [] yTmp;

    public SensorCorrect() {

        Log.v("SENSCOR" ,"SENSOR Correct newed");
        luMap = makeLu();
        int i = 0;
        int s = luMap.size();
        xRes = new float[s];
        yTmp = new float[s];

        for(Map.Entry<Float, Float> entry: luMap.entrySet())  {
            Float x = entry.getKey();
            Float y = entry.getValue();
            xRes[i] = x;
            yTmp[i] = y;
            i++;
            //Tab tab = entry.getValue();
        }

        Log.v("SENSCOR","FINISHED" + xRes.length);
    }

    public float convert(float v ) {
        int ind = findIndex(v);
        if (ind > 0 )
            return interpVal(ind,v);
        else;
        return -2.3f;
    }

    public int findIndex( float val ) {

        for ( int i = 0; i < xRes.length - 1; i++ ) {
            if ( val > xRes[i] && val < xRes[i+1] ) {
               return i;
            }
        }

        return -1;
    }

    public float interpVal( int index, float v ) {

        float xl = xRes[index];
        float xh = xRes[index + 1];
        float yl = yTmp[index];
        float yh = yTmp[index + 1];
        float xd = xh - xl;
        float yd = 5.0f;
        float degPerOhm = xd / yd;
        float xoff = v - xl;
        float deg = yl - (xoff/degPerOhm);
        return deg;
    }
    public LinkedHashMap makeLu() {
        LinkedHashMap<Float, Float> lu = new LinkedHashMap<Float, Float>();
        lu.put(10.24f,180.0f);
        lu.put(11.25f,175.0f);
        lu.put(12.38f,170.0f);
        lu.put(13.66f,165.0f);
        lu.put(15.11f,160.0f);
        lu.put(16.74f,155.0f);
        lu.put(18.59f,150.0f);
        lu.put(20.66f,145.0f);
        lu.put(23.00f,140.0f);
        lu.put(25.70f,135.0f);
        lu.put(28.81f,130.0f);
        lu.put(32.38f,125.0f);
        lu.put(36.51f,120.0f);
        lu.put(41.42f,115.0f);
        lu.put(47.24f,110.0f);
        lu.put(54.01f,105.0f);
        lu.put(61.92f,100.0f);
        lu.put(71.44f,95.0f);
        lu.put(82.96f,90.0f);
        lu.put(96.40f,85.0f);
        lu.put(112.08f,80.0f);
        lu.put(131.38f,75.0f);
        lu.put(155.29f,70.0f);
        lu.put(184.72f,65.0f);
        lu.put(221.17f,60.0f);
        lu.put(266.19f,55.0f);
        lu.put(322.17f,50.0f);
        lu.put(392.57f,45.0f);
        lu.put(481.53f,40.0f);
        lu.put(594.90f,35.0f);
        lu.put(739.98f,30.0f);
        lu.put(926.71f,25.0f);
        lu.put(1168.64f,20.0f);
        lu.put(1486.65f,15.0f);
        lu.put(1905.87f,10.0f);
        lu.put(2473.60f,5.0f);
        lu.put(3240.18f,0f);
        lu.put(4284.03f,-5.0f);
        lu.put(5720.88f,-10.0f);
        lu.put(7721.35f,-15.0f);
        lu.put(10540.68f,-20.0f);
        lu.put(14127.68f,-25.0f);
        lu.put(19149.20f,-30.0f);
        lu.put(26284.63f,-35.0f);
        lu.put(36563.56f,40.0f);


        return lu;
    }
}
