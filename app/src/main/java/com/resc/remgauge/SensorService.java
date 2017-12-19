package com.resc.remgauge;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class SensorService extends Service {

	String TG = "SensorService";
	SensorService ssInstance;
	LocationManager lm;
	Toast msg;
	Location last_loc;
	Float trip_dist = (float) 0.0;
	Consumer cons = null;
	boolean doer = false;
	
	@Override
	public void onDestroy() {
		Log.v(TG, "onDestroy()");
		stopUpdates();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d(TG,"onStartCommand");
		//
		// Set an instance holder in case its needed by a subclass.
		ssInstance = this;
		
		cons = new Consumer();
		// Do stuff.
		startUpdates();
		
	    return Service.START_NOT_STICKY;
	    //return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void stopUpdates() {
	
		lm.removeUpdates(locationListener);
		Log.v(TG, "stopUpdates()");
		
	}
	
	private void startUpdates() {
	   
	    lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	    last_loc = null;
	    request_updates();
	}
	    
	public void request_updates() {
	    if ( lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
	    	Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
	    	Log.v(TG, "GPS Enabled: OK");
	        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, locationListener);
	        
	    }
	    else {
	     Toast.makeText(this, "GPS is Disabled in your device", Toast.LENGTH_SHORT).show();	
	     	Log.v(TG,"GPS Disabled");
	    }
	   
	}

	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			new_loc(location);
		}

		public void onProviderDisabled(String arg0) {
			//TODO Auto-generated method stub
			Toast.makeText(ssInstance, "providerDisabled", Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			//TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			//TODO Auto-generated method stub
			Log.v(TG, "Status changed");
		}
	};

	public void new_loc(Location loc) {

		float rawSpeed = loc.getSpeed();
		float speed_now = (rawSpeed * 60.0f * 60.0f)/1601.0f;

		if (speed_now > 0) {
			// Getting spurious values when not moving
			// Probably accuracy related
			if (last_loc != null) {
				trip_dist += loc.distanceTo(last_loc);

			}
	 
			last_loc = loc;
		}
		if (trip_dist > 9999) {
			trip_dist = (float) 0.0;
		}
		
		float odom_miles =  trip_dist/1601.0f;
		String speed = String.format("%f", speed_now);
		String dist = String.format("%f", odom_miles);

		String infoString = "Speed :" + speed + " Dist: " + dist + "RAW: " + rawSpeed;

		if ( cons != null ) {
			cons.setDist(odom_miles);
			cons.setMph(speed_now);
			cons.setStringInfo(infoString);
		}
	}
	//ourLocationList = locationListener; 
}
