package com.hkm.bst_tool_canvas.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hkm.advancedtoolbar.V5.BeastBar;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import butterknife.ButterKnife;

/**
 * Created by hesk on 1/7/2017.
 */
public abstract class Mainase extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener {


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected final boolean postThreadExist() {
        if (getFragmentManager() == null || getFragmentManager().isDestroyed())
            return false;
        else return true;
    }

    //   protected UserSessionAPI api_user;
    //  protected MovieTicketingAPI api_movie;


    protected abstract int layout();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    protected void applySpaceing(TextView tv) {
        tv.setLineSpacing(2f, 1.6f);
    }

    protected void initSelector(DiscreteScrollView roller) {
        roller.setOrientation(DSVOrientation.HORIZONTAL);
        roller.addOnItemChangedListener(this);
        roller.setItemTransitionTimeMillis(209);
        roller.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .setMaxScale(1.0f)
                .build());

        //roller.setItemTransformer(new Tra);
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }


    protected BeastBar.onButtonPressListener beastbar_back_to_finish = new BeastBar.onButtonPressListener() {
        @Override
        public boolean onBackPress(int previousTitleSteps) {
            finish();
            return false;
        }

        @Override
        public void onSearchPress() {

        }
    };

    protected Bundle getCurrentBundle() {
        Bundle data = getIntent().getExtras();
        if (data == null) {
            data = new Bundle();
        }
        return data;
    }


    protected void init_recyclerview_horizontal(RecyclerView recyclerView, RecyclerView.RecycledViewPool pool, RecyclerView.Adapter adapter) {
        // pool.setMaxRecycledViews(R.layout.i_food_item, Integer.MAX_VALUE);
        // recyclerView.setRecycledViewPool(pool);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // recyclerView.getRecycledViewPool().setMaxRecycledViews(R.layout.i_food_item, Integer.MAX_VALUE);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // l_recycler.addItemDecoration(new GridSpacingItemDecoration(3, 1, false));
       // recyclerView.setItemAnimator(new FadeInItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
