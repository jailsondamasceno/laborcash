package luck.materialdesign.tabsnavigator.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.loginActivity;
import luck.materialdesign.tabsnavigator.tabs.registerChooseFragment;
import luck.materialdesign.tabsnavigator.tabs.registerWithoutEmailFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;
import luck.materialdesign.tabsnavigator.utils.Country;
import luck.materialdesign.tabsnavigator.utils.countryListAdapter;

/**
 * Created by BeS on 09.09.2017.
 */

public class registerActivity extends AppCompatActivity {

    @BindView(R.id.mainFrame)
    FrameLayout frame;
    FragmentTransaction fManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_frame_layout);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("type") != null){
            fManager = getSupportFragmentManager().beginTransaction();
            fManager.replace(R.id.mainFrame, new registerWithoutEmailFragment());
            fManager.commit();
        }else {
            fManager = getSupportFragmentManager().beginTransaction();
            fManager.replace(R.id.mainFrame, new registerChooseFragment());
            fManager.commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = getFragmentManager().findFragmentById(R.id.mainFrame);
//        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
//            super.onBackPressed();
            startActivity(new Intent(registerActivity.this, loginActivity.class));
            finish();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}