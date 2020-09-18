package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.sliding.SlidingTabLayout;

/**
 * Created by BeS on 16.09.2017.
 */

public class findFreelancerFragment  extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    ViewPagerAdapter adapter;
    @BindView(R.id.tabs)
    SlidingTabLayout tabs;
    String type;
    ArrayList<String> TitlesMan = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tabs_fragment, container, false);
        ButterKnife.bind(this, v);


            TitlesMan.clear();
            TitlesMan.add("CATEGORIAS");
            TitlesMan.add("VER NO MAPA");

            adapter =  new ViewPagerAdapter(getActivity().getSupportFragmentManager(),TitlesMan,"searchFree", getArguments().getString("area"));

            pager.setAdapter(adapter);



        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Tabs", "onAttach");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
