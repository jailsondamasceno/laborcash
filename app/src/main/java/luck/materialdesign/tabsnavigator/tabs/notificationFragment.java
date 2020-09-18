package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.chatActivity;
import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.model.bidModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import luck.materialdesign.tabsnavigator.model.userChatModel;

/**
 * Created by BeS on 16.09.2017.
 */

public class notificationFragment extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<notificationModel> albumList;
   notificationModel dialog;

   bidModel bidModel;
    Context context;
    String curUser;
    ArrayList<String> userChilds = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        ButterKnife.bind(this, v);

        albumList = new ArrayList<>();
        context = getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setHasFixedSize(true);
        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("users").child(curUser).child("notification").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    dialog = dataSnapshot.getValue(notificationModel.class);
                    userChilds.add(dataSnapshot.getKey());
                    albumList.add(dialog);
                    recyclerView.setAdapter(new notifyAdapter(getContext(), albumList));
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

    @Override
    public void onResume() {
        super.onResume();


    }

    public void openChat(String dialogChild, String user1Child, String user2Child, String title){
        Intent intent = new Intent(context, chatActivity.class);
        intent.putExtra("childChat",dialogChild);
        intent.putExtra("user1", user1Child);
        intent.putExtra("user2", user2Child);
        intent.putExtra("title", title);
        context.startActivity(intent);
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

    public class notifyAdapter  extends RecyclerView.Adapter<notifyAdapter.MyViewHolder> {

        private Context mContext;
        private List<notificationModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView message;
            private CircleImageView icon;
            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                icon = (CircleImageView) view.findViewById(R.id.iconFreelance);
                message = (TextView)view.findViewById(R.id.text);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public notifyAdapter(Context mContext,  List<notificationModel> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final notificationModel album = albumList.get(position);
            if (album.getType().equals("awarded")) {
                holder.message.setText(album.getUserName() + " você foi premiado para o" + album.getProject());
            }
            else if (album.getType().equals("canceled")) {
                holder.message.setText(album.getUserName() + " removeu do projeto  " + album.getProject());
            }
            else if (album.getType().equals("declined")) {
                holder.message.setText(album.getUserName() + "  recusou o projeto  " + album.getProject());
            }
            else if (album.getType().equals("accepted")) {
                holder.message.setText(album.getUserName() + "  aceitou  o projeto  " + album.getProject());
            }
            else if (album.getType().equals("finishedClient") || album.getType().equals("finishedFreelancer"))   {
                holder.message.setText(album.getProject() + " concluído. Clique para avaliar");
            }
            Glide
                    .with(mContext)
                    .load(album.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(false)
                    .into(holder.icon);

            holder.cardView.setOnClickListener(f ->{
                if (albumList.get(position).getType().equals("awarded")) {
                    FragmentTransaction fManager = getFragmentManager().beginTransaction();
                    curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("project", userChilds.get(position));
                    bundle2.putString("group", albumList.get(position).getGroup());
                    bundle2.putString("type", albumList.get(position).getType());
                    if (albumList.get(position).getUserChild().equals(curUser)) {
                        projectDescriptionAuthorFragment fragment = new projectDescriptionAuthorFragment();
                        fragment.setArguments(bundle2);
                        bundle2.putString("own", "true");
                        fManager.replace(R.id.mainFrame, fragment);
                    } else {
                        projectDescriptionFragment fragment = new projectDescriptionFragment();
                        bundle2.putString("own", "false");
                        fragment.setArguments(bundle2);
                        fManager.replace(R.id.mainFrame, fragment);
                    }
                    fManager.addToBackStack(" ");
                    fManager.commit();
                }else if (albumList.get(position).getType().equals("finishedClient")) {
                    FragmentTransaction fManager = getFragmentManager().beginTransaction();
                    curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("project", userChilds.get(position));
                    bundle2.putString("group", albumList.get(position).getGroup());
                    bundle2.putString("type", albumList.get(position).getType());

                        rateFreelancerFragment fragment = new rateFreelancerFragment();
                        fragment.setArguments(bundle2);
                        fManager.replace(R.id.mainFrame, fragment);

                    fManager.addToBackStack(" ");
                    fManager.commit();
                }else if (albumList.get(position).getType().equals("finishedFreelancer")) {
                    FragmentTransaction fManager = getFragmentManager().beginTransaction();
                    curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("project", userChilds.get(position));
                    bundle2.putString("group", albumList.get(position).getGroup());
                    bundle2.putString("type", albumList.get(position).getType());
                        rateClientFragment fragment = new rateClientFragment();
                        fragment.setArguments(bundle2);
                        fManager.replace(R.id.mainFrame, fragment);
                    fManager.addToBackStack(" ");
                    fManager.commit();
                }
            });

        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }

    }
}