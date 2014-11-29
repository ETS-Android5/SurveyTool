package com.hkm.Application;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

import com.hkm.api.OCHttpClient;
import com.hkm.oc.R;
import com.hkm.oc.wv.online_view;

import java.util.List;

public class OCPreference extends PreferenceActivity {
    public static String TAG = "AccountPreferences";

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.app_end_new, R.anim.app_end_old);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.app_start_new, R.anim.app_start_old);
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        // Add a button to the header list.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class PrefsAccountInfoFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //    PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_account_info);
            Preference button = (Preference) findPreference("loginbutton");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    //code for what you want it to do
                    OCHttpClient.login(getActivity());
                    return true;
                }
            });
        }
    }

    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first preferences fragment.
     */
    public static class Prefs1FragmentInner extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Can retrieve arguments from preference XML.
            Log.i(TAG, "Arguments: " + getArguments());
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_01);
        }
    }

    /**
     * This fragment shows the preferences for the second header.
     */
    public static class PrefsDrawMapApplicationSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Can retrieve arguments from headers XML.
            Log.i(TAG, "Arguments: " + getArguments());
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_drawmap);
        }
    }
    /**
     * This fragment shows the preferences for the second header.
     */
    public static class PrefsNotificationsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Can retrieve arguments from headers XML.
            Log.i(TAG, "Arguments: " + getArguments());
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_notifications);
        }
    }
    /**
     * This fragment shows the preferences for the second header.
     */
    public static class PrefsNewWebView extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent slnt;
            Bundle sDnt;
            slnt = new Intent(getActivity(), online_view.class);
            slnt.putExtra("customExtra", "Something that I used to know");
            startActivity(slnt);
            getActivity().finish();
        }
    }

}