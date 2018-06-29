package com.hkm.oc.panel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.oc.R;

/**
 * Created by hesk on 6/14/2014.
 */

public class RowItem {

    public static enum RowItemType {
        BIG, NORMAL, SPECIAL
    }

    private String title;
    private int icon;
    private RowItemType type;
    private int pos;
    private View cv;

    public RowItem(String title, int icon, RowItemType rt) {
        this.title = title;
        this.icon = icon;
        this.type = rt;
    }

    public RowItem update(int position) {
        this.pos = position;
        return this;
    }

    public RowItem setView(View view) {
        this.cv = view;
        return this;
    }

    public void renderView() {
        ImageView imgIcon = (ImageView) this.cv.findViewById(R.id.icon);
        imgIcon.setImageResource(icon);
        switch (type) {
            case BIG:
                break;
            case NORMAL:
                TextView txtTitle = (TextView) this.cv.findViewById(R.id.title);
                txtTitle.setText(title);
                break;
            default:
                break;
        }

    }

    public int getLayout() {
        switch (type) {
            case BIG:
                return R.layout.datalist_menudrawer_big;
            case NORMAL:
                return R.layout.datalist_menudrawer_item;
            default:
                return 0;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public RowItemType getType() {
        return type;
    }
}