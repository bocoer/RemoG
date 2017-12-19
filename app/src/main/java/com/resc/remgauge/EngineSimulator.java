package com.resc.remgauge;



//
// Simulates an engine 'session' .
//  An engine 'session' is a time frame from when the 
//  key to the car was turned on.  The car warms up, 
//  starts driving etc.

public class EngineSimulator {

	int sessionLengthMinutes = 0;
	int sessionStart = 0;
	int sessionLengthSeconds = 0;
    boolean running = false;
    int timeMinutes;
    int timeSeconds;
    int ot;
    int rpm;

	public EngineSimulator() {
		
	}
	
	public void start() {

        running = true;
        createSessionLength();
	}
	
	public void stop() {
		running = false;
	}

    void updateTime() {
        timeSeconds =  SysUtils.getSeconds() - sessionStart;
        timeMinutes = timeSeconds * 60;
    }

    int getOt () {

        updateTime();

        if ( timeMinutes < 5 ) {
            ot = 140 + (timeMinutes * 7) + MathUtils.randInt(0,3);
        }
        else {
            ot = 200 * (rpm / 100) + MathUtils.randInt(0,8);
        }
        return ot;
    }

    int getOilPsi () {

        updateTime();
        int psi = 0;
        if ( timeMinutes < 5 ) {
            psi = 60 - timeMinutes - MathUtils.randInt(0,4);
        }
        else {
            psi = 20 + MathUtils.randInt(0,3);
        }
        return psi;
    }

    float volt = 11.2f;
    float targetVolt = 12.0f;
    float getVolt () {

        if ( volt < targetVolt ) {
            volt += 0.1f;
            return volt;
        }
        if ( volt > targetVolt ) {
            volt -= 0.1f;
            return volt;
        }
        float v = (float) MathUtils.randInt(0,20) / 10.0f;
        volt = 11.2f + v;
        targetVolt = volt;
        getVolt();
        return 0.0f;
    }

    int getRpm () {

        return 2400;
    }
	public void createSessionLength() {
	
		//
		// Create a session of 2 to 150 minutes
		sessionLengthMinutes = MathUtils.randInt(2,150); 
		sessionLengthSeconds = sessionLengthMinutes * 60;
		//
		// We record how long the engine has been running in seconds
		//
		sessionStart = SysUtils.getSeconds();
		
	}
	
}
