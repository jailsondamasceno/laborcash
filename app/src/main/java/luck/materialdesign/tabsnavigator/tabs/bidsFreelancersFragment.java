package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.chatActivity;
import luck.materialdesign.tabsnavigator.activities.clientOtherAccount;
import luck.materialdesign.tabsnavigator.activities.freelancerOtherAccount;
import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.bidModel;
import luck.materialdesign.tabsnavigator.model.chat.ChatModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import luck.materialdesign.tabsnavigator.model.projectModel;
import luck.materialdesign.tabsnavigator.model.userChatModel;
import luck.materialdesign.tabsnavigator.sliding.SlidingTabLayout;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by BeS on 15.09.2017.
 */

public class bidsFreelancersFragment extends Fragment {
//    @BindView(R.id.tabs_tool_bar)
//    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<bidModel> albumList;
    accountModel user;
    projectModel projectModel;
    String project;
    String group;
    bidModel bidModel;
    String type;
    Context context;
    String token;

    ArrayList<String> userChilds = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    public static bidsFreelancersFragment newInstance(int position, String group, String project, String type) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        args.putString("type", type);
        args.putString("group", group);
        args.putString("project", project);
        bidsFreelancersFragment fragment = new bidsFreelancersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        ButterKnife.bind(this, v);
        group = getArguments().getString("group");
        project = getArguments().getString("project");
        type = getArguments().getString("type");
        albumList = new ArrayList<>();
        context = getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase.getInstance().getReference("projects").child(type).child(group).child(project).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    projectModel = dataSnapshot.getValue(projectModel.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("projects").child(type).child(group).child(project).child("bid").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    userChilds.add(dataSnapshot.getKey());
                    bidModel = dataSnapshot.getValue(bidModel.class);
                    albumList.add(bidModel);
                    recyclerView.setAdapter(new freelancerAdapter(getContext(), albumList));
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

    public void openAccount(String userChild){
        Intent intent = new Intent(context, freelancerOtherAccount.class);
        intent.putExtra("childname", userChild);
        context.startActivity(intent);
    }

    public void createChat(String user1Child, String user1Name, String user1Icon, String user2Child, String user2Name, String user2Icon){
        String resChild;
        if (user1Child.length() > user2Child.length()){
            resChild = user1Child + user2Child;
        }else{
            resChild = user2Child + user1Child;
        }
        FirebaseDatabase.getInstance().getReference("users").child(user1Child).child("dialogs").child(user2Child).setValue(new userChatModel(resChild, user2Name, user2Icon, "Started a new conversation"));
        FirebaseDatabase.getInstance().getReference("users").child(user2Child).child("dialogs").child(user1Child).setValue(new userChatModel(resChild, user1Name, user1Icon, "Started a new conversation"));

        Intent intent = new Intent(context, chatActivity.class);
        intent.putExtra("childChat", resChild);
        intent.putExtra("user1", user1Child);
        intent.putExtra("user2", user2Child);
        intent.putExtra("title", user1Name);
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

                    Log.d("1111111111111111", "Result: " + result);
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
                .addHeader("Authorization", "key=AAAAixRvF3g:APA91bFv99IaUGzYsr0EkheT1Y1Mbd_weT8KdIdjH2ub8gZaYf8DCh_H0_vPMjpYPbn7Zz8ZmOeeDN3YbVZ4_vyF3gHDOyhW9xCgsJ6xaPcHPhM17IADb-v23Lx0b4O9Xyhre8KoyQun")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


    public class freelancerAdapter  extends RecyclerView.Adapter<freelancerAdapter.MyViewHolder> {

        private Context mContext;
        private List<bidModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView bid;
            private TextView message;
            private TextView btnHire;
            private  TextView btnMessage;
            private CircleImageView icon;
            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.userName);
                bid = (TextView)view.findViewById(R.id.bid);
                icon = (CircleImageView) view.findViewById(R.id.iconFreelance);
                message = (TextView)view.findViewById(R.id.message);
                btnHire = (TextView)view.findViewById(R.id.btnHire);
                btnMessage = (TextView)view.findViewById(R.id.btnWrite);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public freelancerAdapter(Context mContext,  List<bidModel> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bids_freelancers_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final bidModel album = albumList.get(position);
            holder.name.setText(album.getUser());
            holder.bid.setText(album.getBid() + " R$");
            holder.message.setText(album.getMessage());
            if (!type.equals("open")){
                holder.btnHire.setVisibility(View.INVISIBLE);
            }

            Glide
                    .with(mContext)
                    .load(album.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(false)
                    .into(holder.icon);

            holder.cardView.setOnClickListener(f ->{
                openAccount(userChilds.get(position));
            });

            holder.btnMessage.setOnClickListener(r -> {
                String curChild = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
                FirebaseDatabase.getInstance().getReference("users").child(curChild).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            accountModel curUser = dataSnapshot.getValue(accountModel.class);
                            createChat(userChilds.get(position), albumList.get(position).getUser(), albumList.get(position).getIcon(), curChild, curUser.getName(), curUser.getPhoto_profile());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            });

            holder.btnHire.setOnClickListener(f -> {
                String curChild = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
                FirebaseDatabase.getInstance().getReference("projects").child("open").child(group).child(project).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            FirebaseDatabase.getInstance().getReference("users").child(curChild).child("projects").child(project).child("paidComis").setValue("false");
                            FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(group).child(project).setValue(dataSnapshot.getValue());
                            FirebaseDatabase.getInstance().getReference("projects").child("awarded").child(group).child(project).child("freelancer").child("name").setValue(userChilds.get(position));
                            FirebaseDatabase.getInstance().getReference("users").child(curChild).child("projects").child(project).child("status").setValue("awarded");
                            FirebaseDatabase.getInstance().getReference("projects").child("open").child(group).child(project).removeValue();
                            FirebaseDatabase.getInstance().getReference("users").child(curChild).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null){
                                        accountModel curUser = dataSnapshot.getValue(accountModel.class);
                                        FirebaseDatabase.getInstance().getReference("users").child(userChilds.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() != null){
                                                    accountModel freeAcc = dataSnapshot.getValue(accountModel.class);
                                                    token = freeAcc.getToken();
                                                    sendMessage("LaborCash", curUser.getName() + " você foi premiado para o " + projectModel.getName(), "", "");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference("users").child(userChilds.get(position)).child("notification").child(project).setValue(new notificationModel(projectModel.getName(), curChild, projectModel.getAuthor(), curUser.getPhoto_profile(), group, "awarded"));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            FragmentTransaction fManager = getFragmentManager().beginTransaction();
//                            getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "client");
                            tabsFragment tabsFragment = new tabsFragment();
                            tabsFragment.setArguments(bundle);

                            fManager.replace(R.id.mainFrame, tabsFragment);
                            fManager.addToBackStack(" ");
                            fManager.commit();
                            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("COMO EMPREGADOR");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            });
//            holder.cardView.setOnClickListener(f ->{
//                onRecyclerClick(projects.get(position), albumList.get(position).getType());
//            });

        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }

    }
}
