package me.chrisvle.rechordly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SliderNavActivity extends FragmentActivity implements RetryFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener {


    /**
     * The number of pages (wizard steps)
     */
    private static final int NUM_PAGES = 8;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private String time;
    private int slide;
    private ImageView dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_nav);
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        slide = intent.getIntExtra("start", 1);

                mPager = (ViewPager) findViewById(R.id.pager);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
                mPager.setCurrentItem(slide);
        dots = (ImageView) findViewById(R.id.dots);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case(0):
                    return new PlaybackFragment();
                case(1):
                    return new RetryFragment();
                case(2):
                    return new DoneFragment();
                case(3):
                    return new LyricFragment();
                case(4):
                    return new CropFragment();
                case(5):
                    return new VolumeFragment();
                case(6):
                    return new FilterFragment();
                case(7):
                    return new EchoFragment();

            }
            return new RecordFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public void startUpdate(ViewGroup container){
            super.finishUpdate(container);
            int i = mPager.getCurrentItem();
            switch(i) {
                case(0):
                    dots.setVisibility(View.INVISIBLE);
                    return;
                case(1):
                    dots.setVisibility(View.INVISIBLE);
                    return;
                case(2):
                    dots.setImageDrawable(getDrawable(R.drawable.dot1));
                    dots.setVisibility(View.VISIBLE);
                    return;
                case(3):
                    dots.setImageDrawable(getDrawable(R.drawable.dot2));
                    dots.setVisibility(View.VISIBLE);
                    return;
                case(4):
                    dots.setImageDrawable(getDrawable(R.drawable.dot3));
                    dots.setVisibility(View.VISIBLE);
                    return;
                case(5):
                    dots.setImageDrawable(getDrawable(R.drawable.dot4));
                    dots.setVisibility(View.VISIBLE);
                    return;
                case(6):
                    dots.setVisibility(View.VISIBLE);
                    return;
                case(7):
                    dots.setVisibility(View.VISIBLE);
                    return;
            }

        }
    }

    /** Fragment Interaction Function */
    public String getTime(){
        return time;
    }


}
