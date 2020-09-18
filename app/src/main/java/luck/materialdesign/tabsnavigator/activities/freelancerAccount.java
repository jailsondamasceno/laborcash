package luck.materialdesign.tabsnavigator.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.StaggeredTextGridView;
import luck.materialdesign.tabsnavigator.customViews.RatingBar;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.chat.ChatModel;
import luck.materialdesign.tabsnavigator.model.chat.FileModel;
import luck.materialdesign.tabsnavigator.model.feedbackModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;
import luck.materialdesign.tabsnavigator.model.withdrawRequestModel;
import luck.materialdesign.tabsnavigator.utils.Country;

import luck.materialdesign.tabsnavigator.utils.Util;

/**
 * Created by BeS on 11.09.2017.
 */

public class freelancerAccount extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, GoogleApiClient.OnConnectionFailedListener{
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    boolean isShow = false;
    int scrollRange;
    String userName;
    String childName;
    String firebaseSkill;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<String> habilid = new ArrayList<>();

    ArrayList<String> Skill = new ArrayList<>();
    ArrayList<String> mySkill = new ArrayList<>();

    @BindView(R.id.iconFreelance)
    CircleImageView icon;

    @BindView(R.id.userName)
    TextView name;

    @BindView(R.id.skillEdit)
    ImageView btnEditSkill;

    @BindView(R.id.countRating)
    TextView countRating;

    @BindView(R.id.countFinish)
    TextView countFinish;

    @BindView(R.id.countCurrent)
    TextView countCurrent;

    @BindView(R.id.skill)
    TextView skillName;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.countryName)
    TextView countryName;

    @BindView(R.id.flag)
    CircleImageView flag;

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

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btnWithdraw)
    TextView btnWithdraw;

    @BindView(R.id.budgetCount)
    TextView budget;

    @BindView(R.id.card_view_budget)
    CardView cardBudget;

    List<feedbackModel> albumList;
    feedbackModel feedmodel;
    accountModel user;
    Dialog dialog;
    Uri picUri;
    int finish, current, rating;
    Toolbar toolbar;
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
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        albumList = new ArrayList<>();
        mAppBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels / 2));
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setStateListAnimator(null);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
            nc.add(c);
        }



        childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
        FirebaseDatabase.getInstance().getReference("users").child(childName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    user = dataSnapshot.getValue(accountModel.class);
                    userName = user.getName();

                    flag.setImageResource(Country.getCountryByName(user.getCountry()).getFlag());

                    countryName.setText(user.getCountry());

                    firebaseSkill = user.getFreelancer().getSkills();
                    String[] skill = firebaseSkill.split(",");
                    habilid.add(" ");
                    for(String s : skill){
                        habilid.add(s+"  ");
                        Log.d("SIZE", String.valueOf(habilid.size()));
                        Log.d("STRING", habilid.toString());
                        Log.d("SKIIIIIL", s+ "    ");
                    }

                    budget.setText(user.getFreelancer().getBudget() + " R$");
                    sobreText.setText(user.getAboutInfo());
                    ratingBar.setRating(Float.parseFloat(user.getFreelancer().getStars().replace(",",".")));
                    countRating.setText(user.getFreelancer().getStars());
                    if (user.getFreelancer().getFeedback_count() != null) {
                        countReview.setText("("+String.valueOf(user.getFreelancer().getFeedback_count()) + ")");
                    }else{
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

                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("freelancer").child("feedbacks").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue() != null){
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

                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("freeprojects").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue() != null){
                                userProjectModel proj = dataSnapshot.getValue(userProjectModel.class);
                                if (proj.getStatus().equals("finished")){
                                    finish++;
                                    countFinish.setText(String.valueOf(finish));
                                }
                                else if (proj.getStatus().equals("in progress")){
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnWithdraw.setOnClickListener(f -> {
            setWithdrawDialog();
        });

        btnEditSkill.setOnClickListener(f -> {
            showDialog();
        });

        btnHabilidEdit.setOnClickListener(f ->{
            setSkillDialog();
        });

        btnSobreEdit.setOnClickListener(f ->{
            setSobreDialog();
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

        icon.setOnClickListener(f ->{
            Intent intent = CropImage.activity(picUri)
                    .setAspectRatio(1,1)
                    .setAutoZoomEnabled(false)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setInitialCropWindowPaddingRatio(0)
                    .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                    .getIntent(this);
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        });

    }

    private void bindActivity() {
//        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_framelayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    public void setWithdrawDialog(){
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.withdraw_layout, (ViewGroup) findViewById(R.id.dialog));
        dialog.setContentView(layout);
        dialog.setTitle("Solicitação de retirada de dinheiro");

        EditText editBudget = (EditText)layout.findViewById(R.id.editBudget);
        TextView btnWithdraw = (TextView)layout.findViewById(R.id.btnWithdraw);
        Spinner typeWithdraw = (Spinner)layout.findViewById(R.id.typeWithdraw);

        btnWithdraw.setOnClickListener(f -> {
            if (editBudget.getText().length()>1) {
                if (Float.parseFloat(editBudget.getText().toString()) <= Float.parseFloat(budget.getText().toString().replace(" R$", ""))) {
                    if (Float.parseFloat(editBudget.getText().toString()) >= 30) {
                        childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
                        FirebaseDatabase.getInstance().getReference("Withdraw Requests").child(childName).setValue(new withdrawRequestModel(String.valueOf(DateFormat.format("dd.MM", System.currentTimeMillis())), editBudget.getText().toString(), typeWithdraw.getSelectedItem().toString()));
                        Toast.makeText(getBaseContext(), "Pedido enviado", Toast.LENGTH_LONG).show();
                        dialog.hide();
                    } else {
                        Toast.makeText(getBaseContext(), "A soma deve ser de pelo menos 30 R$", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Incorreta a soma", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getBaseContext(), "A soma deve ser de pelo menos 30 R$", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void setSkillDialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.search_layout, (ViewGroup) findViewById(R.id.dialog));
        dialog.setContentView(layout);

        dialog.setTitle("editar habilidades");
        ArrayList<String> mList = new ArrayList<>();
        String[] skills;
        if (user.getFreelancer().getSpecgroup().equals("DESIGN E MULTIMIDIA")) {
            skills = getResources().getStringArray(R.array.design);
        } else if (user.getFreelancer().getSpecgroup().equals("MARKETING E VENDAS")) {
            skills = getResources().getStringArray(R.array.sales);
        } else if (user.getFreelancer().getSpecgroup().equals("PROGRAMAÇÃO E TI")) {
            skills = getResources().getStringArray(R.array.it_skills_array);
        } else if (user.getFreelancer().getSpecgroup().equals("CONSTRUÇÃO CIVIL")) {
            skills = getResources().getStringArray(R.array.itens_construcao_civil);
        } else if (user.getFreelancer().getSpecgroup().equals("AUDIOVISUAL")) {
            skills = getResources().getStringArray(R.array.itens_audiovisual);
        } else if (user.getFreelancer().getSpecgroup().equals("AUTOMOVEIS")) {
            skills = getResources().getStringArray(R.array.itens_automoveis);
        } else if (user.getFreelancer().getSpecgroup().equals("GASTRONOMIA")) {
            skills = getResources().getStringArray(R.array.itens_gastronomia);
        } else  {
            skills = getResources().getStringArray(R.array.writingAndcontent);
        }


        TextView btnChange = (TextView)layout.findViewById(R.id.btnChange);
        MultiAutoCompleteTextView skillText = (MultiAutoCompleteTextView)layout.findViewById(R.id.searchRow);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, skills);

        skillText.setAdapter(adapter);
        skillText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//        if (user.getFreelancer().getSkills().length() > 1) {
            skillText.setText(user.getFreelancer().getSkills().trim());
//        }

        skillText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] newAdd =skillText.getText().toString().split(",");

                Set<String> uniquUsers = new HashSet<String>();

                for (int i = 0; i < newAdd.length; i++) {
                    if (!uniquUsers.add(newAdd[i].trim()))
                        newAdd[i] = "Duplicate"; // here I am assigning Duplicate instead if find duplicate
                    // you can assign as null or whatever you want to do with duplicates.
                }

                String si = "";

                for (String s : newAdd){
                    if (!s.equals("Duplicate")){
                        String szap = s.trim() + ",";
                        si = si + szap;
                        Log.d("S", s);
                        Log.d("SII", si);
                    }
                }
                skillText.setText(si);

            }
        });

        btnChange.setOnClickListener(f -> {
            String[] res = skillText.getText().toString().trim().split(",");
            List<String> list = Arrays.asList(skills);
            String result = " ";
            for( String ss : res) {

                if (list.contains(ss)) {
                    result = result + ss + ",";
                }
            }
            FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().replace(".", "(dot)")).child("freelancer").child("skills").setValue(result);
            dialog.hide();
            recreate();
        });

        dialog.show();
    }

    public void setSobreDialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sobre_layout, (ViewGroup) findViewById(R.id.dialog));
        dialog.setContentView(layout);

        dialog.setTitle("información acerca de mí");


        EditText text = (EditText) layout.findViewById(R.id.editSobre);
        TextView btnChange = (TextView)layout.findViewById(R.id.btnChange);
        text.setText(user.getAboutInfo());
        btnChange.setOnClickListener(s -> {
            FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().replace(".", "(dot)")).child("aboutInfo").setValue(text.getText().toString());
            dialog.hide();
            recreate();
        });

        dialog.show();
    }


    public void showDialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.select_skill_layout, (ViewGroup) findViewById(R.id.dialog));
        dialog.setContentView(layout);

        dialog.setTitle("mudar de grupo");

        Spinner skills = (Spinner)layout.findViewById(R.id.skills);
        TextView btnChange = (TextView)layout.findViewById(R.id.btnChange);

        btnChange.setOnClickListener(s -> {
            FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().replace(".", "(dot)")).child("freelancer").child("specgroup").setValue(skills.getSelectedItem());
            dialog.hide();
            recreate();
        });

        dialog.show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
        Util.initToast(this,"Google Play Services error.");
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child("AccountImage");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Log.d("RESULT", "OK");
                Uri resultUri = result.getUri();
                if (resultUri != null) {
                    sendFileFirebase(storageRef, resultUri);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void sendFileFirebase(StorageReference storageReference, final Uri file){
        if (storageReference != null && file != null){
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(childName);
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG","onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Progress", String.valueOf(taskSnapshot.getBytesTransferred())+"/"+String.valueOf(taskSnapshot.getTotalByteCount()));


                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("TAG","onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("photo_profile").setValue(downloadUrl.toString());
                    recreate();
                }
            });
        }else{
            Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();
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

}
