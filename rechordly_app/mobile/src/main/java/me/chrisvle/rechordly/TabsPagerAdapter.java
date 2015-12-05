package me.chrisvle.rechordly;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Goshujin on 12/4/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

//        switch (index) {
//            case 0:
//                // Top Rated fragment activity
//                return new AllFragment();
//            case 1:
//                // Games fragment activity
//                return new MusicFragment();
//            case 2:
//                // Movies fragment activity
//                return new LyricsFragment();
//        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
