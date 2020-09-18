package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
 * Created by BeS on 15.09.2017.
 */

public class projectDescriptionAuthorFragment extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    descriptionAdapter adapter;
    @BindView(R.id.tabs)
    SlidingTabLayout tabs;
    String area;
    ArrayList<String> TitlesMan = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    public static projectDescriptionAuthorFragment newInstance(int position, String group, String project, String type) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        args.putString("type", type);
        args.putString("group", group);
        args.putString("project", project);
        projectDescriptionAuthorFragment fragment = new projectDescriptionAuthorFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tabs_fragment, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();



        TitlesMan.clear();
        TitlesMan.add("DETALHES");
        TitlesMan.add("OFERTAS");

        adapter =  new descriptionAdapter(getActivity().getSupportFragmentManager(),TitlesMan,bundle.getString("group"), bundle.getString("project"), bundle.getString("type"));

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


    public class descriptionAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> Titles = new ArrayList<>(); // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        String type;
        String group;
        String project;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public descriptionAdapter(FragmentManager fm, ArrayList<String> mTitles, String group, String project, String type) {
            super(fm);
            this.type = type;
            this.Titles = mTitles;
            this.group = group;
            this.project = project;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

                if (position == 0){
                    return new projectDescriptionFragment().newInstance(position,  group, project, type);
                }else{
//                return new freelancerLocationSearchFragment().newInstance(0);
                    return new bidsFreelancersFragment().newInstance(position, group, project, type);
                }
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles.get(position);
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return Titles.size();
        }
    }

}
