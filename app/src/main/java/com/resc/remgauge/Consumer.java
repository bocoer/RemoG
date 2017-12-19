package com.resc.remgauge;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.resc.remgauge.R.id.DigitalView;


//
// This consumer class has a number of methods to set data, 
//  it then can dispatch GUI events based on that data.
//
public class Consumer {
	
	public enum ViewType {
		TEXTVIEW,
		GAUGEVIEW,
		DIGITALVIEW,
        GAUGEVIEW_SINGLE
	}

    public enum GaugeType {
        MPH,
        RPM,
        OIL_TEMP,
        OIL_PSI,
        CHT,
        ODOM,
        VOLT,
        AFT,
        AMBIENT,
        MAP
    }


	TestView mTestView = null;
	TextView mTextViewMph = null;
	TextView mTextViewDist = null;
	TextView mTextViewRpm = null;
	TextView mTextViewOt = null;
	TextView mTextViewCht = null;
	TextView mTextViewOp = null;
	TextView mTextViewAfr = null;
	TextView mTextViewAmbient = null;
	TextView mTextViewMesg = null;
	TextView mTextViewAnalogChan0 = null;
	static GaugeBuilder gbMph = null;
	static GaugeBuilder gbOt = null;

	GaugeBuilder myGb = null;

	DigitalView dgView = null;
	View gbView = null;
	View txtView = null;

    //
    //
	static float 	valueOt = 0;
	static int 		valueCht = 0;
	static float 	valueMph = 0;
	static float 	valueDist = 0.0f;
	static int 		valueRpm = 0;
	static int 		valueOp = 0;
	static float 	valueAmbient = 0;
	static float 	valueVlt;
	static int 		valueAfr = -1;
	static int analogChans[] = new int[8];
    //
    //

	static String valueInfoString = "+++";
	float factor = 0.2f;
	int mpaintOperation = 0;
	static boolean running = false;
	int consInstance = 0;
	ViewType mViewType;
	GaugeType mGaugeType;

	private String tag() {
		return "CONSUMER" + consInstance;
	}
	
	public Consumer( View view ) {
		
		mTextViewOt = (TextView) view.findViewById(R.id.textViewOt);
		mTextViewCht = (TextView) view.findViewById(R.id.textViewCht);
		mTextViewDist = (TextView) view.findViewById(R.id.textViewDist);
		mTextViewOp  = (TextView) view.findViewById(R.id.textViewOp);
		mTextViewRpm = (TextView) view.findViewById(R.id.textViewRpm);
		mTextViewMph = (TextView) view.findViewById(R.id.textViewMph);
		mTextViewAfr = (TextView) view.findViewById(R.id.textViewAfr);
		mTextViewAmbient = (TextView) view.findViewById(R.id.textViewAmbient);
		mTextViewMesg = (TextView ) view.findViewById(R.id.textViewMesg);
		mTextViewAnalogChan0 = (TextView) view.findViewById(R.id.analogChan0);
//		myGb = (GaugeBuilder) view.getRootView();
		txtView = view.getRootView();
		consInstance = 1;
   		for ( int i = 0; i < 8; i++ ) {
			analogChans[i] = 0;
		}
		Log.v(tag(), "ctor 1");
		mViewType = ViewType.TEXTVIEW;
	}

	public Consumer ( View view, String ut, String lt, String unt, int minv, int maxv, int centVal, GaugeType gt) {

		myGb = (GaugeBuilder) view.findViewById(R.id.analogGauge);
		myGb.setUnitTitle(unt);
		myGb.setUpperTitle(ut);
		myGb.setLowerTitle(lt);
		//gbView = rootView;
		myGb.setScaleMinValue(minv);
		myGb.setScaleMaxValue(maxv);
		myGb.setTotalNotches((maxv - minv));
		myGb.setIncrementPerLargeNotch((maxv-minv)/10);
		myGb.setIncrementPerSmallNotch(1);
		myGb.setScaleCenterValue(centVal);
		myGb.setDegreesPerNotch();

		dgView = (DigitalView) view.findViewById(R.id.DigitalView);
		mGaugeType = gt;

		if ( gt == GaugeType.MPH) {
			dgView.setElementRow("OT F", 0);
			dgView.setElementRow("Rpm", 1);
			dgView.setElementRow("Mph", 2);
			/*
			positionMap.put("Mph", 0);
			positionMap.put("Rpm",1);
			positionMap.put("OT F", 2);
			positionMap.put("Oil PSI", 3);
			positionMap.put("CHT F", 4);
			positionMap.put("Volt", 5);
			positionMap.put("Odom", 6);
			positionMap.put("AFR", 7);
			positionMap.put("Amb", 8);
			*/
		}
		Log.v(tag(), "ctor dddd2");
	}

	public static float getMph() {
		return valueMph;
	}
	public Consumer( View view, ViewType vt) {

		//mTextViewOt = (TextView) view.findViewById(R.id.textView3);
		//mTextViewCht = (TextView) view.findViewById(R.id.textView2);
		//mTextViewMph = (TextView) view.findViewById(R.id.textViewMph);
		gbMph = (GaugeBuilder) view.findViewById(R.id.analogGauge1);
////		gbMph.setUnitTitle("MPH");
		gbOt =  (GaugeBuilder) view.findViewById(R.id.analogGauge2);

        if ( gbOt == null) { Log.v(tag(), "gbOt null"); return;}

        if ( gbMph == null) { Log.v(tag(), "gbMph null"); return;}

		//gbOt.setTotalNotches(200);
		gbOt.setScaleMinValue(0);
		gbOt.setScaleMaxValue(60);
		gbOt.setTotalNotches(100);
		gbOt.setIncrementPerLargeNotch(10);
		gbOt.setIncrementPerSmallNotch(1);
		gbOt.setScaleCenterValue(30);
		gbOt.setDegreesPerNotch();
		gbView = view;
		gbOt.setUnitTitle("OT");
		consInstance = 2;
		Log.v(tag(), "ctor 2");
        gbOt.setValue(33.0f);
        gbOt.postInvalidate();
		mViewType = ViewType.GAUGEVIEW;
	}
	
	public Consumer( TestView view, int paintOperation ) 
	{
		mpaintOperation = paintOperation;
		//view.getDisplay();
		//view.setXfact(10);
		
		//view.setC(20);
		
		mTestView = view;
		consInstance = 3;
		Log.v("Consumer", " ctor3 constuctor sets paint operation");
		
	}
	
	public Consumer() {
		consInstance = 4;
	}
    public Consumer(View rootView, ViewType vt, boolean doIt) {
        // TODO Auto-generated constructor stub
		if ( doIt ) {
			gbMph = (GaugeBuilder) rootView.findViewById(R.id.analogGauge);
			gbMph.setUnitTitle("MPH");
			gbMph.setUpperTitle("MPH");
			gbMph.setLowerTitle("");
			gbView = rootView;
			gbMph.setScaleMinValue(0);
			gbMph.setScaleMaxValue(100);
			gbMph.setTotalNotches(100);
			gbMph.setIncrementPerLargeNotch(10);
			gbMph.setIncrementPerSmallNotch(1);
			gbMph.setScaleCenterValue(50);
			gbMph.setDegreesPerNotch();
			consInstance = 5;
			dgView = (DigitalView) rootView.findViewById(R.id.DigitalView);
			Log.v(tag(), "ctor 2");
			mViewType = ViewType.GAUGEVIEW;
		} else {
			gbOt = (GaugeBuilder) rootView.findViewById(R.id.analogGauge);
			gbOt.setUnitTitle("F");
			gbOt.setUpperTitle("OtAA");
			gbOt.setLowerTitle("--");
			//gbView = rootView;
			gbOt.setScaleMinValue(150);
			gbOt.setScaleMaxValue(350);
			gbOt.setTotalNotches(200);
			gbOt.setIncrementPerLargeNotch(10);
			gbOt.setIncrementPerSmallNotch(1);
			gbOt.setScaleCenterValue(250);
			gbOt.setDegreesPerNotch();
			consInstance = 5;
			dgView = (DigitalView) rootView.findViewById(R.id.DigitalView);
			Log.v(tag(), "ctor 2");
			mViewType = ViewType.GAUGEVIEW;
		}
    }

    public Consumer(View rootView, ViewType vt, GaugeType gt ) {

        gbMph = (GaugeBuilder) rootView.findViewById(R.id.analogGauge);
        gbMph.setUnitTitle("F");
        gbMph.setUpperTitle("Ot");
        gbMph.setLowerTitle("--");
        gbView = rootView;
        gbMph.setScaleMinValue(150);
        gbMph.setScaleMaxValue(350);
        gbMph.setTotalNotches(200);
        gbMph.setIncrementPerLargeNotch(10);
        gbMph.setIncrementPerSmallNotch(1);
        gbMph.setScaleCenterValue(250);
        gbMph.setDegreesPerNotch();
        consInstance = 5;
        dgView = (DigitalView) rootView.findViewById(DigitalView);
        Log.v(tag(), "ctor 2");
        mViewType = ViewType.GAUGEVIEW;
    }

	public Consumer(View rootView, GaugeBuilder gb, ViewType gaugeview) {

		gbMph = (GaugeBuilder) rootView.findViewById(R.id.analogGauge1);
		gbMph.setUnitTitle("MPHddd");
		gbOt =  (GaugeBuilder) rootView.findViewById(R.id.analogGauge2);
		gbView = rootView;
		gbOt.setUnitTitle("OT");
		consInstance = 5;
		Log.v(tag(), "ctor 2");
		mViewType = ViewType.GAUGEVIEW;
	}

	public Consumer( View rootView, DigitalView dv ) {
		mViewType = ViewType.DIGITALVIEW;
		dgView = (DigitalView) rootView.findViewById(R.id.DigitalView2);
        Log.v(tag(),"ctor 4");
	}
	
	public void stop() {
		running = false;
	}
	public void setFactor( int f ) {
		factor = f;
	}
	public void setMph( float mph ) {
		valueMph = mph;
	}
	public void setDist( float d ) {
		valueDist = d;
	}
	public void setStringInfo ( String s ) {
		valueInfoString = s;
	}
	public void setCht( int headTemps ) {
		valueCht = headTemps;
	}
	public void setOt ( float temp ) {
		valueOt = temp;
	}
    public void setVlt ( float v ) { valueVlt = v; }
	public void setAfr ( int afr ) { valueAfr = afr;}
	public void setAmb ( float v ) { valueAmbient = v; }
	public void setAnalogChan( int v, int ind ) { analogChans[ind] = v;}

	//
	// Start consuming data and post messages to GUI widgets.
	//
	public void startProgress() {
		//

		running = true;
		Runnable runnable = new Runnable() {
	    	@Override
	    	public void run() {
	    
	    		UpdateTextTask setText = new UpdateTextTask();

	    		int iters = 0;
	    		while ( running ) {

	    			//valueCht = (MathUtils.randInt(1,100)) + 200;
	    			//valueOt = MathUtils.randInt(180, 240);
	    			//valueRpm = MathUtils.randInt(800, 3500);
	    			//valueOp = MathUtils.randInt(2, 60);
	    			//valueVlt = (MathUtils.randInt(110,140))/ 10.0f;
				//	valueVlt = valueAfr;
	    		//	valueMph = MathUtils.randInt(100,240);

	    			if ( dgView != null ) {
	    				dgView.setMph(valueMph);
	    				dgView.setOp(valueOp);
	    				dgView.setOt(valueOt);
	    				dgView.setRpm(valueRpm);
                        dgView.setVlt(valueVlt);
                        dgView.setCht(valueCht);
						dgView.setAfr(valueAfr);
						dgView.setAmb(valueAmbient);
	    				dgView.postInvalidate();
	    			}

					//DataLogger.appendLog("mph:" + valueMph);
	    			doFakeWork();
	           
	    			if ( txtView != null ) {
	    			if ( mpaintOperation == 0 && txtView.isShown() ) {
	    				mTextViewOt.post(setText);
	    				mTextViewCht.post(setText);
	    				mTextViewMph.post(setText);
	    				//mTextView1.post(setText);

	    			} else {
	    				//mTestView.postInvalidate();
	    			}
	    			}
					if ( myGb != null ) {
						//Log.v("AMBIENT", "ambient = " + valueAmbient);
						if ( mGaugeType == GaugeType.OIL_TEMP ) {
							myGb.setValue(valueOt);
							myGb.setOptionalValue(valueOt / 3.0f);
						} else if ( mGaugeType == GaugeType.MPH ) {
							myGb.setValue(valueMph);
							myGb.setOptionalValue(valueDist);
						}
						myGb.postInvalidate();
					}
	    			if ( gbMph != null ) {
						gbMph.setValue(valueOt);
						gbMph.postInvalidate();
					}
	    			if ( gbOt != null ) {
						gbOt.setValue(valueOt);
                       // Log.v(tag(),"Invalidate() n null");
                        gbOt.postInvalidate();
                    }
	    			if ( dgView != null ) 
	    				dgView.postInvalidate();
	    			
	    			iters++;
	    		}
	    	}
	    
	    };
	    
	    
	    new Thread(runnable).start();
	    
	}

	//
	//
	//
	public class UpdateTextTask implements Runnable {
	     public void run() {
	    	 
	    	 if ( mpaintOperation == 0 ) {
	    		 
	    		 if ( mTextViewOt != null )
	    			 mTextViewOt.setText(     "Oil temp: " + valueOt + " F");
	    		 
	    		 if ( mTextViewCht != null )
	    			 mTextViewCht.setText(    "Cyl temp: " + valueCht + " F");
	    		 
	    		 if ( mTextViewMph != null )
	    			 mTextViewMph.setText(    "Speed:    " + valueMph + " Mph");
	    		 
	    		 if ( mTextViewRpm != null )
	    			 mTextViewRpm.setText(    "Rpm:      " + valueRpm + " p/min");
	    			 
	    		 if ( mTextViewDist != null ) 
	    			 mTextViewDist.setText(   "Odom:     " + valueDist + " Miles");
	    		 
	    		 if ( mTextViewAmbient != null ) 
	    			 mTextViewAmbient.setText("Ambient:  " + valueAmbient + " F");
	    		 
	    		 if ( mTextViewMesg != null ) 
	    			 mTextViewMesg.setText(valueInfoString);
	    		 
	    		 if ( mTextViewOp != null ) 
	    			 mTextViewOp.setText(     "Oil Pressure: " + valueOp + " psi");
	    		 
	    		 if ( mTextViewAfr != null )
	    			 mTextViewAfr.setText(   "Afr: " + valueAfr);

				 if ( mTextViewAnalogChan0 != null)
					 mTextViewAnalogChan0.setText(" A0: " + analogChans[0]);
	         // do stuff here
	    	 
	    	 //Log.v("Consumer", "UpdateTextTask.run()" + " " + valueOt + " " + valueCht + " " + valueInfoString);
	    	 }
	     }
	}
	
	// 
	//
	//
	private void doFakeWork() {
	    try {
	      Thread.sleep((int)(factor * 1000f));
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	  }

}
