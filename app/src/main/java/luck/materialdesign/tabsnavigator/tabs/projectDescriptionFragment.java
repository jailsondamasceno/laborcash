package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import luck.materialdesign.tabsnavigator.activities.paypalActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.StaggeredTextGridView;
import luck.materialdesign.tabsnavigator.activities.clientOtherAccount;
import luck.materialdesign.tabsnavigator.activities.freelancerAccount;
import luck.materialdesign.tabsnavigator.activities.freelancerOtherAccount;
import luck.materialdesign.tabsnavigator.customViews.RatingBar;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.awardFreelancerModel;
import luck.materialdesign.tabsnavigator.model.bidModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import luck.materialdesign.tabsnavigator.model.projectModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by BeS on 13.09.2017.
 */

public class projectDescriptionFragment extends Fragment {

    @BindView(R.id.projPrice)
    TextView projPrice;

    @BindView(R.id.projBidsCount)
    TextView bidCount;

    @BindView(R.id.gridNoted)
    StaggeredTextGridView skillGrid;

    @BindView(R.id.detailText)
    ExpandableTextView detail;

    @BindView(R.id.authorIcon)
    CircleImageView icon;

    @BindView(R.id.nameAuthor)
    TextView nameAuthor;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.authorCountry)
    TextView country;

    @BindView(R.id.freeIcon)
    CircleImageView freeicon;

    @BindView(R.id.nameFree)
    TextView nameFree;

    @BindView(R.id.statusText)
    TextView statusText;

    @BindView(R.id.rating_bar_free)
    RatingBar ratingBar_free;

    @BindView(R.id.freeCountry)
    TextView freecountry;

    @BindView(R.id.btnEnviar)
    TextView btnEnviar;

    @BindView(R.id.relativeAward)
    RelativeLayout relAward;

    @BindView(R.id.btnAccept)
    TextView btnAccept;

    @BindView(R.id.btnDecline)
    TextView btnDecline;

    @BindView(R.id.btnCancel)
    TextView btnCancel;

    @BindView(R.id.btnFinish)
    TextView btnFinish;
    String token;
    String freeChild;
    String firebaseSkill;
    String curUser;
    String type;
   ActionBar actionBar;

    ArrayList<String> habilid = new ArrayList<>();
    Bundle bundle;
    Long bidCountStock;
    Context context;
    projectModel project;
    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    accountModel freeAccount;
    String bidCountString;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;

    public static projectDescriptionFragment newInstance(int position, String group, String project, String type) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        if (position == 10){
            args.putString("own", "false");
        }else {
            args.putString("own", "true");
        }
        args.putString("type", type);
        args.putString("group", group);
        args.putString("project", project);
        projectDescriptionFragment fragment = new projectDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.project_description, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();
        context = getContext();
        type = bundle.getString("type");
        if (bundle.getString("own").equals("true")){
            btnEnviar.setVisibility(View.INVISIBLE);
            btnAccept.setVisibility(View.INVISIBLE);
            btnDecline.setVisibility(View.INVISIBLE);

        }else{
            btnCancel.setVisibility(View.INVISIBLE);
        }

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (!type.equals("open")){
             relAward.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else{
            relAward.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }
        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    project = dataSnapshot.getValue(projectModel.class);
                    Log.d("PROJ NAME", project.getName());
                        actionBar.setTitle(project.getName());

                    firebaseSkill = project.getSkills();
                    String[] skill = firebaseSkill.split(",");
                    habilid.add(" ");
                    for(String s : skill){
                        habilid.add(s+"  ");
                        Log.d("SIZE", String.valueOf(habilid.size()));
                        Log.d("STRING", habilid.toString());
                        Log.d("SKIIIIIL", s+ "    ");
                    }

                    skillGrid.setmAdapter(new skillAdapter(context, habilid));


                    projPrice.setText(project.getPrice());
                    if (project.getBid_count() == null){
                        bidCount.setText("0");
                    }else {
                        bidCount.setText(String.valueOf(project.getBid_count()));
                    }
                    bidCountStock = project.getBid_count();
                    detail.setText(project.getDescription());

                    if (type.equals("awarded")){
                        statusText.setText(type);
                        FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    awardFreelancerModel free = dataSnapshot.getValue(awardFreelancerModel.class);
                                    if (!free.getName().equals(curUser)){
                                        btnAccept.setVisibility(View.INVISIBLE);
                                        btnDecline.setVisibility(View.INVISIBLE);
                                    }
                                    FirebaseDatabase.getInstance().getReference("users").child(free.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() !=  null){
                                                freeChild = dataSnapshot.getKey();
                                                freeAccount = dataSnapshot.getValue(accountModel.class);
                                                ratingBar_free.setRating(Float.parseFloat(freeAccount.getFreelancer().getStars().replace(",", ".")));
                                                if (!getActivity().isFinishing()) {
                                                    Glide
                                                            .with(context)
                                                            .load(freeAccount.getPhoto_profile())
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .skipMemoryCache(false)
                                                            .into(freeicon);
                                                }
                                                nameFree.setText(freeAccount.getName());
                                                freecountry.setText(freeAccount.getCountry());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    else if (type.equals("in progress")){
                        statusText.setText(type);
                        FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    awardFreelancerModel free = dataSnapshot.getValue(awardFreelancerModel.class);
                                    if (project.getUserChild().equals(curUser)){
                                        btnFinish.setVisibility(View.VISIBLE);
                                    }
                                        btnAccept.setVisibility(View.INVISIBLE);
                                        btnDecline.setVisibility(View.INVISIBLE);
                                        btnCancel.setVisibility(View.INVISIBLE);


                                    FirebaseDatabase.getInstance().getReference("users").child(free.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() !=  null){
                                                freeChild = dataSnapshot.getKey();
                                                freeAccount = dataSnapshot.getValue(accountModel.class);
                                                ratingBar_free.setRating(Float.parseFloat(freeAccount.getFreelancer().getStars().replace(",", ".")));
                                                if (!getActivity().isFinishing()) {
                                                    Glide
                                                            .with(context)
                                                            .load(freeAccount.getPhoto_profile())
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .skipMemoryCache(false)
                                                            .into(freeicon);
                                                }
                                                nameFree.setText(freeAccount.getName());
                                                freecountry.setText(freeAccount.getCountry());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).child("bid").child(free.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null){
                                                bidModel myBid = dataSnapshot.getValue(bidModel.class);
                                                bidCountString = myBid.getBid();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    else if (type.equals("finished")){
                        statusText.setText(type);
                        FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    awardFreelancerModel free = dataSnapshot.getValue(awardFreelancerModel.class);

                                    btnAccept.setVisibility(View.INVISIBLE);
                                    btnDecline.setVisibility(View.INVISIBLE);
                                    btnCancel.setVisibility(View.INVISIBLE);
                                    btnFinish.setVisibility(View.INVISIBLE);


                                    FirebaseDatabase.getInstance().getReference("users").child(free.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() !=  null){
                                                freeChild = dataSnapshot.getKey();
                                                freeAccount = dataSnapshot.getValue(accountModel.class);
                                                ratingBar_free.setRating(Float.parseFloat(freeAccount.getFreelancer().getStars().replace(",", ".")));
                                                if (!getActivity().isFinishing()) {
                                                    Glide
                                                            .with(context)
                                                            .load(freeAccount.getPhoto_profile())
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .skipMemoryCache(false)
                                                            .into(freeicon);
                                                }

                                                nameFree.setText(freeAccount.getName());
                                                freecountry.setText(freeAccount.getCountry());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    FirebaseDatabase.getInstance().getReference("users").child(project.getUserChild()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() !=  null){
                                accountModel account = dataSnapshot.getValue(accountModel.class);
                                if (account.getClient().getStars() != null) {
                                    ratingBar.setRating(Float.parseFloat(account.getClient().getStars().replace(",", ".")));
                                }
                                if (!getActivity().isFinishing()) {
                                    Glide
                                            .with(context)
                                            .load(account.getPhoto_profile())
                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                            .skipMemoryCache(false)
                                            .into(icon);
                                }
                                nameAuthor.setText(account.getName());
                                country.setText(account.getCountry());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference("projects").child(bundle.getString("type")).child(bundle.getString("group")).child(bundle.getString("project")).child("bid").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                Log.d("VALUE", dataSnapshot.getValue().toString());
                                bidModel myBid = dataSnapshot.getValue(bidModel.class);

                                btnEnviar.setEnabled(false);
                                btnEnviar.setText("Sua Proposta: " + "R$"+ myBid.getBid());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        icon.setOnClickListener(f -> {
            if (project.getUserChild() != null){
                openAccount(project.getUserChild());
            }});

        freeicon.setOnClickListener(f -> {
            if (freeChild != null){
                openFreeAccount(freeChild);
            }});

        btnCancel.setOnClickListener(f -> {
            clickCancel();
        });

        btnDecline.setOnClickListener(f -> {
            clickDecline();
        });

        btnAccept.setOnClickListener(f -> {
            clickAccept();
        });

        btnFinish.setOnClickListener(f -> {
            clickFinish();
        });

        btnEnviar.setOnClickListener(f -> {
            clickEnviar();
        });
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void clickEnviar(){
       fManager = getFragmentManager().beginTransaction();

        Bundle bundle2 = new Bundle();
        bundle2.putString("project", bundle.getString("project"));
        bundle2.putString("group", bundle.getString("group"));
        bundle2.putString("bid_count", String.valueOf(project.getBid_count()));
        bundle2.putString("price", project.getPrice());
        createBidFragment fragment = new createBidFragment();
        bundle2.putString("own", "false");
        fragment.setArguments(bundle2);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(" ");
        fManager.commit();
    }

    public void clickFinish(){
        Float bid = Float.parseFloat(bidCountString);

        Bundle bundle2 = new Bundle();
        bundle2.putString("payment", String.valueOf(bid));
        bundle2.putString("group", bundle.getString("group"));
        bundle2.putString("project", bundle.getString("project"));
        bundle2.putString("projectuserchild", project.getUserChild());
        bundle2.putString("freeChild", freeChild);
        bundle2.putString("projectName", project.getName());

        choosePaymentFragment fragment = new choosePaymentFragment();
        fragment.setArguments(bundle2);
        fManager = getFragmentManager().beginTransaction();
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(" ");
        fManager.commit();
    }



    public void clickAccept(){

        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){

                    FirebaseDatabase.getInstance().getReference("projects").child("in progress").child(bundle.getString("group")).child(bundle.getString("project")).setValue(dataSnapshot.getValue());
                    FirebaseDatabase.getInstance().getReference("projects").child("in progress").child(bundle.getString("group")).child(bundle.getString("project")).child("bid_count").removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(project.getUserChild()).child("projects").child(bundle.getString("project")).child("status").setValue("in progress");
                    FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).removeValue();

                    FirebaseDatabase.getInstance().getReference("users").child(curUser).child("freeprojects").child(bundle.getString("project")).setValue(new userProjectModel("in progress",bundle.getString("group"), "", null));

                    FirebaseDatabase.getInstance().getReference("users").child(curUser).child("notification").child(bundle.getString("project")).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                accountModel userModel = dataSnapshot.getValue(accountModel.class);
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null){
                                            accountModel freeAcc = dataSnapshot.getValue(accountModel.class);
                                            token = freeAcc.getToken();
                                            sendMessage("LaborCash", userModel.getName() + " aceitou  o projeto " + project.getName(), "", "");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("users").child(project.getUserChild()).child("notification").child(bundle.getString("project")).setValue(new notificationModel(project.getName(), curUser, userModel.getName(), userModel.getPhoto_profile(), bundle.getString("group"), "accepted"));


                                FragmentTransaction fManager = getFragmentManager().beginTransaction();

                                Bundle bundle = new Bundle();
                                bundle.putString("type", "client");
                                tabsFragment tabsFragment = new tabsFragment();
                                tabsFragment.setArguments(bundle);
                                fManager = getFragmentManager().beginTransaction();
                                fManager.replace(R.id.mainFrame, tabsFragment);
                                fManager.addToBackStack(" ");
                                fManager.commit();
                                actionBar.setTitle("COMO EMPREGADOR");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickDecline(){
        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){

                    FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).setValue(dataSnapshot.getValue());
                    FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).child("freelancer").removeValue();
                    FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).child("bid_count").removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(project.getUserChild()).child("projects").child(bundle.getString("project")).child("status").setValue("open");
                    FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curUser).child("notification").child(bundle.getString("project")).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                accountModel userModel = dataSnapshot.getValue(accountModel.class);
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null){
                                            accountModel freeAcc = dataSnapshot.getValue(accountModel.class);
                                            token = freeAcc.getToken();
                                            sendMessage("LaborCash", userModel.getName() + " recusou o projeto " + project.getName(), "", "");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("users").child(project.getUserChild()).child("notification").child(bundle.getString("project")).setValue(new notificationModel(project.getName(), curUser, userModel.getName(), userModel.getPhoto_profile(), bundle.getString("group"), "declined"));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    FragmentTransaction fManager = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("type", "client");
                    tabsFragment tabsFragment = new tabsFragment();
                    tabsFragment.setArguments(bundle);
                    fManager = getFragmentManager().beginTransaction();
                    fManager.replace(R.id.mainFrame, tabsFragment);
                    fManager.addToBackStack(" ");
                    fManager.commit();
                    actionBar.setTitle("COMO EMPREGADOR");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickCancel(){
        String curChild = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).setValue(dataSnapshot.getValue());
                    FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).child("freelancer").removeValue();
                    FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).child("bid_count").removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curChild).child("projects").child(bundle.getString("project")).child("status").setValue("open");
                    FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(bundle.getString("group")).child(bundle.getString("project")).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curChild).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                accountModel curUser = dataSnapshot.getValue(accountModel.class);
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null){
                                            accountModel freeAcc = dataSnapshot.getValue(accountModel.class);
                                            token = freeAcc.getToken();
                                            sendMessage("LaborCash", curUser.getName() + " remova do projeto " + project.getName(), "", "");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).child("notification").child(bundle.getString("project")).setValue(new notificationModel(project.getName(), curChild, curUser.getName(), curUser.getPhoto_profile(), bundle.getString("group"), "canceled"));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    FragmentTransaction fManager = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("type", "client");
                    tabsFragment tabsFragment = new tabsFragment();
                    tabsFragment.setArguments(bundle);
                    fManager = getFragmentManager().beginTransaction();
                    fManager.replace(R.id.mainFrame, tabsFragment);
                    fManager.addToBackStack(" ");
                    fManager.commit();
                    actionBar.setTitle("COMO EMPREGADOR");
                }
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

    public void openAccount(String userChild){

        Intent intent = new Intent(context, clientOtherAccount.class);
        intent.putExtra("childname", userChild);
        intent.putExtra("project", bundle.getString("project"));
        intent.putExtra("group", bundle.getString("group"));
        intent.putExtra("type", bundle.getString("type"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openFreeAccount(String userChild){
        Intent intent = new Intent(context, freelancerOtherAccount.class);
        intent.putExtra("childname", userChild);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();
    public void sendMessage(final String title, final String body, final String icon, final String message) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("to", token);

                    String result = postToFCM(root.toString());

                    Log.d("1111111111111111", "Resultado: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
//                try {
//                    JSONObject resultJson = new JSONObject(result);
//                    int success, failure;
//                    success = resultJson.getInt("success");
//                    failure = resultJson.getInt("failure");
////                    Toast.makeText(getApplication(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
////                    Toast.makeText(getApplication(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
//                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=AAAASoPU9nw:APA91bFHXyo0Fn7UGDVQsHWJWB2GuGf3mTfWF5Ed2rm-Bq1mS3rfm0w0ABXdR50ZxRqMuxRKwyMTnP76MmOFG9YH7Oi9WqiRoOseMl6_3cZxJuaqBDGiEaaoNei8sNtZwjWtihXs_dWy")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


    @Override
    public void onResume() {
        super.onResume();
        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");

        Log.d("DESCRIÇÃO", "RESUMO");
    }




    public class skillAdapter extends BaseAdapter {

        private ArrayList<String> listData;
        private LayoutInflater layoutInflater;
        private Context context;

        public skillAdapter(Context aContext, ArrayList<String> list) {
            this.context = aContext;
            listData = list;
            layoutInflater = LayoutInflater.from(aContext);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.textview_layout, null);
                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.text);
//                holder.text.setBackgroundColor(Color.rgb(204,204,204));
                holder.text.setTextSize(10);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.text.setText(listData.get(position));


            return convertView;
        }

        // Find Image ID corresponding to the name of the image (in the directory mipmap).
        public int getMipmapResIdByName(String resName) {
            String pkgName = context.getPackageName();

            // Return 0 if not found.
            int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
            Log.i("CustomGridView", "Res Name: " + resName + "==> Res ID = " + resID);
            return resID;
        }

        public class ViewHolder {

            TextView text;

        }

    }

}
