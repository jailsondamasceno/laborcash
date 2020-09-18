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

public class createProjectTypeFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Bundle bundle;
    String[] recItem;

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_project_type, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();
        Log.d("TYPE", bundle.getString("area"));
        Log.d("GROUP", bundle.getString("group"));
        recItem = getResources().getStringArray(R.array.type_project);
        if (recItem != null){
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new skillAdapter(getContext(), recItem));
        }
        return v;
    }

    public void nextScreenFinally(String area, String group, String skills, String type){

        FragmentTransaction fManager = getFragmentManager().beginTransaction();

        Bundle bundle2 = new Bundle();
        bundle2.putString("area", area);
        bundle2.putString("group", group);
        bundle2.putString("skills", skills);
        bundle2.putString("type", type);
        createProjectPriceRangeFragment fragment = new createProjectPriceRangeFragment();
        fragment.setArguments(bundle2);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(" ");
        fManager.commit();
    }

    public void nextScreenHourly(String area, String group, String skills, String type){

        FragmentTransaction fManager = getFragmentManager().beginTransaction();

        Bundle bundle2 = new Bundle();
        bundle2.putString("area", area);
        bundle2.putString("group", group);
        bundle2.putString("skills", skills);
        bundle2.putString("type", type);
        createProjectHourlyPriceRangeFragment fragment = new createProjectHourlyPriceRangeFragment();
        fragment.setArguments(bundle2);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(" ");
        fManager.commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public class skillAdapter extends RecyclerView.Adapter<skillAdapter.MyViewHolder> {

        private Context mContext;

        private String[] albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView text;
            private CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                text = (TextView)view.findViewById(R.id.text);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public skillAdapter(Context mContext, String[] albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.textview_card_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.text.setText(albumList[position]);
            holder.cardView.setOnClickListener(r -> {
                if (position == 0) {
                    nextScreenFinally(bundle.getString("area"), bundle.getString("group"), bundle.getString("skills"), albumList[position]);
                }else{
                    nextScreenHourly(bundle.getString("area"), bundle.getString("group"), bundle.getString("skills"), albumList[position]);
                }
            });
        }

        @Override
        public int getItemCount() {

            return albumList.length;
        }

    }


}
