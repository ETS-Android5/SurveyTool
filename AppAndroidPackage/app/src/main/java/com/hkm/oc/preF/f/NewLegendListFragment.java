package com.hkm.oc.preF.f;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hkm.datamodel.DataHandler;
import com.hkm.listviewhkm.LabelIntent;
import com.hkm.oc.R;
import com.hkm.oc.preF.root.CrossReceiver;

import java.util.ArrayList;

/**
 * Created by Hesk on
 */
public class NewLegendListFragment extends ListFragment implements CrossReceiver {
    public static final String START = "offset_select_start", END = "offset_select_end", ROLE = "RoleType", SELECT = "preselect";
    public static final int roleLine = 0, roleFeature = 1;
    public static final String TAG = "Logobuttons";
    private static final int defaultselection = 0;

    public int internal_selection;
    private int offset_start, offset_end, role;
    private LabelIntent activity_interface;
    private ArrayList<Bundle> workinglist;
    private EfficientAdapter madapterEfficie;

    public static NewLegendListFragment newInstance(int selected_index, int offset_index_start, int offset_index_end, int roleType) {
        NewLegendListFragment newFragment = new NewLegendListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECT, selected_index);
        bundle.putInt(START, offset_index_start);
        bundle.putInt(END, offset_index_end);
        bundle.putInt(ROLE, roleType);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
        //   System.out.print("asdac");
        activity_interface = (LabelIntent) act;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refresh() {
        madapterEfficie.notifyDataSetChanged();
    }

    private int findBundleInternalSelection(int ref_id) {
        for (int i = 0; i < workinglist.size(); i++) {
            final Bundle b = workinglist.get(i);
            if (b.getInt("nid") == ref_id) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param arb                    the data bundle from the list to be rendered
     * @param reference_id_selection this int will be converted from the numeric intrinsic into the local index
     * @return
     */
    @Override
    public NewLegendListFragment take_list_render(ArrayList<Bundle> arb, int reference_id_selection) {
        workinglist = arb;
        if (reference_id_selection == -1) {
            internal_selection = 0;
        } else {
            internal_selection = findBundleInternalSelection(reference_id_selection);
        }
        return this;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment -- onCreate");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "datalist_icons -- onCreateView");
        Bundle args = getArguments();
        internal_selection = args != null ? args.getInt(SELECT) : defaultselection;
        offset_start = args != null ? args.getInt(START) : defaultselection;
        offset_end = args != null ? args.getInt(END) : defaultselection;
        role = args != null ? args.getInt(ROLE) : defaultselection;
        if (role == 0) {
            workinglist = DataHandler.get_tab1_list_content(getActivity());
            //Arrays.copyOfRange(Constant.MapData.InternalIconList, offset_start, offset_end);
        } else if (role == 1) {
            workinglist = new ArrayList<Bundle>();
        }

        // setSelection(internal_selection);
        View custom_list = inflater.inflate(R.layout.datalist_icons_grid_new, container, false);

        madapterEfficie = new EfficientAdapter(getActivity());
        setListAdapter(madapterEfficie);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TestFragment-----onDestroy");
    }

    private class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context ctx;

        public EfficientAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
            ctx = context;
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return workinglist.size();
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return 0;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */

        public View getView(final int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.datalist_icons_item_new, null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                final InteractiveObject tx = new InteractiveObject(convertView, position);
                convertView.setTag(tx);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                final InteractiveObject recall_tx = (InteractiveObject) convertView.getTag();
                recall_tx.updateOrder(position).pickSelect(internal_selection);
            }
            return convertView;
        }

        public class InteractiveObject implements View.OnClickListener {
            public static final String TAG = "interaction object";
            private boolean selected;
            //p  = the ordinary internal selection from the list
            private int p, global_list_position;
            private View buttonview;
            private TextView mtextview;
            private ImageView reusableIm;

            public InteractiveObject(View retrievedView, int position) {
                mtextview = (TextView) retrievedView.findViewById(R.id.text1);
                buttonview = retrievedView.findViewById(R.id.backgroundset);
                reusableIm = (ImageView) retrievedView.findViewById(R.id.icon);
                buttonview.setOnClickListener(this);
                selected = false;
                updateOrder(position);
            }

            @Override
            public void onClick(View v) {
                Log.d(TAG, "position: " + p);
                internal_selection = p;
                activity_interface.onPickList(global_list_position, role);
                EfficientAdapter.this.notifyDataSetChanged();
            }

            public InteractiveObject updateOrder(int position) {
                final Bundle mbund = workinglist.get(position);
                final Bitmap bm = (Bitmap) mbund.getParcelable("icon");
                final String title_row = mbund.getString("title");
                global_list_position = mbund.getInt("nid");
                if (bm == null) {
                    final Bitmap f = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo_ibike_);
                    reusableIm.setImageBitmap(f);
                    reusableIm.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    reusableIm.setImageBitmap(bm);
                    reusableIm.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                mtextview.setText(title_row);
                p = position;
                return this;

            }

            public boolean isSelected() {
                return selected;
            }

            public InteractiveObject pickSelect(int x) {
                if (x == p) {
                    if (!selected) {
                        selected = true;
                        //  buttonview.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_default_disabled_holo_dark));
                        buttonview.setBackground(getResources().getDrawable(R.color.legend_list_selected_background));
                        mtextview.setTextColor(getResources().getColor(R.color.legend_list_selected));
                    }
                } else {
                    if (selected) {
                        selected = false;
                        buttonview.setBackground(null);
                        mtextview.setTextColor(getResources().getColor(R.color.legend_list_not_selected));
                    }
                }
                return this;
            }
        }


    }


}
