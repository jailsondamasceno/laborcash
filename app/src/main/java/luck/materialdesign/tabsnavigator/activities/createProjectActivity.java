package luck.materialdesign.tabsnavigator.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.tabs.categoryFragment;
import luck.materialdesign.tabsnavigator.tabs.createCategoryFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;

/**
 * Created by BeS on 08.09.2017.
 */

public class createProjectActivity extends AppCompatActivity {
    @BindView(R.id.mainFrame)
    FrameLayout mainFrame;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_without_navdrawer);
        ButterKnife.bind(this);
        mainFrame = (FrameLayout)findViewById(R.id.mainFrame);
        android.support.v4.app.FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
        toolbar.setTitle("Criar projecto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putString("type", "empty");
        createCategoryFragment categoryFragment = new createCategoryFragment();
        categoryFragment.setArguments(bundle);
        fManager.replace(R.id.mainFrame, categoryFragment);
        fManager.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
