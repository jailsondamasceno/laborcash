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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.chatActivity;
import luck.materialdesign.tabsnavigator.activities.freelancerAccount;
import luck.materialdesign.tabsnavigator.customViews.RatingBar;
import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.awardFreelancerModel;
import luck.materialdesign.tabsnavigator.model.bidModel;
import luck.materialdesign.tabsnavigator.model.feedbackModel;
import luck.materialdesign.tabsnavigator.model.userChatModel;

/**
 * Created by BeS on 17.09.2017.
 */

public class rateFreelancerFragment extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.editComment)
    EditText editComment;

    @BindView(R.id.btnSend)
    TextView btnSend;

    List<userChatModel> albumList;
    userChatModel dialog;

    String curUser;
    int countFeed = 0;
    int instCount;
   long sumRate;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.freelance_rate_layout, container, false);
        ButterKnife.bind(this, v);
        btnSend.setOnClickListener(f -> {
            FirebaseDatabase.getInstance().getReference("projects").child("finished").child(getArguments().getString("group")).child(getArguments().getString("project")).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        awardFreelancerModel freeAcc = dataSnapshot.getValue(awardFreelancerModel.class);
                        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
                        FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    accountModel user = dataSnapshot.getValue(accountModel.class);

                                  FirebaseDatabase.getInstance().getReference("users").child(curUser).child("notification").child(getArguments().getString("project")).removeValue();
                                    FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null){
                                                accountFreeModel freeModel = dataSnapshot.getValue(accountFreeModel.class);
                                                if (freeModel.getFeedback_count() != null && freeModel.getFeedback_count() != 0) {
                                                    FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").child("feedbacks").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                            if (dataSnapshot.getValue() != null) {
                                                                feedbackModel feedbackModel = dataSnapshot.getValue(feedbackModel.class);
                                                                countFeed++;
                                                                sumRate = sumRate + feedbackModel.getStars();



                                                                    if (countFeed == freeModel.getFeedback_count()) {


                                                                        FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").child("feedbacks").child(getArguments().getString("project")).setValue(new feedbackModel(curUser, user.getPhoto_profile(), editComment.getText().toString(), (long) rating.getRating()));
                                                                        sumRate = sumRate + (long) rating.getRating();
                                                                        countFeed++;
                                                                        float star = (float) sumRate / countFeed;
                                                                        Log.d("SUM_CHILD", String.valueOf(sumRate));
                                                                        Log.d("COUNT_CHILD", String.valueOf(countFeed));
                                                                        Log.d("RATING", String.valueOf(star));
                                                                        FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").child("stars").setValue(String.format("%.1f", star));
                                                                        FragmentTransaction fManager = getFragmentManager().beginTransaction();

                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString("type", "client");
                                                                        tabsFragment tabsFragment = new tabsFragment();
                                                                        tabsFragment.setArguments(bundle);
                                                                        fManager.replace(R.id.mainFrame, tabsFragment);
                                                                        fManager.addToBackStack(" ");
                                                                        fManager.commit();
                                                                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("COMO EMPREGADOR");
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
                                                }else{
                                                    FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").child("feedbacks").child(getArguments().getString("project")).setValue(new feedbackModel(curUser, user.getPhoto_profile(), editComment.getText().toString(), (long) rating.getRating()));
                                                    FirebaseDatabase.getInstance().getReference("users").child(freeAcc.getName()).child("freelancer").child("stars").setValue(String.valueOf(rating.getRating()));
                                                    FragmentTransaction fManager = getFragmentManager().beginTransaction();

                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("type", "client");
                                                    tabsFragment tabsFragment = new tabsFragment();
                                                    tabsFragment.setArguments(bundle);
                                                    fManager.replace(R.id.mainFrame, tabsFragment);
                                                    fManager.addToBackStack(" ");
                                                    fManager.commit();
                                                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("COMO EMPREGADOR");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    Log.d("SUM", String.valueOf(sumRate));
                                    Log.d("COUNT", String.valueOf(countFeed));
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Tabs", "onAttach");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}