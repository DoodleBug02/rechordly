package me.chrisvle.rechordly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditNavFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditNavFragment extends Fragment {

    static final int NUM_ITEMS = 5;

    private ViewPagerAdapter mAdapter;
    private ViewPager mPager;
    private ImageView dots;
    private int start;
    private RelativeLayout.LayoutParams lp;
    private float density;

    private final float DONE = 43;
    private final float LYRIC = 66;
    private final float CROP = 89;
    private final float GAIN = 112;
    private final float ECHO = 134;




    private OnFragmentInteractionListener mListener;

    public EditNavFragment() {
        // Required empty public constructor
    }

    public EditNavFragment(int s){
        start = s;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_nav, container, false);
        mAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.pager2);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(start);

        dots = (ImageView) view.findViewById(R.id.light_dot);
        lp= new RelativeLayout.LayoutParams(dots.getLayoutParams());
        density = getActivity().getResources().getDisplayMetrics().density;


        return view;
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
        public void startUpdate(ViewGroup container){
            super.finishUpdate(container);
            int i = mPager.getCurrentItem();
            switch(i) {
                case (0):
                    lp.setMargins((int)(DONE*density),0, 0, 0);
                    dots.setLayoutParams(lp);
                    return;
                case(1):
                    lp.setMargins((int)(LYRIC*density), 0, 0, 0);
                    dots.setLayoutParams(lp);
                    return;
                case(2):
                    lp.setMargins( (int)(CROP*density), 0, 0, 0);
                    dots.setLayoutParams(lp);
                    return;
                case(3):
                    lp.setMargins((int)(GAIN*density), 0, 0, 0);
                    dots.setLayoutParams(lp);
                    return;
                case(4):
                    lp.setMargins((int)(ECHO*density), 0, 0, 0);
                    dots.setLayoutParams(lp);
                    return;

            }

        }
    }




//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        String getTime();
    }

    public String getTime() {
        return mListener.getTime();
    }
}
