package com.resc.remgauge;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends ActionBarActivity {

	
	String TG = "MAIN";
    /*
     The android.support.v4.view.PagerAdapter will provide
     fragments for each of the sections. We use a
     FragmentPagerAdapter derivative, which will keep every
     loaded fragment in memory. If this becomes too memory intensive, it
     may be best to switch to a
     android.support.v4.app.FragmentStatePagerAdapter
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    public RedBearService mBearService = null;
    /*
     * The ViewPager that will host the section contents.
     */
    ViewPager mViewPager;
    private static Context gContext;
    static ImageView mImageView;
    static Display mDisplay;
    static Intent sensorServiceIntent = null;
    static Intent redBearServiceIntent = null;
    static int NUMBER_OF_PANES = 3;
    static int DIGITAL_PANE = 0;
    static int OT_PANE = 2;
    static int MPH_PANE = 1;
    static int CHT_PANE = 4;
    static int TEXT_PANE = 3;
    static View digitalView;
    static View mphGaugeView;
    static View otGaugeView;
    View opGaugeView;
    View vltGaugeView;
    static View textView;
    static Consumer mCons;
    Set<String> bleDevDescr;
    Set<String> bleDevAddr;
    BluetoothGatt blendD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //
        // Call upstairs.
        //
        super.onCreate(savedInstanceState);

        //
        //
        setContentView(R.layout.activity_main);

        //
        //
        gContext = getApplicationContext();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //mSectionsPagerAdapter.getItemId(0);
        GlobalObject.viewPager = mViewPager;

        //Intent intent = new Intent(this, SpeedCollector.class);
        //MainActivity.this.startActivity(intent);

        //
        // Start bluetooth
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (!ba.isEnabled()) {

            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        }
        //private Set<BluetoothDevice>pairedDevices;
        Set<BluetoothDevice> pairedDevices;
        pairedDevices = ba.getBondedDevices();

        int dv = 0;
        int prd = 0;

        for ( BluetoothDevice s : pairedDevices ) {
            dv++;
            switch (s.getBondState()) {
                case BluetoothDevice.BOND_BONDED:
                    prd++;
                default:
                    //
            }
        }
        Context context = getApplicationContext();
        CharSequence text = "Found " + dv +" devices " + prd + "are paired";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        // 
        // Start the sensor service.
        //
        if (sensorServiceIntent == null) {

            sensorServiceIntent = new Intent(gContext, SensorService.class);
            //
            // Set parameters to service.
            //i.putExtra("KEY1", "Value to be used by the service");

            gContext.startService(sensorServiceIntent);
            Log.d(TG,"Started sensor service");
        }

        if ( redBearServiceIntent == null ) {
            redBearServiceIntent = new Intent(gContext,RedBearService.class);

            gContext.startService(redBearServiceIntent);
            Log.d(TG,"Started redBearService");
        }
        /*
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        ba.startDiscovery();
        */
        //BluetoothLeScanner bls = ba.getBluetoothLeScanner();
      //  ba.stopLeScan(leScanCallback);
     //   ba.startLeScan(leScanCallback);
        Log.v(TG,"Started scann");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
        	gContext.stopService(sensorServiceIntent);
            gContext.stopService(redBearServiceIntent);
            finish();
            sensorServiceIntent = null;
            Log.v(TG, "stopped SensorService");
            if ( mCons != null )
                mCons.stop();
        }
        
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v(TG, "onOptionsItemSelected()");
        if (id == R.id.action_settings) {
            //return true;
        	Log.v("MAIN", "action_settings");
       }
        return super.onOptionsItemSelected(item);
    }

    //
    // A FragmentPagerAdapter that returns a fragment corresponding to
    // one of the sections/tabs/pages.
    //
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

    	//
    	// Needed, check documentation as to why.
    	//
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        	Log.v(TG, "onCreateView() adapter");
        	return null;
        }
        
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        	Log.v(TG, "getItem() " + position);
        	
        	//
        	// First pane, its the digital pane
        	if ( position == DIGITAL_PANE)
        		return DigitalFragment.newInstance(position);
        	
        	//
        	// Second pane, its a Gauge.
        	if ( position == OT_PANE || position == MPH_PANE )
        		return GaugeFragment.newInstance(position );
        	
        	//
        	// Third pane, its a Gauge.
        //	if ( position == 2 )
        	//	return GaugeFragment.newInstance(position + 1);
        	
        	//
        	// Fourth pane, the text window.
        	if ( position == TEXT_PANE)
        		return FragmentHandler.newInstance(position);
        	
        	
			return null;
        		
        
        }

        @Override
        public int getCount() {
            // 
            return NUMBER_OF_PANES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            Log.v(TG, "position = " + position);
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    //
    // Fragment handler, does the work.
    //
    public static class FragmentHandler extends Fragment {
    	
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "Page";

        private String title;
        private int page;
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentHandler newInstance(int sectionNumber) {
        	
            FragmentHandler fragment = new FragmentHandler(sectionNumber);
            Bundle args = new Bundle();
            Log.v("MAIN", "section number " + sectionNumber);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            
            fragment.setArguments(args);
            Log.v("MAIN", "new fragment " + args);
            return fragment;
        }

        //
        // Requires an empty constructor in case this 'activity' get restarted.
        //
        public FragmentHandler() {}
        
        public FragmentHandler(Context con) {}
        
        public FragmentHandler(int which) {
        	Log.v("MAIN", "Fragment " + which);
        }

        //
        // Sets up a 'view' for a page.
        //
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	
        	View rootView = null;
        	
        	page = getArguments().getInt("Page", 0);
        	mDisplay = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        	
        	if ( page == TEXT_PANE ) {

                textView = inflater.inflate(R.layout.text_layout, container, false);
                Consumer c = new Consumer(textView);
                c.startProgress();
                Log.v("MAIN", "new text view");
                return textView;
            }
            return rootView;
        }
       
        public void onClick(View v) {
        	Log.v("MAIN", "onClick()" + v.toString());
        }
    }
    
    
    //
    // 
    //
    public static class GaugeFragment extends Fragment {
    	
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "Page";

        private String title;
        private int page;
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GaugeFragment newInstance(int sectionNumber) {
            
        	GaugeFragment fragment = new GaugeFragment();
            Bundle args = new Bundle();
            
            Log.v("MAIN", "section number " + sectionNumber);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
           
            if ( sectionNumber == TEXT_PANE )
            	args.putString("Text", "TextView");
            
            if ( sectionNumber == DIGITAL_PANE )
            	args.putString("Digital", "DigitalView");
            
            if ( sectionNumber == OT_PANE || sectionNumber == MPH_PANE )
            	args.putString("Gauge", "GaugeView");
            
            fragment.setArguments(args);
            Log.v("MAIN", "GaugeFragment new fragment " + args);
            return fragment;
        }

        //
        // Fragments require an empty constructor or
        //  the program will crash, this is undocumented.
        //
        public GaugeFragment() {}
        //
        // Gets called when the view is created.
        //
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) 
        {
        	
        	super.onCreateView(inflater, container, savedInstanceState);
        	
        	Display display = ((WindowManager)
        			getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        	page = getArguments().getInt("Page", 1);

        	View rootView = null;
        	Log.v("MAIN", "GAUGE FRAGMENT page: " + page);


            if ( page == OT_PANE )
        	{
        		Log.v("MAIN", "GAUGE FRAGMENT ot_gauge");
        		otGaugeView = inflater.inflate(R.layout.ot_gauge, container, false);
                Consumer c = new Consumer(otGaugeView,"Oil Temp","Hi","F",50,300,200,Consumer.GaugeType.OIL_TEMP);
                if ( otGaugeView == null ) {
                    Log.v("MAIN", "MPH GAUGE Null?");
                }
                else {
                    c.startProgress();
                    return otGaugeView;
                }
        	} else if ( page == MPH_PANE ) {
                Log.v("MAIN", "GAUGE FRAGMENT mph_gauge");
                // TODO add gph view
                mphGaugeView = inflater.inflate(R.layout.mph_gauge, container, false);
                if ( mphGaugeView != null ) {
                    Consumer c = new Consumer(mphGaugeView, "Mph", "You","Mph",0,100,50,Consumer.GaugeType.MPH);
                    c.startProgress();
                }
                return mphGaugeView;
            }


        	return rootView;
        	
        }
      
        public void onClick(View v) {
        	Log.v("MAIN", "onClick()" + v.toString());
        }
    }
    
    public static class DigitalFragment extends Fragment {
    	
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "Page";

        private String title;
        private int page;
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DigitalFragment newInstance(int sectionNumber) {
            
        	DigitalFragment fragment = new DigitalFragment();
            Bundle args = new Bundle();
            
            Log.v("MAIN", "section number " + sectionNumber);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
         
            fragment.setArguments(args);
       
            return fragment;
        }

        //
        // Fragments require an empty constructor. 
        //
        public DigitalFragment() {}	

        //
        // Gets called when the view is created.
        //
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) 
        {
        	
        	super.onCreateView(inflater, container, savedInstanceState);
        	
        	Display display = ((WindowManager)
        			getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        	
        	Log.v("MAIN","onCreateView() digitalFrag");
        	page = getArguments().getInt("Pagef", 1);
        
        	View rootView = inflater.inflate(R.layout.digi_lay, container, false);
        	
        	digitalView = rootView.findViewById(R.id.DigitalView2);
        	Consumer c = new Consumer(rootView, (DigitalView) digitalView);
        	c.startProgress();
        	///mCons.setGbView(rootView,gb);
        	//return (View) tv.getRootView();
        	///Log.v("VIEW", "onCreateView " + page);
        	///return (View) gb.getRootView();
        	return digitalView.getRootView();
        	
        }
      
        public void onClick(View v) {
        	Log.v("MAIN", "onClick()" + v.toString());
        }
    }
}

