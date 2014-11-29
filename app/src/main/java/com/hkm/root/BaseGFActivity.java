package com.hkm.root;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.U.Constant;
import com.hkm.oc.R;

/**
 * Created by hesk on 7/31/13.
 */
public class BaseGFActivity extends FragmentActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    // imageview.setImageURI(selectedImage);
                    Log.d(Constant.DETAG, "return result from code result_ok case 0");
                }
            break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    //imageview.setImageURI(selectedImage);
                    Log.d(Constant.DETAG, "return result from code result_ok case 1");
                }
            break;
        }
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
               // Intent i = new Intent(getApplicationContext(), PlotNative.class);
               // i.putExtra("key", "value");
                // Example of sending email to next screen as
                // Key = 'email'
                // value = 'myemail@gmail.com'
               // i.putExtra("email", "myemail@gmail.com");
              //  startActivity(i);
                break;
            case R.id.action_plot_map_js:
               // Intent jschart = new Intent(getApplicationContext(), PlotJS.class);
               // startActivity(jschart);
                break;
            case R.id.action_add_site_pictures:
                pickPhoto();
                break;
            case R.id.action_add_pictures_from_camera:
                pickPicture();
                break;
            case R.id.action_make_report:
                break;
            default:
                Log.d(Constant.DETAG, "selected_id: " + selected_id);
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
