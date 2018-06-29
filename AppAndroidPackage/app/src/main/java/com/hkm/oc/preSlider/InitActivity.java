package com.hkm.oc.preSlider;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.U.Constant;
import com.hkm.oc.R;
import com.hkm.oc.preF.f.Drumb;
import com.hkm.oc.preF.f.MainControl;
import com.hkm.oc.preF.f.ShowControl;

import java.util.Locale;
public class InitActivity extends FragmentActivity {
    public final static String[] App_Titles = {
            "my flows",
            "drumb practice",
            "reading",
            "my schedule"
    };
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replced with any action code
    }

    public void pickPicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replced with any action code
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected_id = item.getItemId();
        switch (selected_id) {
            case R.id.action_plot_map_native:
                break;
            case R.id.action_add_site_pictures:
                pickPhoto();
                break;
            case R.id.action_add_pictures_from_camera:
                pickPicture();
                break;
            case R.id.action_plot_map_js:
                //Intent jschart = new Intent(getApplicationContext(), PlotJS.class);
               // startActivity(jschart);
                break;
            default:
                Log.d(Constant.DETAG, "selected_id: " + selected_id);
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = null;
            Bundle args = new Bundle();
            String tag = Constant.ARG_SECTION_NUMBER;
            Log.d(tag, position + "");
            switch (position) {
                case 0:
                    fragment = new Drumb();
                    break;
                case 1:
                    fragment = new Drumb();
                    break;
                case 2:
                    fragment = new ShowControl();
                    args.putInt(ShowControl.ARG_POSITION, 0);
                    break;
                default:
                    fragment = new MainControl(R.layout.fragment_main_dummy);
                    break;
            }
            try {
                args.putInt(tag, position + 1);
                fragment.setArguments(args);
                fragment.getView().setBackgroundColor(Constant.NormalBackGroundColor);
            } catch (Exception e) {
                Log.d(tag, e.toString());
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            String title = App_Titles[position];
            return title.toUpperCase(l);
            //return getString(R.string.title_section2).toUpperCase(l);
        }
    }




}
