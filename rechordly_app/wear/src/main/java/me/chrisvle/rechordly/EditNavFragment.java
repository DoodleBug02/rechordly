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
        dots = (ImageView) view.findViewById(R.id.dots);

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
                    dots.setImageResource(R.drawable.dot1);
                    return;
                case(1):
                    dots.setImageResource(R.drawable.dot2);
                    return;
                case(2):
                    dots.setImageResource(R.drawable.dot3);
                    return;
                case(3):
                    dots.setImageResource(R.drawable.dot4);
                    return;
                case(4):
                    dots.setImageResource(R.drawable.dot5);
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
