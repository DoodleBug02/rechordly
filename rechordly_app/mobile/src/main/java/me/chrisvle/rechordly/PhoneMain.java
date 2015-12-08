package me.chrisvle.rechordly;

//import android.app.ActionBar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.chrisvle.rechordly.dummy.DummyContent;

public class PhoneMain extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener,
                                                            MusicFragment.OnFragmentInteractionListener,
                                                            LyricsFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView rView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textview;
    private ImageView mPlus;
    private MyItemRecyclerViewAdapter mRAdapter;
    private SavedDataList savedData = SavedDataList.getInstance();
    BroadcastReceiver broadcastReceiver;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedData.getFromDisk(getApplicationContext());

        String[] keys = savedData.getNames();
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                Log.d("Key Name: ", "of " + keys[i]);
                DummyContent.addItem(new DummyContent.DummyItem(keys[i], savedData.getDuration(keys[i]), ""));
            }
        }

        DummyContent.addItem(new DummyContent.DummyItem("test1", "1234", ""));
        DummyContent.addItem(new DummyContent.DummyItem("test2", "1234", ""));
        savedData.addSong("12345678901234567890", "1", "1", "03:30", "None", "android.resource://" + getPackageName() + "/orchestra"); //+ R.raw.orchestra);
        DummyContent.addItem(new DummyContent.DummyItem("12345678901234567890", "03:40", ""));

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        toolbar.setBackground(getDrawable(R.drawable.purp_gradient));

        mPlus = (ImageView) toolbar.findViewById(R.id.plus_image);
        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMain = new Intent(v.getContext(), MessageSender.class);
                openMain.putExtra("START", "main");
                startService(openMain);
            }
        });

        rView = (RecyclerView) findViewById(R.id.view3);
        rView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(mLayoutManager);

        mRAdapter = new MyItemRecyclerViewAdapter(DummyContent.ITEMS, this);

        rView.setAdapter(mRAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rView.addItemDecoration(itemDecoration);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);


//        tabLayout = (TabLayout) findViewById(R.id.tabs);

//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setBackground(new ColorDrawable(Color.parseColor("#D6D6D6")));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#6F37FF"));
//        setBackgroundDrawable(new ColorDrawable("FF0000"));
//        tabLayout.setBackgroundColor(0xD6D6D6);
//        tabLayout.setSelectedTabIndicatorColor(0x6f37ff);

        IntentFilter filter = new IntentFilter();
        filter.addAction("/update_list");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("/update_list")) {
                    mRAdapter.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);

        textview = (TextView) toolbar.findViewById(R.id.mytext);
        Typeface font = Typeface.createFromAsset(toolbar.getContext().getAssets(), "font/Mission_Gothic_Bold.ttf");
        textview.setTypeface(font);
        Intent startPlaybackService = new Intent(this, PlaybackService.class);
        startService(startPlaybackService);

    }


    private void save(){

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemFragment(), "all");
//        adapter.addFragment(new MusicFragment(), "music");
//        adapter.addFragment(new LyricsFragment(), "lyrics");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.d("DUMMY INTERACTION", "");
        String infoName;
        if (item.id.length() > 12) {
            infoName = item.id.substring(0, 10);
            infoName = infoName.concat("...");
        } else {
            infoName = item.id;
        }
        Intent info = new Intent(getBaseContext(), InfoActivity.class);
        info.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        info.putExtra("file_name", item.id);
        info.putExtra("shown_name" , infoName);
        startActivity(info);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("DUMMY INTERACTION", "Lyrics");
    }

    @Override
    public void onMusicFragmentInteraction(Uri uri) {
        Log.d("DUMMY INTERACTION", "music");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

}
