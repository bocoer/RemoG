package com.resc.remgauge;

import java.util.Calendar;

import android.os.AsyncTask;

public class SysUtils {

	static SysUtils sysUtil = null;
	static boolean constructed = false;
	static int msToSleep = 0;
	
	public static void makeIns () {
		if ( !constructed ) {
			sysUtil = new SysUtils();
		}
		constructed = true;
	}
	
	public class Sleeper extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(msToSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static int getSeconds() {
		
		makeIns();
		
		return Calendar.getInstance().get(Calendar.SECOND);
	}
	
	public static void sleepMs( int  ms ) {
		
		makeIns();
		msToSleep = ms;
		Sleeper s = (Sleeper) sysUtil.new Sleeper().execute();
		s.doInBackground((Object) null);
	}
	
}
