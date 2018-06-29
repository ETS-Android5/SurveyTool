package com.hkm.oc.job.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.datamodel.LocationMap;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.R;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.hkm.U.Constant.BASEMAPPath;

/**
 * Created by Hesk on 24/6/2014.
 */
public class DrawMapFragment extends Fragment implements View.OnClickListener {
    private static Picasso picasso;
    private int mNum;
    private LocationMap mlocation;
    private SketchMapData sketchdata;
    private Button v2button;
    private View display_hide;

    public DrawMapFragment() {
    }

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */

    public static DrawMapFragment newInstance(int num, LocationMap lm, SketchMapData sd) {
        DrawMapFragment f = new DrawMapFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("url_stirng", lm.get_url());
        args.putInt("attachment_id", lm.get_attachment_id());
        f.mlocation = lm;
        try {
            f.sketchdata = sd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        f.setArguments(args);
        return f;
    }

    public static DrawMapFragment newInstance(int num, LocationMap lm) {
        DrawMapFragment f = new DrawMapFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("url_stirng", lm.get_url());
        args.putInt("attachment_id", lm.get_attachment_id());
        f.mlocation = lm;
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity mactivity) {
        super.onAttach(mactivity);
        File cacheDir = new File(BASEMAPPath);
        // get external cache dir from context and append unique folder
        picasso = new Picasso.Builder(mactivity).downloader(new OkHttpDownloader(cacheDir)).build();
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("attachment_id") : 1;
    }

    private void buttonSet(boolean display_bool) {
        if (!display_bool) {
            display_hide.setVisibility(View.GONE);
        } else {
            display_hide.setVisibility(View.VISIBLE);
            display_hide.bringToFront();
        }
    }

    public void test_display() {
        if (sketchdata == null) {
            buttonSet(false);
        } else {
            buttonSet(sketchdata.hasUri());
        }
    }


    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     * Display the small fragment inside each field and box
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_basemap, container, false);
        View tv = v.findViewById(R.id.text);
        String s = this.getResources().getString(R.string.assigned_base_map);
        ((TextView) tv).setText(String.format(s, mNum));
        ImageView vi = (ImageView) v.findViewById(R.id.location_map_view);
        v2button = (Button) v.findViewById(R.id.review_image);
        display_hide = v.findViewById(R.id.button_display_1);
        v2button.setOnClickListener(this);
        buttonSet(false);
        if (sketchdata.hasUri()) {
            picasso.with(getActivity()).load(sketchdata.get_draw_map_uri()).fit().centerCrop().placeholder(R.drawable.alpha_1callpower).error(R.drawable.ic_alerts_and_states_error_big).into(vi);
        } else {
            picasso.with(getActivity()).load(mlocation.get_url()).fit().centerCrop().placeholder(R.drawable.alpha_1callpower).error(R.drawable.ic_alerts_and_states_error_big).into(vi);
        }
        display_hide.bringToFront();
        test_display();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            /*      setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
             */
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.review_image) {
            Uri imagelocation = sketchdata.get_draw_map_uri();
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(imagelocation, "image/png");
            startActivity(intent);
        }
    }
}
