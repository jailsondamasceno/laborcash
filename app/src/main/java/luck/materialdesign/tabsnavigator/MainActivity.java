package luck.materialdesign.tabsnavigator;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import luck.materialdesign.tabsnavigator.activities.clientAccount;
import luck.materialdesign.tabsnavigator.activities.createProjectActivity;

import luck.materialdesign.tabsnavigator.activities.findFreelancerActivity;
import luck.materialdesign.tabsnavigator.activities.freelancerAccount;



import luck.materialdesign.tabsnavigator.customViews.ScrimInsetsFrameLayout;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.sliding.SlidingTabLayout;

import luck.materialdesign.tabsnavigator.tabs.ViewPagerAdapter;
import luck.materialdesign.tabsnavigator.tabs.categoryFragment;
import luck.materialdesign.tabsnavigator.tabs.configFragment;
import luck.materialdesign.tabsnavigator.tabs.emptyMockupFragment;
import luck.materialdesign.tabsnavigator.tabs.findFreelancerCategoryFragment;
import luck.materialdesign.tabsnavigator.tabs.messagesFragment;
import luck.materialdesign.tabsnavigator.tabs.notificationFragment;
import luck.materialdesign.tabsnavigator.tabs.panelDeControlFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;
import luck.materialdesign.tabsnavigator.utils.GET_CURRENT_LOCATION;
import luck.materialdesign.tabsnavigator.utils.UtilsDevice;
import luck.materialdesign.tabsnavigator.utils.UtilsMiscellaneous;
import luck.materialdesign.tabsnavigator.utils.adapter;

/**
 * Criado por Jailson Araujo em 10/2017.
 */
public class MainActivity extends AppCompatActivity{


    // Declaring Your View and Variables
    @BindView(R.id.tool_bar)
    Toolbar toolbar;


    accountModel user;
    String curUser;
    String token;
    String group;
    boolean state = false;

    @BindView(R.id.iconUser)
    CircleImageView iconUser;

    @BindView(R.id.nameUser)
    TextView nameUser;
    ActionBar actionBar;
    ArrayList<String> TitlesMan = new ArrayList<>();
    public static final int MY_PERMISSIONS = 1001;
    GET_CURRENT_LOCATION myLocation;
    TextView aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);


        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        myLocation = new GET_CURRENT_LOCATION(this);


        SharedPreferences sPref = getPreferences(MODE_PRIVATE);


        group = sPref.getString("group", "");
        token = FirebaseInstanceId.getInstance().getToken();


        FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue() != null && state) {
                        FirebaseDatabase.getInstance().getReference("users").child(curUser).child("token").setValue(token);
                        user = dataSnapshot.getValue(accountModel.class);
                        if (user.getFreelancer().getSpecgroup() != null && !user.getFreelancer().getSpecgroup().equals("não selecionado")) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations").child(user.getFreelancer().getSpecgroup());
                            GeoFire geoFire = new GeoFire(ref);
                            geoFire.setLocation(curUser, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()));

                            if (sPref.getString("group", "") != null && user.getFreelancer().getSpecgroup() != null) {
                                if (!sPref.getString("group", "").equals(user.getFreelancer().getSpecgroup())) {
                                    FirebaseDatabase.getInstance().getReference("locations").child(sPref.getString("group", "")).child(curUser).removeValue();
                                    SharedPreferences.Editor ed = sPref.edit();
                                    group = user.getFreelancer().getSpecgroup();
                                    ed.putString("group", user.getFreelancer().getSpecgroup());
                                    ed.commit();
                                }
                            }


                        }
                        showFragment();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showFragment(){

        Log.d("SHOW FRAGMENT", "TRUE");
        init_navigator();

        if (getIntent().getStringExtra("type") != null && state) {
            if (getIntent().getStringExtra("type").equals("true")) {
                FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("type", "client");
                tabsFragment tabsFragment = new tabsFragment();
                tabsFragment.setArguments(bundle);
                                    new Handler().post(new Runnable() {
                                        public void run() {
                fManager.replace(R.id.mainFrame, tabsFragment);
                fManager.commit();
                                        }
                                    });

                actionBar.setTitle("COMO EMPREGADOR");
            } else if (getIntent().getStringExtra("type").equals("false")){
                FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("type", "freelancer");
                tabsFragment tabsFragment = new tabsFragment();
                tabsFragment.setArguments(bundle);
                                    new Handler().post(new Runnable() {
                                        public void run() {
                fManager.replace(R.id.mainFrame, tabsFragment);
                fManager.commit();
                                        }
                                    });

                actionBar.setTitle("COMO FREELANCER");
            }
        }
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public static String removePunct(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void finish() {
        super.finish();
        FirebaseDatabase.getInstance().getReference("locations").child(group).child(curUser).removeValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        int id = item.getItemId();

////        noinspection SimplifiableIfStatement
//        if (id == R.id.menuBasket) {
//
//        }
        return super.onOptionsItemSelected(item);
    }


    public  String getCurUser() {
        return curUser;
    }
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    @Override
    protected void onPause() {
        super.onPause();
        state = false;
        Log.d("MAIN ACTIVITY", "OnPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = true;
        Log.d("MAIN ACTIVITY", "OnResume");
        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("users").child(curUser).child("lastActive").setValue(Calendar.getInstance().getTime().getTime()+"");
    }

    private void init_navigator(){


        setSupportActionBar(toolbar);

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));

        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbar,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                )
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                // Disables the burger/arrow animation by default

                    super.onDrawerSlide(drawerView, 0);

            }
        };

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);
        // Set the first item as selected for the first time
//        getSupportActionBar().setTitle(R.string.toolbar_title_home);

        Glide
                .with(getApplicationContext())
                .load(user.getPhoto_profile())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(false)
                .into(iconUser);

        nameUser.setText(user.getName());

        actionBar = getSupportActionBar();

        RelativeLayout framePerfilFree = (RelativeLayout)findViewById(R.id.framePerfilFree);
        framePerfilFree.setOnClickListener(f -> {
            startActivity(new Intent(MainActivity.this, freelancerAccount.class));
        });

        RelativeLayout framePerfilClient = (RelativeLayout)findViewById(R.id.framePerfilClient);
        framePerfilClient.setOnClickListener(f -> {
            startActivity(new Intent(MainActivity.this, clientAccount.class));
        });

        RelativeLayout frameCreateProj = (RelativeLayout)findViewById(R.id.frameNewProj);
        frameCreateProj.setOnClickListener(f -> {
            startActivity(new Intent(MainActivity.this, createProjectActivity.class));
        });

        RelativeLayout frameSearchProj = (RelativeLayout)findViewById(R.id.frameSearch);
        frameSearchProj.setOnClickListener(f -> {
            setFragment("search", "PESQUISA DE PROJETOS", " ", new tabsFragment());

        });

        RelativeLayout frameSearchFree = (RelativeLayout)findViewById(R.id.frameSearchFree);
        frameSearchFree.setOnClickListener(f -> {
            if(checkAndRequestPermissions() && state) {
                startActivity(new Intent(MainActivity.this, findFreelancerActivity.class));
            }
        });

        RelativeLayout framePanel = (RelativeLayout)findViewById(R.id.framePanel);
        framePanel.setOnClickListener(f -> {
            setFragment("panel", "PAINEL DE CONTROLE", "panel", new panelDeControlFragment());
        });

        RelativeLayout frameNotification = (RelativeLayout)findViewById(R.id.frameNotification);
        frameNotification.setOnClickListener(f -> {
            setFragment("notification", "Notificação", " ", new notificationFragment());
        });

        RelativeLayout frameMessages = (RelativeLayout)findViewById(R.id.frameMessage);
        frameMessages.setOnClickListener(f -> {
            setFragment("message", "MENSAGEM", " ", new messagesFragment());
        });

        RelativeLayout frameMisFreelancer = (RelativeLayout)findViewById(R.id.frameProjFree);
        frameMisFreelancer.setOnClickListener(f -> {
            setFragment("freelancer", "COMO FREELANCER", " ", new tabsFragment());
        });

        RelativeLayout frameMisClient = (RelativeLayout)findViewById(R.id.frameProjClient);
        frameMisClient.setOnClickListener(f -> {
            setFragment("client", "COMO EMPREGADOR", " ", new tabsFragment());
        });

        RelativeLayout frameConfig = (RelativeLayout)findViewById(R.id.frameConfigure);
        frameConfig.setOnClickListener(f -> {
            setFragment("config", "CONFIGURAÇÃO", " ", new configFragment());
        });
    }

    private  boolean checkAndRequestPermissions() {
        int permissionFineLocation = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MY_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("LOG", "\n" +"Reencaminhamento de permissão chamado-------");

        switch (requestCode) {
            case MY_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("LOG", "permissão de serviços de localização concedida");
                        startActivity(new Intent(MainActivity.this, findFreelancerActivity.class));
                    } else {
                        Log.d("LOG", "Algumas permissões não são concedidas pergunte novamente);\n");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            showDialogOK("\n" +
                                            "Serviços de localização Permissão necessária para este aplicativo",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Vá para as configurações e habilite as permissões", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



    public void setFragment(String type, String title, String backstack,  Fragment fragment){
        FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(backstack);
        fManager.commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        actionBar.setTitle(title);
    }

    @Override
    public void onBackPressed() {

        Log.d("Main", "onBackStack");
        int count = getSupportFragmentManager().getBackStackEntryCount();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        Log.d("Count", String.valueOf(count));
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        else if (count == 0) {
            tabsFragment tabsFragment = new tabsFragment();
                super.onBackPressed();

            //additional code
        } else {
            int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();

            if (tag.equals("pp")){
                android.support.v4.app.FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                fManager.replace(R.id.mainFrame, new tabsFragment());
                getSupportFragmentManager().popBackStack();
                fManager.commit();
            }else if (tag.equals("panel")){
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                super.onBackPressed();
            }else if (tag.equals("client")){
                Bundle bundle = new Bundle();
                bundle.putString("type", "client");
                tabsFragment tabsFragment = new tabsFragment();
                tabsFragment.setArguments(bundle);
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                fManager.replace(R.id.mainFrame, tabsFragment);
                fManager.addToBackStack(" ");
                fManager.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                actionBar.setTitle("COMO EMPREGADOR");
            }else if (tag.equals("freelancer")){
                Bundle bundle = new Bundle();
                bundle.putString("type", "freelancer");
                tabsFragment tabsFragment = new tabsFragment();
                tabsFragment.setArguments(bundle);
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                fManager.replace(R.id.mainFrame, tabsFragment);
                fManager.addToBackStack(" ");
                fManager.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                actionBar.setTitle("COMO FREELANCER");
            }
            else{
                FragmentTransaction fManager = getSupportFragmentManager().beginTransaction();
                fManager.replace(R.id.mainFrame, new panelDeControlFragment());
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                actionBar.setTitle("PAINEL DE CONTROLE");
                fManager.commit();
            }

        }

    }

}