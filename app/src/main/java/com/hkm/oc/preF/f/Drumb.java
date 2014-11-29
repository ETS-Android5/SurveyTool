package com.hkm.oc.preF.f;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hkm.U.Constant;
import com.hkm.oc.R;

import java.util.Random;

/**
 * Created by hesk on 7/26/13.
 */
public class Drumb extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final int xml_layout = R.layout.fragment_game_drumb;
    public static final String ARG_VIB_SWITCH = "viber_switch";
    public final static String[] assetSnd = {
            "blast_far1",
            "blast",
            "largeBlast_far1",
            "launch1"
    };
    public static boolean enable_viber = true;
    public static int time_span_vib = 100;
    public static SoundPool a_soundpool;
    private ImageButton lb, rb;
    private int current_hit_snd;
    private Vibrator vib;
    private MediaPlayer m;

    public Drumb() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(xml_layout, container, false);
        lb = (ImageButton) rootView.findViewById(R.id.leftbut);
        rb = (ImageButton) rootView.findViewById(R.id.rightbut);
        trigger_sound_system();
        return rootView;
    }
  /*  @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == findViewById(R.id.rightbut)) {
            current_btn = bt1;
        } else if (v == findViewById(R.id.leftbut)) {
            current_btn = bt2;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            current_btn.setBackgroundResource(R.drawable.bb2);
            platSnd();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            current_btn.setBackgroundResource(R.drawable.bb1);
        }
        return true;
    }*/

    private int randSound() {
  /*      List accountList = new ArrayList();
    for(int k=0;k < counter;k++){
            accountList.add(k, (String)flowCtx.getValueAt("transitId"+m));
        }*/

        Random r = new Random();
        int r_num = r.nextInt(3) + 1;
        return Integer.valueOf("soundID" + r_num);
    }

    private void feature_get_background_ran() {

    }

    private void feature_vibvib() {
        if (enable_viber) {
            vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(time_span_vib);
        }
    }

    public void feature_play_hit_sound(int assetSnd_position) {
        // Log.d(Constant.DETAG, assetSnd_position + "");
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
            }
            m = new MediaPlayer();

            String filename = assetSnd[assetSnd_position];
            //   String filename = "";
            //   Log.d(Constant.DETAG, filename + " - yahoo assetSnd_position assets");
            // InputStream is = manager.openFD(filename).createInputStream();
            //    m.setDataSource(is.getFD());
            AssetFileDescriptor descriptor = getActivity().getAssets().openFd(filename);

            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
            m.start();
        } catch (Exception e) {
            Log.d(Constant.DETAG, e.toString());
        }
    }

    public void trigger_listeners() {
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                feature_vibvib();
                // feature_play_hit_sound(3);
                feature_get_background_ran();
                //  Toast.makeText(Drumb.this,"ImageButton is clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                feature_vibvib();
                // feature_play_hit_sound(2);
                feature_get_background_ran();
                // Toast.makeText(Drumb.this, "ImageButton is clicked!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void trigger_sound_system() {
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        a_soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        a_soundpool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool sp, int i1, int i2) {
               /* loaded = true;
                target_sound_id = soundID1;*/
            }
        });

        // String t = " ";
        AssetManager manager = getActivity().getAssets();
        for (int l = 0; l < assetSnd.length; l++) {
            // t = "Num: " + l + "\n";
            String filenames = "assets/hitsound/" + assetSnd[l];
            //  Log.d(Constant.DETAG, filenames);
            // a_soundpool.load()
            // soundID1=a_soundpool.load(manager.openFd(filenames),1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        trigger_listeners();

        //if(args.getInt(ARG_VIB_SWITCH)!=null){

        //}
        if (args != null) {
            // Set article based on argument passed in
            // updateArticleView(args.getInt(ARG_POSITION));
        } else if (current_hit_snd != -1) {
            // Set article based on saved instance state defined during onCreateView
            // updateArticleView(mCurrentPosition);
        } else {

        }
    }
}
