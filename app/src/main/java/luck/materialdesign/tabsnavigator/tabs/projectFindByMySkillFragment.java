package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.projectModel;

/**
 * Created by BeS on 14.09.2017.
 */

public class projectFindByMySkillFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.text)
    TextView noResult;


    accountFreeModel account;

    List<String> projects;
    String firebaseSkill;
    List<projectModel> albumList;
    ArrayList<String> habilid = new ArrayList<>();
    Bundle bundle;
    projectModel project;
    String childName;
    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        albumList = new ArrayList<>();
        projects = new ArrayList<>();
        childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
        Log.d("childname", childName);
        FirebaseDatabase.getInstance().getReference("users").child(childName).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    account = dataSnapshot.getValue(accountFreeModel.class);
                    Log.d("SPECGROUP", account.getSpecgroup());
                    getProjects();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    public void getProjects(){
        FirebaseDatabase.getInstance().getReference("projects").child("open").child(account.getSpecgroup()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null) {
                    noResult.setVisibility(View.INVISIBLE);
                    project = dataSnapshot.getValue(projectModel.class);
                    String[] res = project.getSkills().trim().split(",");
                    String[] skills = account.getSkills().trim().split(",");
                    Log.d("RES", res.toString());
                    Log.d("SKILLD", skills.toString());
                    List<String> list = Arrays.asList(res);
                    String result = " ";
                    for( String ss : skills) {

                        if (list.contains(ss)) {
                            albumList.add(project);
                            projects.add(dataSnapshot.getKey());
                            recyclerView.setAdapter(new projAdapter(getContext(), albumList));
                        }
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onRecyclerClick(String projName, String group, String author){
        FragmentTransaction fManager = getFragmentManager().beginTransaction();

        Bundle bundle2 = new Bundle();
        bundle2.putString("project", projName);
        bundle2.putString("group", group);
        bundle2.putString("type", "open");
        if (author.equals(childName)){
            projectDescriptionAuthorFragment fragment = new projectDescriptionAuthorFragment();
            fragment.setArguments(bundle2);
            bundle2.putString("own", "true");
            fManager.replace(R.id.mainFrame, fragment);
        }else{
            projectDescriptionFragment fragment = new projectDescriptionFragment();
            bundle2.putString("own", "false");
            fragment.setArguments(bundle2);
            fManager.replace(R.id.mainFrame, fragment);
        }
        fManager.addToBackStack(" ");
        fManager.commit();
    }


    public class projAdapter  extends RecyclerView.Adapter<projAdapter.MyViewHolder> {

        private Context mContext;
        private List<projectModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView created;
            private TextView bids;
            private TextView price;
            private TextView skill;

            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.name);
                created = (TextView)view.findViewById(R.id.created);
                bids = (TextView)view.findViewById(R.id.bids);
                price = (TextView)view.findViewById(R.id.price);
                skill = (TextView) view.findViewById(R.id.gridNoted);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public projAdapter(Context mContext,  List<projectModel> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_find_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final projectModel album = albumList.get(position);
            holder.name.setText(album.getName());
            habilid.clear();
            Log.d("NAME", album.getName());
            Log.d("TIME", album.getTimeStamp());
            holder.created.setText("Criado: "+converteTimestamp(album.getTimeStamp()));
            if (album.getBid_count() != null){
                holder.bids.setText("Proposta: " + album.getBid_count());
            }else{
                holder.bids.setText("Proposta: 0");
            }
            holder.price.setText(album.getPrice());

            firebaseSkill = album.getSkills();
            String[] skill = firebaseSkill.split(",");
            String res = "";
            for(String s : skill){
                res = res + s + "  |  ";
                Log.d("SIZE", String.valueOf(habilid.size()));
                Log.d("STRING", habilid.toString());
                Log.d("SKIIIIIL", s+ "    ");
            }
            holder.skill.setText(res.substring(0, res.length()-3));


            holder.cardView.setOnClickListener(f ->{
                onRecyclerClick(projects.get(position), albumList.get(position).getType(), albumList.get(position).getUserChild());
            });

        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }


        private CharSequence converteTimestamp(String mileSegundos){
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(Long.parseLong(mileSegundos));
            //here your time in miliseconds
            Calendar cl2 = Calendar.getInstance();
            cl2.setTimeInMillis(System.currentTimeMillis());
            String date = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + cl.get(Calendar.MONTH) + ":" + cl.get(Calendar.YEAR);
            String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
            if (cl.get(Calendar.DAY_OF_MONTH) == cl2.get(Calendar.DAY_OF_MONTH)){
                return DateFormat.format("HH:mm", Long.parseLong(mileSegundos));
            }
            else{
                return DateFormat.format("dd.MM  HH:mm", Long.parseLong(mileSegundos));
            }
        }
    }
}
