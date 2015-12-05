package me.chrisvle.rechordly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaybackFragment extends Fragment {

    private ImageButton play_btn;
    private ImageButton pause_btn;
    private RelativeLayout parent;


    public PlaybackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_playback, container, false);
        play_btn = (ImageButton) view.findViewById(R.id.play_btn);
        pause_btn = (ImageButton) view.findViewById(R.id.pause_btn);
        parent = (RelativeLayout) view.findViewById(R.id.play_screen);

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Playback Activity", "Play Request frag");
                Intent intent = new Intent("/pause");
                getActivity().sendBroadcast(intent);
                pause_btn.setVisibility(View.INVISIBLE);
                play_btn.setVisibility(View.VISIBLE);


                //FIXME @Jeremy add play music service logic

            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Playback Activity", "Play Request frag");
                Intent intent = new Intent("/play");
                getActivity().sendBroadcast(intent);
                play_btn.setVisibility(View.INVISIBLE);
                pause_btn.setVisibility(View.VISIBLE);

                //FIXME @Jeremy add pause music service logic

            }
        });

        return view;
    }



}
