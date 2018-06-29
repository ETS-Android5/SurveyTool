package com.hkm.oc.job.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.U.Tool;
import com.hkm.api.handlers.PicTransformMegaPixel;
import com.hkm.oc.R;
import com.hkm.oc.job.JobTaskActivity;
import com.hkm.oc.job.SitePhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.hkm.U.Constant.IntentKey.WPP_INTENT;
import static com.hkm.U.Content.current_job_task;

/**
 * Created by Hesk on 29/5/2014.
 */
public class PhotoView {
    public static String TAG = "PHOTOVIEW";
    public TextView textView;
    public ImageView image;

    private int mBackgroundColor, pos;
    private String mText;
    private Context ctx;
    private Uri mUri;

    public PhotoView(Context ctx, View Convertedview, int position) {
        this.ctx = ctx;
        this.mBackgroundColor = 15;
        this.image = (ImageView) Convertedview.findViewById(R.id.photo_view_image_apply);
        this.textView = (TextView) Convertedview.findViewById(R.id.photo_view_description);
        this.update(position);
    }

    /**
     * @return the backgroundColor
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    protected File getImageFile() {
        String name_uri = Tool.getRealPathFromURI(ctx, mUri);
        return new File(name_uri);
    }

    protected void show_image() {
        try {
            Picasso.with(ctx)
                    .load(getImageFile())
                    .fit().centerCrop()
                    .placeholder(R.drawable.alpha_1callpower)
                    .transform(new PicTransformMegaPixel(100000))
                    .error(R.drawable.ic_alerts_and_states_error_big)
                    .into(image);
        } catch (Exception e) {
            Tool.trace(ctx, e.getMessage());
        }
        // .resize(300, 500)
        // .centerCrop()
    }


    /**
     * @return the text
     */
    public String getText() {
        return mText;
    }

    public void renderView() {
        show_image();
        this.textView.setText(mText);
        // convertView.setBackgroundColor(getItem(position).getBackgroundColor());
    }

    public PhotoView update(int position) {
        this.pos = position;
        this.mUri = current_job_task.getPhotoList().get(position).uri;
        this.mText = current_job_task.getPhotoList().get(position).description;
        return this;
    }

    public void onEdit() {
        Intent slnt = new Intent(ctx, SitePhotoView.class);
        Bundle b = new Bundle();
        b.putString("caption", this.mText);
        b.putInt("position", this.pos);
        slnt.putExtras(b);
        slnt.putExtra("i_uri", mUri);
        // ctx.startActivity();
        ((JobTaskActivity) ctx).startActivityForResult(slnt, WPP_INTENT);
    }
}
