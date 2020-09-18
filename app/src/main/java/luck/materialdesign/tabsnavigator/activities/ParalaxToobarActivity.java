package luck.materialdesign.tabsnavigator.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import luck.materialdesign.tabsnavigator.R;


public class ParalaxToobarActivity extends AppCompatActivity {
    boolean isShow = false;
    int scrollRange;
    AppBarLayout appBarLayout;
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paralax_toolbar);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels/2));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset + scrollRange == 0 && !isShow){
                    collapsingToolbar.setTitle("Username");
                    isShow = true;
                }else if (verticalOffset + scrollRange != 0 && isShow){
                    collapsingToolbar.setTitle("");
                    isShow = false;


                }

            }
        });




    }

}
