package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.loginActivity;
import luck.materialdesign.tabsnavigator.utils.adapter;
import luck.materialdesign.tabsnavigator.utils.album;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by BeS on 09.09.2017.
 */

public class registerChooseFragment extends Fragment {

    @BindView(R.id.btnRegWithEmail)
    TextView btnRegEmail;

    @BindView(R.id.btnRegFacebook)
    LoginButton btnRegFacebook;

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_layout, container, false);
        ButterKnife.bind(this, v);
        btnRegEmail.setOnClickListener(f -> {
            fManager = getFragmentManager().beginTransaction();
            fManager.replace(R.id.mainFrame, new registerWithEmailFragment());
            fManager.addToBackStack(" ");
            fManager.commit();
        });

        firebaseAuth = FirebaseAuth.getInstance();



        callbackManager = CallbackManager.Factory.create();
        btnRegFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnRegFacebook.setFragment(this);
        btnRegFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());



        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }else{
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    fManager = getFragmentManager().beginTransaction();
                    fManager.replace(R.id.mainFrame, new registerWithoutEmailFragment());
                    fManager.addToBackStack(" ");
                    fManager.commit();
//                if (user != null){
//                    Log.d("EMAIL", user.getEmail());
//                    Log.d("Icon", String.valueOf(user.getPhotoUrl()));
//                    Log.d("name", user.getDisplayName());
//
//                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(pass).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                        }
//                    });
//                }
//                    goMainActivity();
                }
            }
        });
    }


    public  void goMainActivity(){

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


}
