package com.resc.remgauge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class DigitalView extends View {
	
	private Paint labelPaint;
	private Paint roboPaint;
	
	private Paint scalePaint;
	private Paint miscPaint;
	private Paint digitalPaint;
	boolean setLayerType = false;
	Map<String, Integer> positionMap ;

	//
	static float valMph = 0.0f;
	static float valOt = 0;
	static int valRpm = 0;
	static int valCht = 0;
	static int valOp = 0;
	static float valAmb = 0.1f;
	//

	int valAfr = 0;
	static int valOd = 0;
    float valVlt = 0.0f;
	float mDigitalTextSize = 55f;
	int mHeight;
	int mWidth;
	
	public DigitalView(Context context) {
		super(context);
		initialize(context, null);
	}

	public DigitalView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initialize(context,attrs);
	}

	public DigitalView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initialize(context,attrs);
	}
	
	String[] mValueArray = {"Mph","This","That","And"};
	
	void initialize(Context context, AttributeSet attrs) {
		screenResolution(context);

		positionMap = new HashMap<String,Integer>();
		positionMap.put("Mph", 0);
		positionMap.put("Rpm",1);
		positionMap.put("OT F", 2);
		positionMap.put("Oil PSI", 3);
		positionMap.put("CHT F", 4);
		positionMap.put("Volt", 5);
		positionMap.put("Odom", 6);
		positionMap.put("AFR", 7);
		positionMap.put("Amb", 8);

		/*
		if ( context != null && attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Digital);
			int jj = 0;
			jj = a.getInt(R.styleable.Digital_smallFormat, jj);
			mDigitalTextSize = a.getFloat(R.styleable.Digital_valueSize,mDigitalTextSize);
			String aa;
			aa = a.getString(R.styleable.Digital_orderList);
			if ( aa != null )
			{
				mValueArray = aa.split("\\-");
				Log.v("DIGITAL", "hey = " + jj + " aa" + aa+ " jj" + mValueArray[3]);
			}
			
			//int jj = a.getInt(R.styleable.,           totalNotches);
			a.recycle();
		}
		*/

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType = true;
        }

		roboPaint = new Paint();
		roboPaint.setColor(Color.GREEN);
		roboPaint.setTextSize(0.087f);
		Typeface textPaintTF = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		roboPaint.setTypeface(Typeface.create(textPaintTF, Typeface.BOLD));
		//textPaint.setTextSize(75);
		roboPaint.setTextAlign(Align.LEFT);
		setLayerType(LAYER_TYPE_SOFTWARE, roboPaint);
		roboPaint.setLinearText(true);

		digitalPaint = new Paint();
		digitalPaint.setColor(Color.GREEN);
		digitalPaint.setTextSize(mDigitalTextSize);
		////Typeface digitalPaintTF = Typeface.createFromAsset(context.getAssets(), "fonts/LetsGoDigital.ttf");
	
		Typeface digitalPaintTF = Typeface.create("Ariel", Typeface.NORMAL);
		digitalPaint.setTypeface(Typeface.create(digitalPaintTF, Typeface.BOLD));
		
		digitalPaint.setTextAlign(Align.LEFT);

		//setLayerType(LAYER_TYPE_SOFTWARE, digitalPaint);
		//digitalPaint.setLinearText(true);
		
		Typeface scaleTF = Typeface.createFromAsset(context.getAssets(), "fonts/Mechanical.ttf");
		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.FILL);
		scalePaint.setColor(Color.RED);
		scalePaint.setAntiAlias(true);
		//scalePaint.setTextSize(0.055675f);
        scalePaint.setTextSize(55f);
	    scalePaint.setTypeface(Typeface.create(scaleTF, Typeface.NORMAL));
	    //scalePaint.setTextScaleX(6f);
	    scalePaint.setTextAlign(Paint.Align.CENTER); 
	    scalePaint.setLinearText(true);
	        
	    labelPaint = new Paint();
	   // miscPaint.setTextSize(DRAWING_CACHE_QUALITY_AUTO);
	    labelPaint.setColor(Color.RED);
	    labelPaint.setTextSize(55f);
	    labelPaint.setAntiAlias(true);
	    labelPaint.setTextAlign(Align.LEFT);
	    labelPaint.setLinearText(true);
	    labelPaint.setTypeface(Typeface.DEFAULT_BOLD);

	}
	
	public void setMph(float mph) {
		valMph = mph;
	}
    public void setVlt( float vlt ) { valVlt = vlt;}
	public void setOt( float ot ) {
		valOt = ot;
	}
	public void setCht(int cht) {
		valCht = cht;
	}
	public void setRpm(int rpm) {
		valRpm = rpm;
	}
	public void setOp( int op ) { valOp = op ; }
	public void setAfr( int afr ) { valAfr = afr; }
	public void setAmb( float v ) { valAmb = v ; }


	public void setElementRow(String which, int pos ) {
		positionMap.put(which, pos);
	}

	private int getElementRow(String which ) {
		Integer a = positionMap.get(which);
		if (a == null ) {
			return positionMap.size() + 1;
		}
		else {
			return a;
		}
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		////Log.v("DigitalView", "onDraw()");
		float h = (float) getHeight();
        float w = (float) getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
       // canvas.scale(h , w);

		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.BLACK);
		canvas.drawPaint(p);
        int scaledSize = getResources().getDimensionPixelSize(R.dimen.scalePaintDimen);
        Paint np = new Paint();
        np.setColor(Color.WHITE);
        scalePaint.setLinearText(true);
        scalePaint.setTextSize(scaledSize);
        np.setTextSize(scaledSize);

        /*
        canvas.drawText(String.valueOf(h),60,50,np);
        canvas.drawText(String.valueOf(w),100,50,np);
        */

        addText(canvas, "Mph", String.valueOf(valMph), getElementRow("Mph"));
        addText(canvas, "Rpm", String.valueOf(valRpm),getElementRow("Rpm"));
        addText(canvas, "OT F", String.valueOf(valOt),getElementRow("OT F"));
        addText(canvas, "Oil PSI", String.valueOf(valOp),getElementRow("Oil PSI"));
        addText(canvas, "CHT F", String.valueOf(valCht),getElementRow("CHT F"));
        addText(canvas, "Volt",  String.valueOf(valVlt),getElementRow("Volt"));
        addText(canvas, "Odom", String.valueOf(valOd),getElementRow("Odom"));
        addText(canvas, "AFR", String.valueOf(valAfr),getElementRow("AFR"));
		addText(canvas, "Amb",  String.valueOf(valAmb),getElementRow("Amb"));

	}

	private void addText( Canvas canvas, String desc, String val, int index ) {

        canvas.drawText(desc, 100f,(90f * index) + 90f, scalePaint);
        canvas.drawText(val, 300f, (90f * index) + 90f, digitalPaint);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		Log.d("DIGITALVIEW", "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d("DIGITALVIEW", "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);

	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int chosenWidth = chooseDimension(widthMode, widthSize);
	    int chosenHeight = chooseDimension(heightMode, heightSize);

	    int chosenDimension = Math.min(chosenWidth, chosenHeight);

	    Log.v("DIGIALVIEW", "Width " + widthSize + " Height " + heightSize + " ModeW " + widthMode + " ModeH " + heightMode);
	    
	   // if ( widthSize > 1000 )
	    //	setMeasuredDimension(widthSize,610);
	   // else
	    	//setMeasuredDimension(widthSize,1240);
	    setMeasuredDimension(widthSize,mHeight);
	}
	
	//
	// Determine the screen resolution
	// and set it as a property of the object.
	//
	public void screenResolution ( Context context ) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int width = metrics.widthPixels ;
		int height = metrics.heightPixels;
		Log.v("DIGITALVIEW", "width " + width + " height " + height);
		mHeight = height;
		mWidth = width;
	}

	 private int chooseDimension(int mode, int size) {
	        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
	            return size;
	        } else { // (mode == MeasureSpec.UNSPECIFIED)
	            //return getPreferredSize();
	        	return size;
	        } 
	    }

	 // in case there is no size specified
	 private int getPreferredSize() {
	        return 550;
	 }
	 
}
