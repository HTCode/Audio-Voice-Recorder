package com.rsma.audiovoicerecorder.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.rsma.audiovoicerecorder.AdmobHelper;
import com.rsma.audiovoicerecorder.PermissionsHelper;
import com.rsma.audiovoicerecorder.R;
import com.rsma.audiovoicerecorder.fragments.FileViewerFragment;
import com.rsma.audiovoicerecorder.fragments.RecordFragment;


public class MainActivity extends AppCompatActivity {
    private AdmobHelper adh;
    private PermissionsHelper prh = new PermissionsHelper(this, 100,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static final String TAG = MainActivity.class.getSimpleName();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;

    // ==========================================================================================
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        prh.onDenied(requestCode);
    }

    // ==========================================================================================
    @Override
    public void onBackPressed() {
        adh.confirmExitAd();
    }

    // ==========================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prh.requestPermissions();
        adh = new AdmobHelper(this,
                "ca-app-pub-3940256099942544~3347511713",
                "ca-app-pub-3940256099942544/1033173712", R.id.adViewBanner);

        // --------------------------------------------------------
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        // +++++++++++++++++++++++++++++++++
        final LinearLayout lila = (LinearLayout) tabs.getChildAt(0);
        tabs.setBackgroundColor( Color.WHITE );

        TextView tv0 = (TextView) lila.getChildAt(0);
        TextView tv1 = (TextView) lila.getChildAt(1);
        tv0.setBackgroundColor( getResources().getColor(R.color.white) );
        tv0.setTextColor( getResources().getColor(R.color.primary) );
        tv1.setBackgroundColor( getResources().getColor(R.color.primary_light) );
        tv1.setTextColor( getResources().getColor(R.color.whiteText) );

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i < lila.getChildCount(); i++){
                    TextView tv = (TextView) lila.getChildAt(i);
                    if(i == position){
                        tv.setBackgroundColor( getResources().getColor(R.color.white) );
                        tv.setTextColor( getResources().getColor(R.color.primary) );
                    } else {
                        tv.setBackgroundColor( getResources().getColor(R.color.primary_light) );
                        tv.setTextColor( getResources().getColor(R.color.whiteText) );
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // +++++++++++++++++++++++++++++++++

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings)};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return RecordFragment.newInstance(position);
                }
                case 1: {
                    return FileViewerFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public MainActivity() {
    }
}
