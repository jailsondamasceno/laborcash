package luck.materialdesign.tabsnavigator.tabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;

import luck.materialdesign.tabsnavigator.MainActivity;


/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> Titles = new ArrayList<>(); // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    String type;
    String group;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,ArrayList<String> mTitles, String type, String group) {
        super(fm);
        this.type = type;
        this.Titles = mTitles;
        this.group = group;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (type.equals("search")){
            if (position == 0){
                return new categoryFragment().newInstance(position, "not empty");
//                return new emptyFragment();
            }else{
//                return new freelancerLocationSearchFragment().newInstance(0);
                return new projectFindByMySkillFragment();
            }
        }

        else if (type.equals("searchFree")){
            Log.d("POSTITION", String.valueOf(position));
            if (position == 0){
                return new findFreelancerListFragment().newInstance(0, group);
            }else{
//                return new freelancerLocationSearchFragment().newInstance(0);
//                return new freelancerLocationSearchFragment().newInstance(1, group);
                return new MapViewFragment().newInstance(1, group);
            }
        }
        else if (type.equals("freelancer")){
            switch (position){
                case 0:
                    return new freelancerTodosFragment();
                case 1:
                    return new freelancerTrabalharFragment();
                case 2:
                    return new freelancerFinishedFragment().newInstance(2);
                case 3:
                    return new freelancerFinishedFragment().newInstance(3);
                default:
                    return new emptyMockupFragment().newInstance(position, "empty_client");
            }
        }
        else if (type.equals("client")){
            switch (position){
                case 0:
                    return new clientTodosFragment();
                case 1:
                    return new clientTrabalharFragment();
                case 2:
                    return new clientFinishedFragment().newInstance(2);
                case 3:
                    return new clientFinishedFragment().newInstance(3);
                default:
                    return new emptyMockupFragment().newInstance(position, "empty_client");
            }
        }
        return new categoryFragment();
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