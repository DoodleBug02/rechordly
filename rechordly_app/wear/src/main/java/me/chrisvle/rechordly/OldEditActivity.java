package me.chrisvle.rechordly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;

public class OldEditActivity extends FragmentActivity implements
        DoneFragment.OnFragmentInteractionListener,
        EchoFragment.OnFragmentInteractionListener,
        GainFragment.OnFragmentInteractionListener,
        CropFragment.OnFragmentInteractionListener,
        LyricFragment.OnFragmentInteractionListener {

    static final int NUM_ITEMS = 5;

    private PagerAdapter mAdapter;
    private ViewPager mPager;
    private ImageView dots;
    private int start;
    private String time;

    public OldEditActivity() {
        // Required empty public constructor
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Mission_Gothic_Bold.otf");

        setContentView(R.layout.activity_old_edit);
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        start = intent.getIntExtra("start", 2); // Fix this code here
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(start);
        dots = (ImageView) findViewById(R.id.dots_edit);

    }



    private class  ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case (0):
                    return new DoneFragment();
                case (1):
                    return new LyricFragment();
                case (2):
                    return new CropFragment();
                case (3):
                    return new GainFragment();
                case (4):
                    return new EchoFragment();
            }
            return new DoneFragment();
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int i = mPager.getCurrentItem();
            switch (i) {
                case (0):
                    dots.setImageResource(R.drawable.dot1);
                    return;
                case (1):
                    dots.setImageResource(R.drawable.dot2);
                    return;
                case (2):
                    dots.setImageResource(R.drawable.dot3);
                    return;
                case (3):
                    dots.setImageResource(R.drawable.dot4);
                    return;
                case (4):
                    dots.setImageResource(R.drawable.dot5);
                    return;
            }

        }
    }

    /** Fragment Interaction Function */
    public String getTime(){
        return time;
    }

    /** Fragment Interaction Function to let fragments know if they are an old edit **/
    public boolean oldEdit() {
        return true;
    }

}


