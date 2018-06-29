package com.hkm.root;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

public class BaseProvider extends ActionProvider {

    protected final Context context;
    protected final int layout;
    protected final BaseProvider self;
    protected View view;
    protected int positionLeft = 0;

    //protected Dropdown dropdown;
    public BaseProvider(Context context, int layout) {
        // public BaseProvider(Context context, int layout, Dropdown dropdown) {
        super(context);
        this.layout = layout;
        this.context = context;
        this.self = this;
        //   this.dropdown = dropdown;
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(this.layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.onItemClick();
            }
        });
        this.view = view;
        return view;
    }

    public boolean onItemClick() {
        toggleDropdown();
        return true;
    }

    protected void toggleDropdown() {
        this.positionLeft = getRelativeLeft(view);
        // DropdownInflater.getInstance().toggleDropdown(this.dropdown,this.positionLeft);
    }

    protected int getRelativeLeft(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc[0];
    }
}