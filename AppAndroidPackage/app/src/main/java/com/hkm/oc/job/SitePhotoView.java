package com.hkm.oc.job;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.U.Tool;
import com.hkm.oc.R;
import com.hkm.oc.pre.BaseLayerAppearance;
import com.hkm.root.Dialog.DialogLabelDescription;
import com.squareup.picasso.Picasso;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hesk on 5/31/2014.
 */
public class SitePhotoView extends BaseLayerAppearance implements View.OnClickListener {
    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private ImageButton btn;
    private String labelcaption;
    private int position;
    private TextView mtextView;
    private String displaytext;
    private String pretag;

    @Override
    protected void go_back_to_main_screen() {

    }

    @Override
    protected void postOnCreate() {
        //   Bundle v = this.getIntent().getExtras();
        Intent in = this.getIntent();
        Uri u = (Uri) in.getParcelableExtra("i_uri");
        labelcaption = (String) in.getExtras().getString("caption", "");
        position = in.getExtras().getInt("position", -1);
        displaytext = "Figure. " + position;
        pretag = Content.current_job_task.getPhotoList().get(position).tag;
        String realname = Tool.getRealPathFromURI(this, u);
        File loadfile = new File(realname);
        setContentView(R.layout.single_view_pv);
        // Any implementation of ImageView can be used!
        mImageView = (ImageView) findViewById(R.id.iv_photo);
        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.with(this).load(loadfile).centerInside().fit().placeholder(R.drawable.logo_ibike_).error(R.drawable.ic_pin).into(mImageView);

        btn = (ImageButton) findViewById(R.id.bn_change_description);
        btn.setOnClickListener(this);
        mtextView = (TextView) findViewById(R.id.photo_view_description);
        label_refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_change_description:
                edit_label();
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        final String DTag = dialog.getTag();

        if (DTag.equalsIgnoreCase("sitephotolabel")) {
            DialogLabelDescription m = (DialogLabelDescription) dialog;
            String txt = m.getInformationEntered();
            labelcaption = txt;
            Content.current_job_task.getPhotoList().get(position).setlabel(txt);
            label_refresh();
        }

    }

    public void label_refresh() {
        mtextView.setText(pretag + " " + labelcaption);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {
       /* final String DTag = dialog.getTag();
        if (DTag.equalsIgnoreCase("sitephotolabel")) {
            DialogLabelDescription m = (DialogLabelDescription) dialog;
            m.clearField();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.site_photo_view_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.remove_site_pic:
                remove_n_finish(i);
                break;
            case R.id.edit_label:
                edit_label();
                break;
            case R.id.edit_tag:
                edit_tag();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void edit_tag() {

        /*String[] list_data;
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
*/
        final String[] items = mFragmentActivity.getResources().getStringArray(R.array.photo_terms);
        new AlertDialog.Builder(mFragmentActivity)
                .setTitle("Select a Tag")
                .setIcon(R.drawable.ic_tag_74_256)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int w) {

                        //   int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button
                        // String preTag = items[selectedPosition];
                        final String tag = items[w];
                        Content.current_job_task.getPhotoList().get(position).setTag(tag);
                        pretag = tag;
                        label_refresh();

                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                        // .setSingleChoiceItems(items, 0, null)
              /*  .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button
                        // String preTag = items[selectedPosition];
                        PhotoViewData d = Content.current_job_task.getPhotoList().get(position);
                        d.tag = new String(items[selectedPosition]);
                    }
                })*/
                .show();

    }

    private void edit_label() {
        try {
            final DialogLabelDescription dialog = DialogLabelDescription.newInstance(labelcaption, displaytext);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "sitephotolabel"); //with this tag name
        } catch (Exception e) {
            Tool.trace(this, e.toString());
        }
    }

    private void remove_n_finish(Intent i) {
        i = getIntent();
        Bundle b = new Bundle();
        b.putInt("remove_pos", position);
        i.putExtras(b);
        setResult(Constant.IntentAction.REMOVE, i);
        finish();
    }
}
