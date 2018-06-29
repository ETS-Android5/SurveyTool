package com.hkm.oc.job;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Hesk ons
 */
public class ListBaseMaps extends ListActivity {
    public ListBaseMaps(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        // ((ListTestingsResult)getApplication()).setActiveActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    /*when this activity is closing*/
    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("returnKey1", "intend done");
        data.putExtra("returnKey2", "You could be better then you are. ");
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }
}
