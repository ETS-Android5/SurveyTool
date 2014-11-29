package com.hkm.listviewhkm;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.hkm.datamodel.DataHandler;
import com.hkm.datamodel.JData;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;
import com.hkm.listviewhkm.base.LabelBase;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.oc.preF.f.NewLegendListFragment;
import com.hkm.oc.preF.root.FPAManager;
import com.hkm.oc.preF.transitions.ZoomInOutViewPagerTransformer;

import java.util.ArrayList;

import static com.hkm.U.Content.current_sketch_map;

/**
 * Created by Hesk on
 */
public class LabelIntent extends LabelBase {

    /**
     * Add Tab content to the Tabhost
     *
     * @param activity
     * @param tabHost
     * @param tabSpec
     */
    private static void AddTab(LabelIntent activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /**
     * everything is start from here.
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasChanges = false;
        //=== Inflate the layout
        setContentView(R.layout.label_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //=== Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            //set the tab as per the saved state
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        //=== find the label =====
        final int node_index = getIntent().getExtras().getInt("node_index", -99999);
        //=== Initialise ViewPager
        final RouteNode n = current_sketch_map.get_route_node_at(node_index);
        intializeViewPager(n);
        intializeOtherInfo(n);
        onCreateSpinner();
        addNumbericPicker();
        label_display_field = (TextView) findViewById(R.id.label_display);
        display_label_but = (Button) findViewById(R.id.label_display_control_button);
        display_label_but.setOnClickListener(this);

        if (node_index > -1) {
            addLineFactory(node_index);
            //display_loaded_data(node_index);
        } else {
            Tool.trace(this, "Error there is no data to display in here");
        }
    }

    /**
     * display the loaded data here
     */
    private void display_loaded_data(int record_plan_index) {

        //initialization on the list picker
        // final int record_plan_index = getIntent().getExtras().getInt("listid", -99999);
       /*
            try {
                final String[] path = DataHandler.get_listing_path(record_plan_index);
                final int index_intrinsic = DataHandler.get_label_letter_intrinsic(record_plan_index);


                if (DataHandler.isSharp(index_intrinsic)) {
                    final NewLegendListFragment LOF = (NewLegendListFragment) fragmentsList.get(NewLegendListFragment.roleFeature);
                    LOF.take_list_render(
                            DataHandler.get_list_from_letter_by_ref_id(Integer.parseInt(path[0]), this), 2).refresh();
                }


            } catch (Exception e) {
                Tool.trace(this, "start here cannot render the list now");
            }
        */
    }

    /**
     * needs to be run one time to setup the labeling view
     * execute once on the first time
     * init new data on the row that is just initialized
     */
    private void addLineFactory(int record_plan_index) {
        mLineFactory = new LineFactory();
        final Label mlabelcurrent = current_sketch_map.get_label_at(record_plan_index);
        if (mlabelcurrent != null) {
            //update and display the numeric label here
            mLineFactory
                    .set_record_plan_index(record_plan_index)
                    .updateI(mlabelcurrent.get_letterIntrinsic())
                    .updateL(mlabelcurrent.get_dLetter())
                    .updateN(mlabelcurrent.get_dNumeric())
                    .update_line_I(mlabelcurrent.get_line_label_int());

            mLineFactory.invalidate();
        } else {
            mLineFactory
                    .set_record_plan_index(record_plan_index)
                    .invalidate();
        }
    }

    /**
     * this is the interaction when the selection of the line or feature is taken and occured
     *
     * @param index_id
     * @param Role
     */
    @Override
    public void onPickList(int index_id, int Role) {
        if (Role == NewLegendListFragment.roleFeature) {
            Tool.trace(this, "Feature # selected ID: " + index_id);
        } else if (Role == NewLegendListFragment.roleLine) {
            if (mLineFactory.getLetterIntrinsic() != index_id) {
                Tool.trace(this, "Line ID selected: " + index_id);
                final NewLegendListFragment LOF = (NewLegendListFragment) fragmentsList.get(NewLegendListFragment.roleLine);
                LOF.refresh();
            } else {
                //this happens when the first selection of line is pressed and change the tab into the FEATURE TYPE tab.
                Tool.trace(this, "Same ID found: " + index_id);
                final NewLegendListFragment LOF = (NewLegendListFragment) fragmentsList.get(NewLegendListFragment.roleFeature);
                LOF.take_list_render(DataHandler.get_list_from_letter_by_ref_id(index_id, this), -1).refresh();
                this.mTabHost.setCurrentTab(NewLegendListFragment.roleFeature);
            }
        }
        hasChanges = true;
        mLineFactory.updateMechanicallyByIntrinsicN(index_id).invalidate();
    }

    /**
     * setup the numeric picker on the intent
     * execute once
     */
    private void addNumbericPicker() {
        FragmentActivity fm = (FragmentActivity) this;
        mNumberPickerBuilder = new NumberPickerBuilder()
                .setFragmentManager(fm.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        // .setReference(pos)
                .setDecimalVisibility(View.VISIBLE)
                .setMinNumber(0)
                .addNumberPickerDialogHandler(this);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
    }

    /**
     * the setup of the spinner is created here with a list of previous set data rows if there is nothing read from the previous data.
     * No spinner should be shown on the view.
     * <p/>
     * execute once
     * onCreateSpinner
     */
    private void onCreateSpinner() {

        String[] list_data;
        areaspinner = (Spinner) findViewById(R.id.areaspinner);

        try {
            list_data = JData.get_spinner_options();
        } catch (Exception e) {
            System.out.print(e.toString());
            list_data = new String[]{
                    "No Selection"
            };
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_data);

        //array you are populating
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        areaspinner.setAdapter(adapter2);
        areaspinner.setOnItemSelectedListener(this);
        areaspinner.setSelection(0);

    }

    private void intializeOtherInfo(RouteNode rn) {
        TextView v = (TextView) findViewById(R.id.radius_bar);
        v.setText(rn.get_cable_radius() + " (cm)");
    }

    /**
     * Initialise ViewPager
     */
    private void intializeViewPager(final RouteNode N) {
        Label L = N.get_label();
        if (L != null) {
            final int line_label_int = L.get_line_label_int();
            final int selection_line = line_label_int - START_LINE_INDEX;
            final int selection_sharp = L.is_sharp() ? L.get_letterIntrinsic() : 0;
            intializeVP(selection_line, selection_sharp);
        } else {
            intializeVP(0, 0);
        }
    }

    private void intializeVP(final int selection_line, final int selection_sharp) {
        fragmentsList = new ArrayList<Fragment>();
        Fragment linety = NewLegendListFragment.newInstance(selection_line, START_LINE_INDEX, END_LINE_INDEX, 0);
        Fragment featurety = NewLegendListFragment.newInstance(selection_sharp, START_SHARP_INDEX, END_SHARP_INDEX, 1);
        //Fragment friendsFragment = Drumb.newInstance("Hello Friends.");
        //Fragment chatFragment = TestFragment.newInstance("Hello Chat.");
        fragmentsList.add(linety);
        fragmentsList.add(featurety);
        //fragmentsList.add(friendsFragment);
        //fragmentsList.add(chatFragment);
        this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(new FPAManager(getFragmentManager(), fragmentsList));
        this.mViewPager.setOffscreenPageLimit(3);
        this.mViewPager.setCurrentItem(0);
        this.mViewPager.setOnPageChangeListener(this);
        this.mViewPager.setPageTransformer(true, new ZoomInOutViewPagerTransformer());
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        LabelIntent.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(tabs_holder[0]), (tabInfo = new TabInfo("Tab1", NewLegendListFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        LabelIntent.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(tabs_holder[1]), (tabInfo = new TabInfo("Tab2", NewLegendListFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        //   LabelIntent.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(tabs_holder[2]), (tabInfo = new TabInfo("Tab3", Drumb.class, args)));
        //   this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {

    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        // text.setText("Number: " + number + "\nDecimal: " + decimal + "\nIs negative: " + isNegative + "\nFull number: "  + fullNumber);
        mLineFactory.updateN(number).invalidate();
        hasChanges = true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.label_display_control_button) {
            mNumberPickerBuilder.show();
        }
    }

    /**
     * when the first occur and the selection..
     * the number of initialization should be according to how many tabs does it have for each list.
     * this is the selection from the spinner
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (i > 0) {
            Log.d("label intent", view.toString());
          /*
            final String[] c = JData.

            final String[] k = DataHandler.get_previous_list_item_selected(i);

            mLineFactory
                    .updateN(Integer.parseInt(k[2]))
                    .updateMechanicallyByIntrinsicN(Integer.parseInt(k[1]))
                    .invalidate();

            if (!mLineFactory.datawrite_by_other_ref_radius(i)) {
                Tool.trace(this, "failed from updating data content");
            } else {
                Tool.trace(this, "data content updated");
            }*/
    /*        System.out.println(l);
            System.out.println(i);*/
        } else {


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        this.mTabHost.setCurrentTab(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String s) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onBackPressed() {
        home_button_press();
    }

    public void home_button_press() {
        if (mLineFactory.exitenable()) {
            final Intent i = new Intent();
            mLineFactory.save();
            if (hasChanges) {
                i.putExtras(mLineFactory.dataWrite()); // -nothing will be used
                setResult(RESULT_OK, i); // -result okay
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Intent myIntent = new Intent(getApplicationContext(), MyActivity.class);
        // startActivityForResult(myIntent, 0);
        switch (item.getItemId()) {
            case android.R.id.home:
                home_button_press();
                break;
        }
        return true;

    }

}
