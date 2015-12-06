package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Circular Slider for the Volume Screen
 */
public class GainSliderView extends CircularSliderView {
    private int mStopX;
    private int mStopY;
    private int mStopXL;
    private int mStopYL;
    private int mStopXR;
    private int mStopYR;
    private int prevThumbX;
    private boolean right;
    private Activity context;

    public GainSliderView(Context context) {
        super(context);
        this.context =(Activity) context;
        init(null, 0);
    }

    public GainSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (Activity)context;
        init(attrs, 0);
    }

    public GainSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = (Activity)context;
        // init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

//        a.recycle();

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStopX = mCircleCenterX;
        mStopY = mCircleCenterY-mCircleRadius;
        mStopXL = (int)( mCircleCenterX + mCircleRadius * Math.cos(Math.toRadians(95)));
        mStopYL = (int)( mCircleCenterY - mCircleRadius * Math.sin(Math.toRadians(95)));
        mStopXR = (int)( mCircleCenterX + mCircleRadius * Math.cos(Math.toRadians(85)));
        mStopYR = (int)( mCircleCenterY - mCircleRadius * Math.sin(Math.toRadians(85)));

    }

    private boolean isRight() {

        Log.d("VolumeSlider", "isRight: prevThumbX is " + prevThumbX);
        if (prevThumbX > mStopX) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLeft() {
        Log.d("VolumeSlider", "isLeft: prevThumbX is " + prevThumbX);
        if (prevThumbX < mStopX) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // start moving the thumb (this is the first touch)
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x < mThumbX + mThumbSize && x > mThumbX - mThumbSize && y < mThumbY + mThumbSize && y > mThumbY - mThumbSize) {
                    mIsThumbSelected = true;
                    updateSliderState(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // still moving the thumb (this is not the first touch)
                if (mIsThumbSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderState(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                // finished moving (this is the last touch)
                mIsThumbSelected = false;
                break;
            }
        }

        // redraw the whole component
        invalidate();
        return true;
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    protected void updateSliderState(int touchX, int touchY) {
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        double newAngle = Math.acos(distanceX / c);
        if (distanceY < 0) {
            newAngle = -newAngle;
        }
        double x =  (mCircleCenterX + mCircleRadius * Math.cos(newAngle));
        double y =  (mCircleCenterY - mCircleRadius * Math.sin(newAngle));

//        if (Math.abs(mThumbX - mStopX)<35 && Math.abs(mThumbY - mStopY)<35) {
//            if ((x > mStopX) && isLeft()) {
//                return;
//            }
//            if ((x < mStopX) && isRight()){
//                return;
//            }
//        }
        Log.d("Stop", "mStopX is " + mStopX);
//        Log.d("Stop", "mStopY is " + mStopY);
//        Log.d("Stop", "mThumbX is " + mThumbX);

//        Log.d("Stop", "mStopXL is" + mStopXL);
//        Log.d("Stop", "mStopYL is" + mStopXR);
//        Log.d("Stop", "mStopXR is" + mStopYL);
//        Log.d("Stop", "mStopYR is" + mStopYR);

        Log.d("Stop", "x is " + x);
        Log.d("Stop", "Angle is" + Math.toDegrees(mAngle));
        Log.d("Stop", "newAngle is" + Math.toDegrees(newAngle));
        Log.d("Stop", "newAngle diff is " + (Math.abs((Math.toDegrees(newAngle))-90)));
        Log.d("Stop", "Angle diff is" + (Math.abs((Math.toDegrees(mAngle))-90)));
        Log.d("Stop", "isLeft is " + isLeft());
        Log.d("Stop", "x > mStopX" + (x > mStopX));

//        if (Math.abs(mThumbX - mStopXL)<15 && Math.abs(mThumbY - mStopYL)<15) {
//            if ((x > mStopX) && isLeft()) {
//                return;
//            }
//        }
//
//        if (Math.abs(mThumbX - mStopXR)<25 && Math.abs(mThumbY - mStopYR)<25) {
//            if ((x < mStopX) && isRight()) {
//                return;
//            }
//        }



        if (Math.abs((Math.toDegrees(mAngle))-90)<15) {
            if ((x > mStopX) && isLeft()) {
                return;
            }
            if ((x < (mStopX+1)) && isRight()){
                return;
            }
        }


        mAngle = newAngle;


        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            mListener.onSliderMoved((mAngle - mStartAngle) / (2 * Math.PI));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mThumbX != mStopX) {
            prevThumbX = mThumbX;
        }
        TextView gain = (TextView) context.findViewById(R.id.gain);

        gain.setText(getGain() + "");
    }

    private double angleConvert(double ang) {
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
        return ret*(100/6.30);
    }

    public int getGain() {
        return (int)Math.round(angleConvert(mAngle));
    }

}

