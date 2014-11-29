package com.hkm.oc.panel.actionmode.mode;

import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.module.PanelActionModeExt;
import com.hkm.oc.panel.actionmode.module.ModeTag;
import com.hkm.oc.panel.basic_panel_support;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.elements.ProposedTrialPit;
import com.hkm.U.Tool;
import com.hkm.oc.panel.corepanel.handler.PTModeCallback;
import com.hkm.oc.preF.f.WorkPanelFragment;

/**
 * Created by Hesk on 26/6/2014.
 */
public class ActionModeProposedTrialPit extends PanelActionModeExt implements PTModeCallback {


    private MapPanel.CMode currentstoredmode;
    private ProposedTrialPit ppt;
    private int action_mode_selection;
    private View custom_view_layout;
    private EditText inputbra;
    private TextView title;
    public ActionModeProposedTrialPit(WorkPanelFragment workPanelFragment, basic_panel_support c) {
        super(workPanelFragment, c);
        ppt = panel.getPT().setFragmentmanager(root_context.getSupportFragmentManager());
    }

    /**
     * Called when the action mode is created;
     * startActionMode() was calledw
     *
     * @param mode
     * @param menu
     * @return
     */
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        final MenuInflater inf = mode.getMenuInflater();
        currentstoredmode = panel.getMode();
        panel.setMode(MapPanel.CMode.TP_MODE);
        // Assumes that you have "contexual.xml" menu resources
        action_mode_selection = 0;
        inf.inflate(R.menu.ptp_main, menu);
        ppt.setActionModeObject(mode);
        ppt.onMode(ProposedTrialPit.TPmodeON);
        //   final int done_id = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
        //   Button c = (Button) findViewById(done_id);
        //   c.setOnClickListener(this);
        //   mode.setTag();
        init_input_tp_label(mode);
        inputbra.setVisibility(View.GONE);
        ppt.setTPListener(this);
        initTag(mode, ModeTag.Tag.TP_MODE, true);
        return true;
    }

    private void init_input_tp_label(ActionMode mode) {
        try {
            custom_view_layout = LayoutInflater.from(root_context).inflate(R.layout.action_mode_tp, null);
            mode.setCustomView(custom_view_layout);
            inputbra = (EditText) root_context.findViewById(R.id.text_on_rulerbar);
            inputbra.setHint("Please enter the label for this TP box");
            title = (TextView) root_context.findViewById(R.id.actionm_bar_title);
            ppt.setInputLabelField(inputbra);
            inputbra.setVisibility(View.VISIBLE);
            inputbra.setSingleLine();
            inputbra.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLabelInput(MenuInflater inf, Menu menu, ActionMode mode) {
        init_input_tp_label(mode);
        inf.inflate(R.menu.truefalse, menu);
        title.setVisibility(View.GONE);
    }

    private void clear_custom_view(final ActionMode mode) {
        try {
            View myView = root_context.findViewById(mode.getCustomView().getId());
            myView.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    // Called each time the action mode is shown. Always called after
    // onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        //  mActionModeCloseReady = false;
        final MenuInflater inf = mode.getMenuInflater();
        mode.getMenu().clear();

        inputbra.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        clear_custom_view(mode);
        /**
         *  get CallBack Selection Number From TP
         */
        if (mode.getTag() != null && mode.getTag() instanceof ProposedTrialPit.TagPointer) {
            ProposedTrialPit.TagPointer tagpoint = (ProposedTrialPit.TagPointer) mode.getTag();
            action_mode_selection = tagpoint.getModeNum();
            mode.setTag(null);
        }
        //remove the custom text input box


        try {
            switch (action_mode_selection) {
                case 0:
                    inf.inflate(R.menu.ptp_main, menu);
                    ppt.onMode(ProposedTrialPit.TPmodeON);
                    changeClose(mode, true);
                    return true;
                case 1:
                    /**
                     *  1: The app is about to draw the slop line
                     */
                    inf.inflate(R.menu.truefalse, menu);
                    ppt.onMode(ProposedTrialPit.AngleMode);
                    setTitle("Use two fingers to adjust the line");
                    return true;
                case 2:
                    /**
                     *  2: The app is editing the existing boxes
                     */
                    inf.inflate(R.menu.ptp_edit, menu);
                    ppt.onMode(ProposedTrialPit.SelectMode);
                    setTitle("Touch to select the TP");
                    return true;
                case 3:
                    // there is nothing to do in here
                    // inf.inflate(R.menu.truefalse, menu);
                    ppt.onMode(ProposedTrialPit.DrawRectMode);
                    setTitle("Touch one finger on the screen and drag. Release to confirm and label the TP");

                    return true;
                case 4:
                    /**
                     * 4: editing the label input again
                     */
                    onLabelInput(inf, menu, mode);
                    return true;
                case 5:
                    /**
                     *  5: callback and confirmed from drawing rectangle from the TP
                     */
                    onLabelInput(inf, menu, mode);
                    return true;
                case 6:
                    /**
                     *  6: start making adjustment of the text
                     */
                    inf.inflate(R.menu.truefalse, menu);

                    return true;
                case 7:
                    /**
                     *  7: start making adjustment of the text
                     */
                    inf.inflate(R.menu.truefalse, menu);
                case 8:
                    /**
                     *  7: start making adjustment of the text
                     */
                    inf.inflate(R.menu.truefalse, menu);

                default:
                    return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            return true;
        }
        //end
    }

    private boolean onPressYes(ActionMode mode) {
        if (action_mode_selection == 5 || action_mode_selection == 4) {
            /**
             * the confirm button for the label edit text field
             */
            if (ppt.onConfirmLabelingTP()) {
                if (action_mode_selection == 5) {
                    action_mode_selection = 6;
                    Tool.hideKeyBoard(root_context, inputbra);
                }
                if (action_mode_selection == 4) {
                    action_mode_selection = 7;
                }
                ppt.onMode(ProposedTrialPit.EditLabelPosition);
                setTitle("Touch on the label and adjust the position");
                mode.invalidate();
            } else {
                Tool.trace(root_context, "enter the label on the top text input");
            }
            return true;
        } else if (action_mode_selection == 1) {
            /**
             *   accepting the current line angle
             *   and start the box rectangle mode
             */
            action_mode_selection = 3;
            mode.invalidate();
            return true;
        } else if (action_mode_selection == 7) {
            ppt.onFinalizeTP();
            mode.finish();
        } else if (action_mode_selection == 6) {
            /**
             * confirmation from adjusting the position of the label for new TP
             */
            ppt.onFinalNewTp();
            mode.finish();
        } else if (action_mode_selection == 8) {
            action_mode_selection = 5;
            mode.invalidate();
        }
        return true;
    }

    /**
     * Called when the user selects a contextual menu item
     *
     * @param mode
     * @param item
     * @return
     */
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Tool.trace(root_context, String.valueOf(f));
        switch (item.getItemId()) {
            case R.id.new_tpV1:
                /**
                 * from the main menu
                 */
                action_mode_selection = 1;
                mode.invalidate();
                return true;
            case R.id.editing_tp:
                /**
                 * from the main menu
                 */
                action_mode_selection = 2;
                mode.invalidate();
                return true;
            case R.id.act_cancel:
                action_mode_selection = 0;
                mode.invalidate();
                return true;
            case R.id.act_accept:
                if (action_mode_selection == 2) {
                    //accept on editing previous tp
                    ppt.deSelectAll();
                    action_mode_selection = 0;
                } else if (action_mode_selection == 4) {
                    //accept on drawings
                    ppt.deSelectAll();
                    action_mode_selection = 5;
                }
                mode.invalidate();
                return true;
            case R.id.de_yes:
                return onPressYes(mode);
            case R.id.de_no:
                if (action_mode_selection == 5) {
                    /**
                     * reject button for the label edit text field
                     */
                    //action_mode_selection = 0;
                    ppt.reset_label_field();
                    Tool.showKeyBoard(root_context, inputbra);
                }
                if (action_mode_selection == 1) {

                }
                if (action_mode_selection == 6) {

                }
                return true;
            case R.id.edit_label:
                if (ppt.isSingleBox()) {
                    action_mode_selection = 4;
                    mode.invalidate();
                } else {
                    Tool.trace(root_context, "Pleas make sure that only one TP is selected.");
                }
                return true;
              /*  case R.id.act_edit_rotate:
                    action_mode_selection = 4;
                    mode.invalidate();
                    return true;*/
            case R.id.act_trash:
                ppt.removeSelected();
                //panel.resume();
            default:
                Tool.trace(root_context, "default pressed");
                return true;
        }
    }


    /**
     * Called when the user exits the action mode
     */
    public void onActionModeDoneClick() {
        // Do something here.
    }

    public void setTitle(String t) {
        title.setText(t);
    }

    @Override
    public void PT_trigger_setTitle(String title) {
        setTitle(title);
    }
}
