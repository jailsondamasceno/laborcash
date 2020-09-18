package luck.materialdesign.tabsnavigator.tabs;



import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.utils.album;


/**
 * Created by BeS on 08.09.2017.
 */

public class panelDeControlFragment extends Fragment {

    private FragmentTransaction fManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.panel_de_control, container, false);

        fManager = getFragmentManager().beginTransaction();
        emptyMockupFragment profile = new emptyMockupFragment().newInstance(0, "empty_profile");
        emptyMockupFragment proposal = new emptyMockupFragment().newInstance(0, "empty_proposal");
        emptyMockupFragment findproj = new emptyMockupFragment().newInstance(0, "empty_project");
        fManager.replace(R.id.frameProfile, profile);
        fManager.replace(R.id.frameProposal, proposal);
        fManager.replace(R.id.frameProject, findproj);
        fManager.commit();

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

}
