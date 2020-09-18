package luck.materialdesign.tabsnavigator.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.paypalActivity;
import luck.materialdesign.tabsnavigator.utils.adapter;

/**
 * Created by BeS on 26.09.2017.
 */

public class choosePaymentFragment extends Fragment{

    @BindView(R.id.btnPaypal)
    TextView btnPaypal;

    @BindView(R.id.btnStripe)
    TextView btnStripe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_paymethod, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        Log.d("project", bundle.getString("project"));
        btnPaypal.setOnClickListener(f -> {
            Intent intent = new Intent(getActivity(), paypalActivity.class);
            intent.putExtra("payment", bundle.getString("payment"));
            intent.putExtra("group", bundle.getString("group"));
            intent.putExtra("project", bundle.getString("project"));
            intent.putExtra("projectuserchild", bundle.getString("projectuserchild"));
            intent.putExtra("freeChild", bundle.getString("freeChild"));
            intent.putExtra("projectName", bundle.getString("projectName"));
            getActivity().startActivity(intent);
            toHomeScreen();
        });

        btnStripe.setOnClickListener(f -> {
            Bundle bundle2 = new Bundle();

            stripeFragment fragment = new stripeFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fManager = getFragmentManager().beginTransaction();
            fManager = getFragmentManager().beginTransaction();
            fManager.replace(R.id.mainFrame, fragment);
            fManager.addToBackStack(" ");
            fManager.commit();
        });
        return v;
    }

    public void toHomeScreen(){
        FragmentTransaction fManager;
        Bundle bundle = new Bundle();
        bundle.putString("type", "client");
        tabsFragment tabsFragment = new tabsFragment();
        tabsFragment.setArguments(bundle);
        fManager = getFragmentManager().beginTransaction();
        fManager.replace(R.id.mainFrame, tabsFragment);
        fManager.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("COMO EMPREGADOR");
    }
}
