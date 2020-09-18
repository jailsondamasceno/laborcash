package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.freelancerOtherAccount;
import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.projectModel;
import luck.materialdesign.tabsnavigator.sliding.SlidingTabLayout;

/**
 * Created by BeS on 14.09.2017.
 */

public class findFreelancerListFragment extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<accountModel> albumList;
    accountModel user;
    SlidingTabLayout tabs;
    String group;
    Context context;
    ArrayList<String> TitlesMan = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    public static findFreelancerListFragment newInstance(int position, String group) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        args.putString("group", group);
        findFreelancerListFragment fragment = new findFreelancerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        ButterKnife.bind(this, v);
        context = getContext();
        group = getArguments().getString("group");
        albumList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    try {
                        user = dataSnapshot.getValue(accountModel.class);
                        if (user.getFreelancer().getSpecgroup() != null) {
                            if (user.getFreelancer().getSpecgroup().equals(group)) {
                                albumList.add(user);
                                recyclerView.setAdapter(new freelancerAdapter(getContext(), albumList));
                            }
                        }
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void openAccount(String userChild){
        Intent intent = new Intent(context, freelancerOtherAccount.class);
        intent.putExtra("childname", userChild);
        context.startActivity(intent);
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

    public class freelancerAdapter  extends RecyclerView.Adapter<freelancerAdapter.MyViewHolder> {

        private Context mContext;
        private List<accountModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView skills;
            private CircleImageView icon;
            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.userName);
                skills = (TextView)view.findViewById(R.id.skills);
                icon = (CircleImageView) view.findViewById(R.id.iconFreelance);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public freelancerAdapter(Context mContext,  List<accountModel> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_freelancer_list_items, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final accountModel album = albumList.get(position);
            holder.name.setText(album.getName());
            Glide
                    .with(mContext)
                    .load(album.getPhoto_profile())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(false)
                    .into(holder.icon);
            String[] skill = album.getFreelancer().getSkills().split(",");
            String res = "";
            for(String s : skill){
                res = res + s + "  |  ";
            }
            if (res != null && res.length() > 3) {
                holder.skills.setText(res.substring(0, res.length() - 3));
            }


            holder.cardView.setOnClickListener(f ->{
                openAccount(albumList.get(position).getEmail().replace(".","(dot)"));
            });

        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }

    }
}
