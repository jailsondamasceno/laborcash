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
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.customViews.RatingBar;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.feedbackModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;
import luck.materialdesign.tabsnavigator.utils.Country;

import luck.materialdesign.tabsnavigator.utils.Util;

/**
 * Created by BeS on 08.09.2017.
 */

public class clientAccount extends AppCompatActivity  implements AppBarLayout.OnOffsetChangedListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @BindView(R.id.iconClient)
    CircleImageView icon;

    @BindView(R.id.userName)
    TextView name;

    @BindView(R.id.flag)
    CircleImageView flag;

    @BindView(R.id.sobreEdit)
    CircleImageView btnSobreEdit;

    @BindView(R.id.aboutMeText)
    ExpandableTextView sobreText;

    @BindView(R.id.countryName)
    TextView countryName;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.countPublished)
    TextView countPublish;

    @BindView(R.id.countRating)
    TextView countRating;

    @BindView(R.id.countReview)
    TextView countReview;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;


    List<feedbackModel> albumList;
    Dialog dialog;
    Toolbar toolbar;
    Uri picUri;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    boolean isShow = false;
    int scrollRange;
    String childName;
    String userName;
    accountModel user;
    int publish = 0;
    AppBarLayout appBarLayout;
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_layout);
        ButterKnife.bind(this);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        bindActivity();

        mAppBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels/2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setStateListAnimator(null);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        albumList = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbar.setTitle("");
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
            nc.add(c);
        }
        childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("users").child(childName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    user = dataSnapshot.getValue(accountModel.class);
                    userName = user.getName();
                    name.setText(userName);
                    countryName.setText(user.getCountry());
                    flag.setImageResource(Country.getCountryByName(user.getCountry()).getFlag());
                    sobreText.setText(user.getAboutInfo());
                    Glide
                            .with(getBaseContext())
                            .load(user.getPhoto_profile())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .skipMemoryCache(false)
                            .into(icon);
                    countRating.setText(user.getClient().getStars());
                    countReview.setText("("+String.valueOf(user.getClient().getFeedback_count())+")");
                    ratingBar.setRating(Float.parseFloat(user.getClient().getStars().replace(",",".")));
                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("client").child("feedbacks").addChildEventListener(new ChildEventListener() {
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


                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("projects").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue() != null){
                                userProjectModel proj = dataSnapshot.getValue(userProjectModel.class);
                                publish++;
                                countPublish.setText(String.valueOf(publish));

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

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset + scrollRange == 0 && !isShow){
                    collapsingToolbar.setTitle(userName);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setTitle(userName);
                    toolbar.setTitleTextColor(Color.WHITE);
                    isShow = true;
                }else if (verticalOffset + scrollRange != 0 && isShow){
                    collapsingToolbar.setTitle("");
                    toolbar.setVisibility(View.INVISIBLE);
                    isShow = false;


                }

            }
        });

        btnSobreEdit.setOnClickListener(f ->{
            setSobreDialog();
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


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    public void setSobreDialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sobre_layout, (ViewGroup) findViewById(R.id.dialog));
        dialog.setContentView(layout);

        dialog.setTitle("sobre mim!");


        EditText text = (EditText) layout.findViewById(R.id.editSobre);
        TextView btnChange = (TextView)layout.findViewById(R.id.btnChange);
        btnChange.setOnClickListener(s -> {
            FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().replace(".", "(dot)")).child("aboutInfo").setValue(text.getText().toString());
            dialog.hide();
            recreate();
        });

        dialog.show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindActivity() {
//        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_framelayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child(Util.FOLDER_STORAGE_IMG);
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

    private void sendFileFirebase(StorageReference storageReference, final Uri file){
        if (storageReference != null && file != null){
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name+"_gallery");
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

}
