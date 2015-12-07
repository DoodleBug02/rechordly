package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


public class CropSliderViewBack extends CircularSliderView{

    private Activity context;
    private Drawable soundWave;
    private int time;
    private int black_outline;
    private long mBlackRadius;
    private Paint mPaint = new Paint();

    private float lineOffset = 0;
    private float secondsOffset = 0;

    float image_height;
    float image_width;
    int waveTop;
    int waveBottom;
    float waveStart;
    float waveEnd; // Position it would end on the watch screen
    float waveOffset = 0;
    float waveDiam; // Amount of space on the watch to display wave form (starts at inner edges of blue circle)
    float waveDiamIn; // wave Diam taking into account the 10 offset on both sides

    float timeToLengthRatio;
    float inSecOffset;

    TextView timeText;


    public CropSliderViewBack(Context context) {
        super(context);
        this.context =(Activity) context;
        init(null, 0);
    }

    public CropSliderViewBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (Activity)context;
        init(attrs, 0);
    }

    public CropSliderViewBack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = (Activity)context;
        // init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

//        a.recycle();
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context,attrs,defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CropSliderView, defStyleAttr, 0);

        soundWave  = a.getDrawable(R.styleable.CropSliderView_soundwave);
        time = a.getInt(R.styleable.CropSliderView_time, 0);
        black_outline = a.getDimensionPixelSize(R.styleable.CropSliderView_black_outline, 0);

        a.recycle();
    }

    void setTime(int t) {
        time = t;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // use smaller dimension for calculations (depends on parent size)

        mBlackRadius = w / 2 ;

        int hw = soundWave.getIntrinsicHeight();
        int ww = soundWave.getIntrinsicWidth();

        float ratio = ww/hw;
        image_height = (float) (mCircleRadius*.333*2);
        image_width = image_height * ratio;
        waveTop = (int) (mCircleCenterY - (mCircleRadius * .333));
        waveBottom = (int) (mCircleCenterY + (mCircleRadius * .333));
        waveStart = w - mPadding - mBorderThickness - 10;
        waveEnd = mPadding + mBorderThickness + 10;
        waveDiam = waveEnd - waveStart + 20;
        waveDiamIn = waveEnd - waveStart;
        timeToLengthRatio = time/image_width;
        inSecOffset = timeToLengthRatio * waveDiamIn/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // draw png */
        soundWave.setBounds((int)(waveStart - image_width-waveOffset), waveTop, (int) (waveStart-waveOffset), waveBottom);
        soundWave.draw(canvas);

        // Draw 2 parallel lines
        mPaint.setColor(Color.parseColor("#00a888"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.drawLine(waveEnd-10, waveTop, mPadding + mBorderThickness + 2*mCircleRadius, waveTop, mPaint);
        canvas.drawLine(waveEnd - 10, waveBottom, mPadding + mBorderThickness + 2 * mCircleRadius, waveBottom, mPaint);


        mPaint.setStrokeWidth(0);
        int line = 0; //To be updated when scroll bar moves
        canvas.drawLine(waveStart+lineOffset, waveTop, waveStart+lineOffset, waveBottom, mPaint);

        // outer circle (ring)
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(black_outline);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mBlackRadius, mPaint);

        super.onDraw(canvas);

    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    @Override
    protected void updateSliderState(int touchX, int touchY) {
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        double priorAngle = mAngle;

        mAngle = Math.acos(distanceX / c);
        if (distanceY < 0) {
            mAngle = -mAngle;
        }

        double prevSec = angleToSeconds(priorAngle);
        double sec = angleToSeconds(mAngle);
        float Offset = (float)(sec - prevSec);
        if (Offset < -50) {
            //Went across boundary left -> right
            if (secondsOffset < -30 ) {
                Offset = Offset + 60;
            } else {
                Offset = 0;
                secondsOffset = 0;
                mAngle = priorAngle;
            }

        } else if (Offset > 40) {
            //Went across boundary left <- right
            Offset = Offset - 60;

        }
        if (secondsOffset <= -time && Offset<0) {
            secondsOffset = -time;
            Offset = 0;
            mAngle = priorAngle;
        }

        if (secondsOffset >= 0 && Offset>0) {
            secondsOffset = 0;
            Offset = 0;
            mAngle = priorAngle;
        }

        secondsOffset = secondsOffset + Offset;
        updateWave(secondsOffset);

        timeText = (TextView) context.findViewById(R.id.timeB);

        timeText.setText(getTime() + "");

        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            mListener.onSliderMoved((mAngle - mStartAngle) / (2 * Math.PI));
        }
    }

    /** Works **/
    private double angleToSeconds(double ang) {
        double ret=0;
        if (ang >= 0 && ang <= 1.57079 ) {
            ret = -ang;
            ret = ret + 1.57079;

        } else if (ang > -3.15 && ang < 0) {
            ret = -ang;
            ret = ret + 1.57079;

        } else if (ang > 1.57079 && ang < 3.15) {
            ret = -ang;
            ret = ret + 1.57079+3.15+3.15;

        }
        return ret*(60/6.30);
    }

    private float secondsToXPos(float seconds) {
        return seconds * (1/timeToLengthRatio);
    }

    private void updateWave(float secondsOffset) {
        Log.d("time", "secondsOffset is " + secondsOffset + "");
        Log.d("time", "inSecOffset is" + inSecOffset);
        Log.d("time", "timeToLengthRatio is" + timeToLengthRatio);


        if (secondsOffset > inSecOffset) {
            lineOffset =  secondsToXPos(secondsOffset);
            waveOffset = 0;
            Log.d("time", "lineOffset is " + lineOffset);
        } else if (secondsOffset < (-time - inSecOffset)) {
            lineOffset = secondsToXPos(secondsOffset + time + inSecOffset + inSecOffset);
            waveOffset = secondsToXPos(-time) - secondsToXPos(2*inSecOffset);
        } else {
            float adjustedSecondOffset = secondsOffset - inSecOffset;
            waveOffset = secondsToXPos(adjustedSecondOffset);
            Log.d("time", "waveOffset is " + waveOffset);
        }
    }

    public String getTime() {
        int min = (int)((time+secondsOffset)/60);
        int sec = (int)((time+secondsOffset) % 60);
        String m = String.format("%02d", min);
        String s = String.format("%02d", sec);
        return m+":"+s;
    }


}
