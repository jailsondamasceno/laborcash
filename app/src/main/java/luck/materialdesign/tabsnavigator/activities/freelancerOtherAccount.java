package luck.materialdesign.tabsnavigator.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.StaggeredTextGridView;
import luck.materialdesign.tabsnavigator.customViews.RatingBar;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.feedbackModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import luck.materialdesign.tabsnavigator.model.userChatModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;
import luck.materialdesign.tabsnavigator.tabs.notificationFragment;
import luck.materialdesign.tabsnavigator.tabs.projectDescriptionAuthorFragment;
import luck.materialdesign.tabsnavigator.tabs.projectDescriptionFragment;
import luck.materialdesign.tabsnavigator.tabs.rateClientFragment;
import luck.materialdesign.tabsnavigator.tabs.rateFreelancerFragment;
import luck.materialdesign.tabsnavigator.utils.Country;


/**
 * Created by BeS on 11.09.2017.
 */

public class freelancerOtherAccount extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    boolean isShow = false;
    int scrollRange;
    String userName;
    String childName;
    String firebaseSkill;

    ArrayList<String> habilid = new ArrayList<>();
    ArrayList<String> Skill = new ArrayList<>();
    ArrayList<String> mySkill = new ArrayList<>();

    @BindView(R.id.iconFreelance)
    CircleImageView icon;

    @BindView(R.id.userName)
    TextView name;

    @BindView(R.id.skillEdit)
    ImageView btnEditSkill;

    @BindView(R.id.skill)
    TextView skillName;

    @BindView(R.id.countryName)
    TextView countryName;

    @BindView(R.id.flag)
    CircleImageView flag;

    @BindView(R.id.countRating)
    TextView countRating;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.countFinish)
    TextView countFinish;

    @BindView(R.id.countCurrent)
    TextView countCurrent;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.countReview)
    TextView countReview;

    @BindView(R.id.aboutMeText)
    ExpandableTextView sobreText;

    @BindView(R.id.sobreEdit)
    CircleImageView btnSobreEdit;

    @BindView(R.id.gridNoted)
    StaggeredTextGridView gridSkill;

    @BindView(R.id.habilidEdit)
    CircleImageView btnHabilidEdit;

    @BindView(R.id.btnWrite)
    TextView btnWrite;



    @BindView(R.id.card_view_budget)
    CardView cardBudget;

    int finish, current, rating;
    accountModel user;
    Dialog dialog;
    Toolbar toolbar;
    List<feedbackModel> albumList;
    feedbackModel feedmodel;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freelancer_layout);
        ButterKnife.bind(this);
        bindActivity();
        Intent intent = getIntent();
        btnEditSkill.setVisibility(View.INVISIBLE);
        btnHabilidEdit.setVisibility(View.INVISIBLE);
        btnSobreEdit.setVisibility(View.INVISIBLE);

        btnWrite.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                0);
        lp2.addRule(RelativeLayout.BELOW, R.id.skillArea);
        cardBudget.setLayoutParams(lp2);
        cardBudget.setVisibility(View.INVISIBLE);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbar.setTitle("");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        mAppBarLayout.addOnOffsetChangedListener(this);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        albumList = new ArrayList<>();
        mAppBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels / 2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setStateListAnimator(null);
        }
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
            nc.add(c);
        }
        childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
        FirebaseDatabase.getInstance().getReference("users").child(intent.getStringExtra("childname")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue() != null) {
                        user = dataSnapshot.getValue(accountModel.class);
                        userName = user.getName();
                        flag.setImageResource(Country.getCountryByName(user.getCountry()).getFlag());
                        countryName.setText(user.getCountry());
                        firebaseSkill = user.getFreelancer().getSkills();
                        String[] skill = firebaseSkill.split(",");
                        habilid.add(" ");
                        for (String s : skill) {
                            habilid.add(s + "  ");
                            Log.d("SIZE", String.valueOf(habilid.size()));
                            Log.d("STRING", habilid.toString());
                            Log.d("SKIIIIIL", s + "    ");
                        }
                        sobreText.setText(user.getAboutInfo());
                        ratingBar.setRating(Float.parseFloat(user.getFreelancer().getStars().replace(",", ".")));
                        countRating.setText(user.getFreelancer().getStars());
                        if (user.getFreelancer().getFeedback_count() != null) {
                            countReview.setText("(" + String.valueOf(user.getFreelancer().getFeedback_count()) + ")");
                        } else {
                            countReview.setText("(0)");
                        }

                        gridSkill.setmAdapter(new skillAdapter(getBaseContext(), habilid));

                        if (user.getFreelancer().getSpecgroup().equals("projetos abertos esta semana")) {
                            skillName.setText("...");
                        } else {
                            skillName.setText(user.getFreelancer().getSpecgroup());
                        }

                        name.setText(userName);
                        Glide
                                .with(getBaseContext())
                                .load(user.getPhoto_profile())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .skipMemoryCache(false)
                                .into(icon);


                        countCurrent.setText("0");
                        countFinish.setText("0");
                        FirebaseDatabase.getInstance().getReference("users").child(intent.getStringExtra("childname")).child("freelancer").child("feedbacks").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue() != null) {
                                    feedbackModel feedback = dataSnapshot.getValue(feedbackModel.class);
                                    albumList.add(feedback);
                                    recyclerView.setAdapter(new feedbackAdapter(getBaseContext(), albumList));
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
                        FirebaseDatabase.getInstance().getReference("users").child(intent.getStringExtra("childname")).child("freeprojects").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue() != null) {
                                    userProjectModel proj = dataSnapshot.getValue(userProjectModel.class);
                                    if (proj.getStatus().equals("finished")) {
                                        finish++;
                                        countFinish.setText(String.valueOf(finish));
                                    } else if (proj.getStatus().equals("in progress")) {
                                        current++;
                                        countCurrent.setText(String.valueOf(current));
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
                }else{
                    Toast.makeText(getBaseContext(), "conta excluída e", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnWrite.setOnClickListener(f -> {
            String curChild = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
            FirebaseDatabase.getInstance().getReference("users").child(curChild).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        accountModel curUser = dataSnapshot.getValue(accountModel.class);
                        createChat(intent.getStringExtra("childname"), user.getName(), user.getPhoto_profile(), curChild, curUser.getName(), curUser.getPhoto_profile());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset + scrollRange == 0 && !isShow) {
                    collapsingToolbar.setTitle(userName);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setTitle(userName);
                    toolbar.setTitleTextColor(Color.WHITE);
                    isShow = true;
                } else if (verticalOffset + scrollRange != 0 && isShow) {
                    collapsingToolbar.setTitle("");
                    toolbar.setVisibility(View.INVISIBLE);
                    isShow = false;


                }

            }
        });

    }

    private void bindActivity() {
//        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_framelayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
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

        Intent intent = new Intent(this, chatActivity.class);
        intent.putExtra("childChat", resChild);
        intent.putExtra("user1", user1Child);
        intent.putExtra("user2", user2Child);
        intent.putExtra("title", user1Name);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
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

    public class CustomGridAdapter extends BaseAdapter {

        private ArrayList<String> listData;
        private LayoutInflater layoutInflater;
        private Context context;
        private String curText;

        public CustomGridAdapter(Context aContext, ArrayList<String> list) {
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


    public class feedbackAdapter  extends RecyclerView.Adapter<feedbackAdapter.MyViewHolder> {

        private Context mContext;
        private List<feedbackModel> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView message;
            private CircleImageView icon;
            private RatingBar rating;
            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                icon = (CircleImageView) view.findViewById(R.id.icon);
                message = (TextView)view.findViewById(R.id.text);
                cardView = (CardView)view.findViewById(R.id.card_view);
                rating = (RatingBar)view.findViewById(R.id.rating);
            }
        }


        public feedbackAdapter(Context mContext,  List<feedbackModel> albumList) {
            this.mContext = mContext;
            this.albumList = albumList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feedback_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final feedbackModel album = albumList.get(position);

            holder.message.setText(album.getFeedback());
            holder.rating.setRating(album.getStars());
            Glide
                    .with(mContext)
                    .load(album.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(false)
                    .into(holder.icon);
            holder.cardView.setOnClickListener(f ->{
            });
        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }

    }

}
