package me.chrisvle.rechordly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class SliderNavActivity extends FragmentActivity implements RetryFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener {


    /**
     * The number of pages (wizard steps)
     */
    private static final int NUM_PAGES = 3;

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
    private int slide2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_nav);
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        slide = intent.getIntExtra("start", 1);
        slide2 = intent.getIntExtra("start2", 0);
                mPager = (ViewPager) findViewById(R.id.pager);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
                mPager.setCurrentItem(slide);

    }


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
                    return new EditNavFragment(slide2);
            }
            return new RetryFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

    /** Fragment Interaction Function */
    public String getTime(){
        return time;
    }


}
