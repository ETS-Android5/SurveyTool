package com.hkm.surveytool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.hkm.surveytool.Element.Dot;
import com.hkm.surveytool.Element.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesk ons
 * Take on handling data of the list
 */
public class DataHandler {
    /**
     * simply just break down the list row into an array
     *
     * @param position
     * @return
     */
    private static String[] break_down(int position) {
        return AppDataRetain.radius_list.get(position).split(",");
    }

    /**
     * simply just break down the reference into an array
     *
     * @param position_index
     * @return
     */
    private static String[] break_down_ref(int position_index) {
        return Constant.MapData.InternalIconList[position_index].split(",");
    }





    public static String get_string_label_for_drawings(String data) {
        /**
         * the row reference field will be composed with
         *
         *  [4]+[3]
         *
         * if index 4 => G3
         * then 4 = G for display
         *
         * if index 3 => 34
         * then 3 = 34 for display
         *
         * if # will be interpreted separately
         *
         * */
        final String[] a = data.split(",");
        if (a[3].length() > 0 && a[4].length() > 0) {
            final String letter = Character.toString(a[4].charAt(0));
            final int letter_number_intrinsic = Integer.parseInt(a[4].substring(1));
            final String number_label = a[3];
            return letter + number_label + sharpOut(letter_number_intrinsic);
        } else {
            return Constant.ROW_NOT_COMPLETE_TEXT;
        }
    }


    public static String get_1_item(String r) {
        final String[] larry = r.split(",");
        return larry[0];
    }

    public static String get_2_item(String r) {
        final String[] larry = r.split(",");
        return larry[1];
    }




    public static String get_label_numeric(int position) {
        final String[] a = break_down(position);
        String out = "";
        final String i = a[3];
        Log.d("get_label_numeric", "i length: " + i.length());
        if (i.length() == 0 || i.equalsIgnoreCase("null")) {
            out = "";
        } else {
            final int st = i.lastIndexOf("#");
            if (st > -1) {
                //TODO: maybe there is a bug
                out = i.substring(0, st);
            } else {
                out = i;
            }
        }
        return out;
    }

    /**
     * getting the letter character only from the testing data row by the index position
     *
     * @param position
     * @return
     */
    public static String get_label_letter(int position) {
        final String[] a = break_down(position);
        final String t = a[4];
        if (t.length() > 0 && !t.equalsIgnoreCase("nullnull")) {
            return Character.toString(a[4].charAt(0));
        } else {
            Log.d("datahandler", "there is no letter defined here");
            return "";
        }
    }


    /**
     * getting the Letter from the reference data list by the index id
     *
     * @param index_id
     * @return
     */
    public static String get_letter_reference_by_index_id(int index_id) {
        final String text = Constant.MapData.InternalIconList[index_id];
        final String[] f = text.split(",");
        return f[3];
    }

    /**
     * this is the determine whether or not the reference data list is coming with the # sign
     *
     * @param index_id
     * @return
     */
    public static boolean isSharp(int index_id) {
        //only for 0 - 39
        if (index_id >= 0 && index_id <= 39) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * letter output for checking the reference is # or not
     *
     * @param index_id
     * @return
     */
    public static String sharpOut(int index_id) {
        return isSharp(index_id) ? "#" : "";
    }

    /**
     * getting the Letter from the reference data list by the index id
     * <p/>
     * list type detail:
     * <p/>
     * 0. legend icon id name
     * 1. description text
     * 2. legend type
     * 3. type
     * 4. color code id name
     * <p/>
     * 5. THE MARKER LINE LABEL
     *
     * @param letter_num_intrinsic
     * @return
     */
    public static String get_label_color_instrinsic(int letter_num_intrinsic) {
        final String[] a = break_down_ref(letter_num_intrinsic);
        return a[4];
    }

    public static String get_line_label_by_instrinsic(int letter_num_intrinsic) {
        /**
         *  exception out of bound may occur if isSharp is false
         */
        final String[] a = break_down_ref(letter_num_intrinsic);
        return a[5];
    }

    /**
     * this is the part from the Spinner selection
     */
    public static String[] get_previous_labels() {
        final int i = AppDataRetain.radius_list.size();
        if (i > 0) {
            String[] f = new String[i + 1];
            f[0] = "Replace by ref. point..";
            for (int k = 1; k <= i; k++) {
                final String c = get_string_label_for_drawings(AppDataRetain.radius_list.get(k - 1));
                if (!c.equals(Constant.ROW_NOT_COMPLETE_TEXT)) {
                    f[k] = c;
                }
            }
            return f;
        } else {
            System.out.print("big problems in here");
            return null;
        }
    }


    public static boolean result_update_from_the_previous_item(int real_choice, int target_index) {
        if (real_choice >= 0) {
            final String loaded = AppDataRetain.radius_list.get(real_choice);
            AppDataRetain.radius_list.set(target_index, loaded);
            return true;
        } else
            return false;
    }

    public static List<Integer> getlegendindex(Route r) {
        int cur = r.size();
        List<Integer> v = new ArrayList<Integer>();
        int found = 0;
        for (int i = 0; i < cur; i++) {
            Dot d = r.getListDot(i);
            int intrinsic = d.get_letter_intrinsic();
            int fnd = v.indexOf(intrinsic);
            if (fnd == -1) {
                v.add(intrinsic);
                found++;
            }
        }
        System.out.print(v);
        return v;
        //ArrayUtils.toPrimitive(v.toArray(new Integer[v.size()]));
    }

    /**
     * getting the Letter from the reference data list by the index id
     *
     * @param big_pick_id handle the base id from the selected reference in the <line>
     * @param ctx         handle the basecontext from the system
     * @return
     */

    public static ArrayList<Bundle> get_list_from_letter_by_ref_id(int big_pick_id, Context ctx) {
        final int after_this_will_be_line = 39;
        final String check_letter = get_letter_reference_by_index_id(big_pick_id);
        final ArrayList<Bundle> bundle_listing = new ArrayList<Bundle>();
        for (int i = 0; i < Constant.MapData.InternalIconList.length; i++) {
            final String k = get_letter_reference_by_index_id(i);
            if (k.equalsIgnoreCase(check_letter) && i < after_this_will_be_line) {
                Bundle vb = new Bundle();
                String[] bk = break_down_ref(i);
                vb.putString("title", "(" + bk[3] + ") " + bk[1]);
                vb.putString("letter", k);
                vb.putString("raw", Constant.MapData.InternalIconList[i]);
                vb.putInt("nid", i);
                vb.putParcelable("icon", getBitmapFromDataRefSourceId(i, ctx));
                bundle_listing.add(vb);
            }
        }
        return bundle_listing;
    }

    public static ArrayList<Bundle> get_tab1_list_content(Context ctx) {
        final int after_this_will_be_line = 39;
        final ArrayList<Bundle> bundle_listing = new ArrayList<Bundle>();
        for (int i = 0; i < Constant.MapData.InternalIconList.length; i++) {
            if (i > after_this_will_be_line) {
                Bundle vb = new Bundle();
                String[] bk = break_down_ref(i);
                vb.putString("title", "(" + bk[3] + ") " + bk[1]);
                vb.putString("letter", bk[3]);
                vb.putString("raw", Constant.MapData.InternalIconList[i]);
                vb.putInt("nid", i);
                vb.putParcelable("icon", getBitmapFromDataRefSourceId(i, ctx));
                bundle_listing.add(vb);
            }
        }
        return bundle_listing;
    }

    public static Bitmap getBitmapFromDataRefSourceId(int index, Context ctx) {
        String[] bk = break_down_ref(index);
        final String png_filename = bk[0];
        final int g = ctx.getResources().getIdentifier(png_filename, "drawable", ctx.getPackageName());
        //        BitmapDrawable bd = (BitmapDrawable) ctx.getResources().getDrawable(g);
        Bitmap b = BitmapFactory.decodeResource(ctx.getResources(), g);
        return b;
    }


}
