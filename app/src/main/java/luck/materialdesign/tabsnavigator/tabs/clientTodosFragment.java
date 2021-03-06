package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;

import luck.materialdesign.tabsnavigator.activities.findProjectActivity;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.projectBriefModel;
import luck.materialdesign.tabsnavigator.model.projectModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;
import luck.materialdesign.tabsnavigator.utils.adapter;
import luck.materialdesign.tabsnavigator.utils.album;

/**
 * Created by BeS on 13.09.2017.
 */

public class clientTodosFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.relativeEmpty)
    RelativeLayout relativeLayout;

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.btn)
    TextView btn;

    String userName;
    Bundle bundle;
    userProjectModel project;
    List<projectModel> albumList;

    List<String> projects;
    projectModel a;
    adapter adapter;
    accountModel user;
    int[] image = new int[]{R.drawable.profile_dummy, R.drawable.dummy, R.drawable.rocket};
    String[] imageName = new String[]{"Seu perfil está incompleto", "Você ainda não fez nenhuma proposta", "Você ainda não possui projetos ativos"};
    String[] btnText = new String[]{"COMPLETAR SEUS DADOS", "ENVIAR UMA PROPOSTA", "BUSCAR PROJETOS"};

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.project_recycler, container, false);
        ButterKnife.bind(this, v);

        icon.setImageResource(image[2]);
        text.setText("Você ainda não possui projetos abertos");
        text.setTextSize(30);
        btn.setText(btnText[2]);

        String childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
        albumList = new ArrayList<>();
        projects = new ArrayList<>();

//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase.getInstance().getReference("users").child(childName).child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    Log.d("DATA", dataSnapshot.getValue().toString());
                   project = dataSnapshot.getValue(userProjectModel.class);
                    FirebaseDatabase.getInstance().getReference("projects").child(project.getStatus()).child(project.getType()).child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                relativeLayout.setVisibility(View.INVISIBLE);

                                a = dataSnapshot.getValue(projectModel.class);
                                Log.d("NAME", a.getName());
                                albumList.add(a);
                                projects.add(dataSnapshot.getKey());
                                adapter = new adapter(getContext(), albumList);
                                recyclerView.setAdapter(adapter);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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

    public void onRecyclerClick(String proj, String group, String type){
        FragmentTransaction fManager = getFragmentManager().beginTransaction();

        Bundle bundle2 = new Bundle();
        bundle2.putString("project", proj);
        bundle2.putString("group", group);
        bundle2.putString("type", type);
        projectDescriptionAuthorFragment fragment = new projectDescriptionAuthorFragment().newInstance(0, group, proj, type);
        fragment.setArguments(bundle2);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack("client");
        fManager.commit();

//        Intent intent = new Intent(getActivity(), findProjectActivity.class);
//        intent.putExtra("comoclient", "true");
//        intent.putExtra("project", proj);
//        intent.putExtra("group", group);
//        intent.putExtra("type", type);
//        startActivity(intent);
    }

    public class adapter  extends RecyclerView.Adapter<adapter.MyViewHolder> {

        private Context mContext;

        private List<projectModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView created;
            public TextView bid;
            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
            name = (TextView)view.findViewById(R.id.name);
            created = (TextView)view.findViewById(R.id.created);
            bid = (TextView)view.findViewById(R.id.bids);
            cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public adapter(Context mContext, List<projectModel> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_item_brief, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final projectModel album = albumList.get(position);
            holder.name.setText(album.getName());
            holder.created.setText("Criado: "+converteTimestamp(album.getTimeStamp()));

            if (album.getBid_count() != null){
                holder.bid.setText("Proposta: " + album.getBid_count());
            }else{
                holder.bid.setText(" ");
            }

            holder.cardView.setOnClickListener(f ->{
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)")).child("projects").child(projects.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            userProjectModel proj = dataSnapshot.getValue(userProjectModel.class);
                            onRecyclerClick(projects.get(position), albumList.get(position).getType(), proj.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            });
        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }
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
            return DateFormat.format("dd.MM  hh:mm", Long.parseLong(mileSegundos));
        }
    }

}
