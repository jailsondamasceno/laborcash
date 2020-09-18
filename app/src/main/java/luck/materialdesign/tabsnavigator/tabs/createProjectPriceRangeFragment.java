package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.utils.categoryAdapter;
import luck.materialdesign.tabsnavigator.utils.categoryAlbum;


/**
 * Created by BeS on 09.09.2017.
 */

public class createProjectPriceRangeFragment extends Fragment {

    @BindView(R.id.priceRange)
    Spinner priceRange;

    @BindView(R.id.btnNext)
    TextView btnNext;


    Bundle bundle;

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_project_price_range_layout, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();
        Log.d("TYPE", bundle.getString("type"));
        Log.d("GROUP", bundle.getString("group"));
        Log.d("Area", bundle.getString("area"));
        btnNext.setOnClickListener(f -> {
            FragmentTransaction fManager = getFragmentManager().beginTransaction();

            Bundle bundle2 = new Bundle();
            bundle2.putString("type", bundle.getString("type"));
            bundle2.putString("group",  bundle.getString("group"));
            bundle2.putString("area", bundle.getString("area"));
            bundle2.putString("skills", bundle.getString("skills"));
            bundle2.putString("price", priceRange.getSelectedItem().toString());
            createProjectPublishFragment fragment = new createProjectPublishFragment();
            fragment.setArguments(bundle2);
            fManager.replace(R.id.mainFrame, fragment);
            fManager.addToBackStack(" ");
            fManager.commit();
        });
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
