package com.hkm.listviewhkm.holder;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hkm.listviewhkm.RNList.RNAdapter;
import com.hkm.listviewhkm.model.C;
import com.hkm.listviewhkm.model.Tool;

import static com.hkm.listviewhkm.model.C.inputtags;
import static com.hkm.listviewhkm.model.C.tag.ROW_NUMBER_SET_A;

/**
 * Created by hesk on 7/1/2014.
 */
public class Pattern extends ActionRowViewHolder {

    public Pattern(int p, Context c, RNAdapter ad) {
        super(p, c, ad);
    }

    @Override
    public void onClick(View view) {
        final C.tag feat = (C.tag) view.getTag();
        final boolean use_num_picker = inputtags.contains(feat);
        try {
            //  final int position_ini = H.pos;
            Log.d("START", "Get Pos at:" + currentStatus.toString() + " , p:" + pos);
            switch (feat) {
                case EDIT:
                    setStatus(C.status.MODIFY);
                    break;
                case CANCEL:
                    //  route_adapter.removeItem();
                    break;
                case YES:
                    if (action_submit_data()) {
                        //    route_adapter.updateListItem(this);
                    }
                    break;
                case C_EDIT:
                    //   route_adapter.removeItem();
                    Tool.trace(ctx, "C_EDIT");
                    break;
                case C_INSERT:
                    setStatus(C.status.COMPLETE);
                    adapter_pointer.addItem(pos);
                    Tool.trace(ctx, "C_INSERT");
                    //  route_adapter.addnew(pos);
                    break;
                case C_LINECUT:
                    final boolean mark = mMarkType == C.breaktype.CONTINUE;
                    setMarkerBreak(mark);
                    setStatus(C.status.COMPLETE);
                    adapter_pointer.mark(pos, mark);
                    //   route_adapter.updateListItem(this);
                    Tool.trace(ctx, "Marker Notified!");
                    break;
                case C_OK:
                    //this is the cancel button from the layout
                    setStatus(C.status.COMPLETE);
                    break;
                case C_REMOVE:
                    adapter_pointer.removeItem(pos);
                    break;
                case LABEL_CONTROL:
                    adapter_pointer.startLabel(pos);  Tool.trace(ctx, "LABEL_CONTROL");
                    break;
                default:
                    if (use_num_picker) {
                        if (feat == ROW_NUMBER_SET_A) {
                            mNumberPickerBuilder.setMinNumber(-10);
                        } else mNumberPickerBuilder.setMinNumber(0);
                        numberic_dialog_set_position = feat;
                        mNumberPickerBuilder.show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Tool.trace(ctx, e.toString());
        }
    }


    //TODO: working on something for double digits..
    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        // text.setText("Number: " + number + "\nDecimal: " + decimal + "\nIs negative: " + isNegative + "\nFull number: "  + fullNumber);
        boolean do_anaylsis = false;
        switch (numberic_dialog_set_position) {
            case ROW_NUMBER_SET_A:
                do_anaylsis = true;
                number_row_a = String.format("%.2f", fullNumber);
                distance_a.setText(number_row_a);
                break;
            case ROW_NUMBER_SET_B:
                do_anaylsis = true;
                number_row_b = String.format("%.2f", fullNumber);
                distance_b.setText(number_row_b);
                break;
            case ROW_NUMBER_SET_C:
                number_row_c = String.format("%.2f", fullNumber);
                depth_from_ground.setText(number_row_c);
                //   Content.current_sketch_map.get_route_node_at(pos)
                //   route_adapter.getItem(pos).set_depth((float) fullNumber);
                break;
            case ROW_NUMBER_SET_D:
                number_row_d = String.format("%.1f", fullNumber);
                cable_width.setText(number_row_d);
                // Content.current_sketch_map.get_route_node_at(pos)
                //.set_cable_r((float) fullNumber);
                //route_adapter.getItem(pos).set_cable_r((float) fullNumber);
                break;
            default:
                break;
        }
        if (do_anaylsis) {
            analyze_distances_possible_and_update();
        }
        update_stack_list(numberic_dialog_set_position, fullNumber);
        numberic_dialog_set_position = C.tag.TAG_NONE;
    }
}
