package luck.materialdesign.tabsnavigator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.activities.registerActivity;
import luck.materialdesign.tabsnavigator.customViews.SmoothCheckBox;
import luck.materialdesign.tabsnavigator.customViews.ToggleButtonView;
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;

/**
 * Created by BeS on 07.05.2017.
 */

public class loginActivity extends AppCompatActivity {
    TextView regHere;
    @BindView(R.id.btnSignIn)
    TextView signin;
    @BindView(R.id.btnSignUp)
    TextView signup;

    @BindView(R.id.logEmail)
    EditText email;

    @BindView(R.id.logPassword)
    EditText pass;

    @BindView(R.id.checkRemember)
    SmoothCheckBox remember;

    @BindView(R.id.btnForgot)
    TextView btnForgot;

    TextView login;



    LoginButton loginButton;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    FragmentTransaction fTrans;
    ViewPager frame;
    @BindView(R.id.toggle)
    ToggleButtonView toggle;
    FragmentManager fragmentManager;
    SharedPreferences sPref;

    static ArrayList<String> TitlesMan= new ArrayList<>();
    private AccessTokenTracker mFacebookAccessTokenTracker;
    private TextView mLoggedInStatusTextView;

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;


    public static final int NUMBER_OF_REQUEST = 23401;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        LoginManager.getInstance().logOut();
//        FirebaseAuth.getInstance().signOut();
        sPref = getPreferences(MODE_PRIVATE);
//        Fabric.with(this, new Crashlytics());
        if (hasConnection(getApplicationContext()) == true) {
            String savedText = sPref.getString("remember", "");
            if (savedText.equals("yes")){
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    sPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("remember", "not");
                    ed.commit();
                } else {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        intent.putExtra("type", "false");
                        startActivity(intent);
                        finish();
                    }
                }
            }else{

            }

        }else{
            Toast.makeText(getApplicationContext(), "Check the Internet Connection", Toast.LENGTH_LONG).show();
        }




        signin.setOnClickListener(f -> {

            if (email.getText().length()>0 && pass.getText().length() > 0){
                if (remember.isChecked()) {
                    sPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("remember", "yes");
                    ed.commit();
                }else{
                    sPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("remember", "not");
                    ed.commit();
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            intent.putExtra("type", String.valueOf(toggle.isSwitchOn()));
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getBaseContext(), "Login error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Debe llenar los campos obligatorios", Toast.LENGTH_LONG).show();
            }

            });

        signup.setOnClickListener(f -> {
            startActivity(new Intent(loginActivity.this, registerActivity.class));
            finish();
        });
//        fragmentManager =  getSupportFragmentManager();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);



        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Log.d("FACEBOOK", "SUCCESS");
                Log.d("FACEBOOK ID", loginResult.getAccessToken().getUserId());
                Log.d("FACEBOOK INFO", loginResult.toString());

                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Log.d("FACEBOOK", "CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("FACEBOOK", "ERROR" + exception);
            }
        });

        btnForgot.setOnClickListener(f -> {
            if (email.getText().length()>0) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getBaseContext(), "A senha foi enviada para seu e-mail", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "e-mail errado", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getBaseContext(), "Por favor entre com seu e-mail", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Log.d("CREDENTIAL", credential.getProvider());
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }else{

                    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                DataSnapshot data = dataSnapshot.child("users");
                            try{
                                if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                                    String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");

                                    if (data.child(mail).exists()) {
                                        //do ur stuff
                                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                        intent.putExtra("type", String.valueOf(toggle.isSwitchOn()));
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //do something
                                        Intent intent = new Intent(loginActivity.this, registerActivity.class);
                                        intent.putExtra("type", "withoutEmail");
                                        startActivity(intent);
                                        finish();
                                        Log.d("FALSE", "CHILD");
                                    }
                                }else{
                                    Toast.makeText(getBaseContext(), "Erro. Por favor, utilize a entrada através de e-mail", Toast.LENGTH_LONG).show();
                                    LoginManager.getInstance().logOut();
                                }
                            }catch(NullPointerException e){
                                Toast.makeText(getBaseContext(), "Erro. Por favor, utilize a entrada através de e-mail", Toast.LENGTH_LONG).show();
                                LoginManager.getInstance().logOut();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    goMainActivity();
                }
            }
        });
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    public  void goMainActivity(){

        Intent intent = new Intent(loginActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(firebaseAuthListerner);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        firebaseAuth.removeAuthStateListener(firebaseAuthListerner);
    }
}