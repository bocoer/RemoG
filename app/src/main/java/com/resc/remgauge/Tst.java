package com.resc.remgauge;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rob on 6/28/15.
 */
public class Tst {

    static public void showToast(String str, Context context ) {

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
    static public void showLong(String str, Context context ) {

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
    static public void showShort(String str, Context context ) {

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
    static public void showN(String str, Context context , int n) {

        Toast toast = Toast.makeText(context, str, n);
        toast.show();
    }
}
