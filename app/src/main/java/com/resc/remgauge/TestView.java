package com.resc.remgauge;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class TestView extends View {

	Paint circpaint;
	Paint rimpaint;
	Paint bgPaint;
	Paint facePaint;
	Paint handPaint;
	Paint rimPaint;
	Paint scalePaint;
	Paint rimShadowPaint;
	Paint titlePaint;
	Path handPath;
	Paint logoPaint;
	Paint handScrewPaint;
	Paint backgroundPaint;
	Paint textPaint;
	Bitmap faceTexture;
	Canvas mCan;
	RectF rimRect;
	RectF faceRect;
	RectF scaleRect;
	Path titlePath;
	Matrix logoMatrix;
	Bitmap bitmap;
	Display display;
	Paint rimCirclePaint;
	private Bitmap logo;
	private float logoScale;
	int totalNicks = 85;
	int centerDegree = 62;
	float degreesPerNick = 360.0f / totalNicks;
	int minDegrees = 0;
	int maxDegrees = 100;
	WindowManager wm;
	int winWidth = 0;
	int winHeight = 0;
	int mX = 0;
	int x;
	int y;
	int radius = -1;
	boolean setLayerType = true;
	static boolean drawnScale = false;
	
	public TestView(Context context) {
		super(context);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			setLayerType = true;
		}
		
		 
		// 
		// Get the window size.
		//
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		winWidth = p.x; winHeight = p.y;
		
		circpaint = new Paint();
		rimpaint = new Paint();
		//					 Left,  Top,  Right, Bottom 
		rimRect = new RectF(0.2f, 0.4f, 0.8f,0.8f);
		rimCirclePaint = new Paint();
		rimCirclePaint.setAntiAlias(true);
		rimCirclePaint.setStyle(Paint.Style.STROKE);
		rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
		rimCirclePaint.setStrokeWidth(0.005f);
		
		rimpaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rimpaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f,1.0f,
							Color.rgb(0xf0,0xf5,0xf0),
							Color.rgb(0x30,0x31,0x30),
							Shader.TileMode.CLAMP));
		bgPaint = new Paint();
		facePaint = new Paint();
		bitmap = Bitmap.createBitmap(400, 600, Bitmap.Config.ARGB_8888);
		textPaint = new Paint();
		textPaint.setColor(Color.GREEN);
		textPaint.setTextSize(0.07f);
		textPaint.setTypeface(Typeface.DEFAULT);
		//textPaint.setTextScaleX(0.8f);
		//textPaint.setTextAlign(Paint.Align.CENTER);		
		textPaint.setTextAlign(Align.LEFT);
		faceRect = new RectF();
		drawnScale = false;
		setupDrawTools();
		
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	
        super.onDraw(canvas);
        x = getWidth();
        y = getHeight();
   
      
        boolean doIt =  true;
      
        if ( doIt ) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale((float)x, (float) y);
       
        //float rs = 0.02f;
        //faceRect.set(rimRect.left + rs, rimRect.top + rs, rimRect.right - rs, rimRect.bottom - rs);
      
        bgPaint.setColor(Color.RED);
        bgPaint.setStyle(Paint.Style.FILL);
        //canvas.drawBitmap(bitmap, 0,0, bgPaint);
        //canvas.drawPaint(bgPaint);
        
        facePaint.setColor(Color.BLACK);
        facePaint.setStyle(Paint.Style.FILL);
       
        
        
        drawHand(canvas);
        
        canvas.drawOval(rimRect, rimpaint);
        canvas.drawOval(rimRect,rimCirclePaint); 
        //canvas.drawOval(faceRect,facePaint);
        Paint handPaint = new Paint();
        handPaint.setStrokeWidth(0.015f);
        handPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        handPaint.setColor(Color.RED);
     
        
        //canvas.drawLine(x - 250, y - 200, 200, 200, handPaint);
        //canvas.drawLine(x / 2, y - 250, (x / 2 ) + radius, y - 700, handPaint);
       float xr = 0.0010f * (float) radius;
       // float yr = 0.0006f * (float) radius;
        //float rr = 0.2f + ((float) radius / 1000.0f);
        if ( radius < 200 ) xr = (-1.0f * xr);
      
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        //canvas.rotate((float)radius,rimRect.centerX(), rimRect.centerY())
        //canvas.drawLine(rimRect.centerX(), rimRect.centerY() + 0.2f, rimRect.centerX()  , 
        	//rimRect.centerY() ,
        	//handPaint);
        
        setLayerType(LAYER_TYPE_SOFTWARE, textPaint);
        textPaint.setLinearText(true);
       
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        float m =  ( radius - centerDegree) / 2.0f * degreesPerNick;
        
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.drawText(Float.toString(radius), 0.45f, 0.27665f, textPaint);
        canvas.restore();
        
        canvas.rotate((float)m,0.5f, 0.5f);
       
        //canvas.drawLine(rimRect.centerX(), rimRect.centerY() + 0.333f, rimRect.centerX()  , 
        		//rimRect.centerY() ,
        		//handPaint);
        
		handPath.moveTo(0.5f, 0.5f - 0.2f);
		handPath.lineTo(0.5f - 0.010f,0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f, 0.5f + 0.2f);
		handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
		
		canvas.drawPath(handPath, handPaint);
        canvas.restore();
        
        //canvas.drawOval(faceRect, facePaint);
        //canvas.drawOval(faceRect, rimShadowPaint);
        
        
        //drawScale(canvas);
       
        //drawHand(canvas);
        //canvas.restore();
       //canvas.drawLine(rimRect.centerX(), rimRect.centerY() + 0.1f, rimRect.centerX()  , 
        		//rimRect.centerY() ,
        		//handPaint);
     
		
		canvas.restore();
		canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
		canvas.drawText(Float.toString(radius), 0.5f, 0.7665f, textPaint);
        
        }
    }
    public void setC(int n ) {
    	
    	
    	//rimRect.bottom = n;
    	radius = n;
    	if ( radius > 360 || radius < 0)
    		radius = 0;
    	
    	
    	//invalidate();
    	//this.invalidate();
    	//mCan.drawCircle((x / 2) - mX, y / 2, 100, paint);
    }
    boolean handInitialized = false;
    int handPosition = 10;
    
	private void drawHand(Canvas canvas) {
		boolean handInitialized = false;
		
		if (handInitialized) {
			float handAngle = degreeToAngle(handPosition);
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			handPath.moveTo(0.5f, 0.5f + 0.2f);
			handPath.lineTo(0.50f - 0.010f,0.5f + 0.2f - 0.007f);
			handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
			handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
			handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
			handPath.lineTo(0.5f, 0.5f + 0.2f);
			canvas.rotate(handAngle, 0.5f, 0.5f);
			canvas.drawPath(handPath, handPaint);
			canvas.restore();
			
			canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
		}
	}
	private int nickToDegree(int nick) {
		int rawDegree = ((nick < totalNicks / 2) ? nick : (nick - totalNicks)) * 2;
		int shiftedDegree = rawDegree + centerDegree;
		return shiftedDegree;
	}
	
	private float degreeToAngle(float degree) {
		return (degree - centerDegree) / 2.0f * degreesPerNick;
	}
	private void drawScale(Canvas canvas) {
		canvas.drawOval(scaleRect, scalePaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		for (int i = 0; i < totalNicks; ++i) {
			float y1 = scaleRect.top - 0.00005f;
			float y2 = y1 + 0.025f;
			float y22 = y1 + 0.05f;
		
			int value = nickToDegree(i);
			//Log.v("FFFFF", "tnicks " + totalNicks + " value " + value + " minDegrees " +minDegrees + " maxDeg " + maxDegrees);
			if ( value >= minDegrees && value <= maxDegrees )
				canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);
			
			if (i % 5 == 0) {
				
				if (value >= minDegrees && value <= maxDegrees) {
					canvas.drawLine(0.5f, y1, 0.5f, y22, scalePaint);
					String valueString = Integer.toString(value);
					//Log.v("VALUE", "v = " + value + " val string = " + valueString);
					scalePaint.setColor(Color.BLACK);
					scalePaint.setAlpha(55);
					canvas.drawText(valueString, 0.5f, y2 +0.0635f, scalePaint);
				}
			}
			scalePaint.setColor(Color.WHITE);
			
			canvas.rotate(degreesPerNick, 0.5f, 0.5f);
		}
		canvas.restore();		
	}
    void setupDrawTools() {
    	rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

		// the linear gradient is a bit skewed for realism
		rimPaint = new Paint();
		rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, 
										   Color.rgb(0xf0, 0xf5, 0xf0),
										   Color.rgb(0x30, 0x31, 0x30),
										   Shader.TileMode.CLAMP));		
		
		rimCirclePaint = new Paint();
		rimCirclePaint.setAntiAlias(true);
		rimCirclePaint.setStyle(Paint.Style.STROKE);
		rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
		rimCirclePaint.setStrokeWidth(0.005f);

		float rimSize = 0.02f;
		faceRect = new RectF();
		faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize, 
			     rimRect.right - rimSize, rimRect.bottom - rimSize);		

		faceTexture = BitmapFactory.decodeResource(getContext().getResources(), 
				   R.drawable.ic_launcher);
		BitmapShader paperShader = new BitmapShader(faceTexture, 
												    Shader.TileMode.MIRROR, 
												    Shader.TileMode.MIRROR);
		Matrix paperMatrix = new Matrix();
		facePaint = new Paint();
		facePaint.setFilterBitmap(true);
		paperMatrix.setScale(1.0f / faceTexture.getWidth(), 
							 1.0f / faceTexture.getHeight());
		paperShader.setLocalMatrix(paperMatrix);
		facePaint.setStyle(Paint.Style.FILL);
		facePaint.setShader(paperShader);

		rimShadowPaint = new Paint();
		rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect.width() / 2.0f, 
				   new int[] { 0x00000000, 0x00000500, 0x50000500 },
				   new float[] { 0.96f, 0.96f, 0.99f },
				   Shader.TileMode.MIRROR));
		rimShadowPaint.setStyle(Paint.Style.FILL);

		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		//scalePaint.setColor(0x9f004d0f);
		scalePaint.setColor(Color.WHITE);
		scalePaint.setStrokeWidth(0.010f);
		scalePaint.setAntiAlias(true);
		
		scalePaint.setTextSize(0.035f);
		scalePaint.setTypeface(Typeface.SANS_SERIF);
		scalePaint.setTextScaleX(2.396668f);

		scalePaint.setTextAlign(Paint.Align.CENTER);		
		//setLayerType(LAYER_TYPE_SOFTWARE, scalePaint);
		scalePaint.setLinearText(true);
		
		float scalePosition = 0.0050f;
		scaleRect = new RectF();
		scaleRect.set(faceRect.left + scalePosition, faceRect.top + scalePosition,
					  faceRect.right - scalePosition, faceRect.bottom - scalePosition);

		titlePaint = new Paint();
		titlePaint.setColor(0xaf946109);
		titlePaint.setAntiAlias(true);
		titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		titlePaint.setTextAlign(Paint.Align.CENTER);
		titlePaint.setTextSize(0.05f);
		titlePaint.setTextScaleX(0.8f);

		titlePath = new Path();
		titlePath.addArc(new RectF(0.24f, 0.24f, 0.76f, 0.76f), -180.0f, -180.0f);

		logoPaint = new Paint();
		logoPaint.setFilterBitmap(true);
		logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
		logoMatrix = new Matrix();
		logoScale = (1.0f / logo.getWidth()) * 0.3f;;
		logoMatrix.setScale(logoScale, logoScale);

		handPaint = new Paint();
		//handPaint.setAntiAlias(true);
		//handPaint.setColor(0xff392f2c);
		handPaint.setColor(Color.BLUE);
		//handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
		handPaint.setStyle(Paint.Style.FILL);	
		handPaint.setStrokeWidth(0.055f);
		//setLayerType(LAYER_TYPE_SOFTWARE, handPaint);
		//handPaint.setLinearText(true);
		
		handPath = new Path();
		handPath.moveTo(0.5f, 0.5f + 0.2f);
		handPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f, 0.5f + 0.2f);
		handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
		
		handScrewPaint = new Paint();
		handScrewPaint.setAntiAlias(true);
		handScrewPaint.setColor(0xff493f3c);
		handScrewPaint.setStyle(Paint.Style.FILL);
		
		backgroundPaint = new Paint();
		backgroundPaint.setFilterBitmap(true);
    }
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		} 
	}
	
	// in case there is no size specified
	private int getPreferredSize() {
		return 300;
	}
    
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d("TESTVIEW", "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d("TESTVIEW", "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		
		int chosenDimension = Math.min(chosenWidth, chosenHeight);
		
		setMeasuredDimension(chosenDimension, chosenDimension);
	}
}
