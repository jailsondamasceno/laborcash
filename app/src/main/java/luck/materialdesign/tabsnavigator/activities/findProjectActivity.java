package luck.materialdesign.tabsnavigator.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.tabs.createCategoryFragment;
import luck.materialdesign.tabsnavigator.tabs.projectDescriptionAuthorFragment;
import luck.materialdesign.tabsnavigator.tabs.projectDescriptionFragment;
import luck.materialdesign.tabsnavigator.tabs.projectFindFragment;

/**
 * Created by BeS on 14.09.2017.
 */

public class findProjectActivity extends AppCompatActivity {
    @BindView(R.id.mainFrame)
    FrameLayout mainFrame;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    boolean state = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_without_navdrawer);
        ButterKnife.bind(this);
        mainFrame = (FrameLayout)findViewById(R.id.mainFrame);

        toolbar.setTitle("Procurar  projetos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("FIND PROJECT ACTIVITY", "OnCreate");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("FIND PROJECT ACTIVITY", "OnPause");
        state = false;
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ButterKnife.bind(this);
        Log.d("FIND PROJECT ACTIVITY", "OnPostResume");
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
        if (getIntent().getStringExtra("comoclient") == null) {

            Bundle bundle = new Bundle();
            bundle.putString("type", getIntent().getStringExtra("type"));
            projectFindFragment fragment = new projectFindFragment();
            fragment.setArguments(bundle);
            fManager.replace(R.id.mainFrame, fragment);
            fManager.commit();
        }else{
            Bundle bundle2 = new Bundle();
            bundle2.putString("project", getIntent().getStringExtra("project"));
            bundle2.putString("group", getIntent().getStringExtra("group"));
            bundle2.putString("type", getIntent().getStringExtra("type"));
            projectDescriptionAuthorFragment fragment = new projectDescriptionAuthorFragment();
            fragment.setArguments(bundle2);
            fManager.replace(R.id.mainFrame, fragment);
            fManager.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("FIND PROJECT", "RESUME");
    }
}