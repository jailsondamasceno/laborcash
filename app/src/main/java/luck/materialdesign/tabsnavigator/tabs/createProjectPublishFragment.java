package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.projectModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;

/**
 * Created by BeS on 13.09.2017.
 */

public class createProjectPublishFragment extends Fragment {

    @BindView(R.id.editTitle)
    EditText title;

    @BindView(R.id.editDescription)
    EditText description;

    @BindView(R.id.btnPublish)
    TextView btnPublish;

    String userName;
    Bundle bundle;

    Context context;

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_project_final_page, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();
        context = getContext();
        Log.d("TYPE", bundle.getString("type"));
        Log.d("GROUP", bundle.getString("group"));
        Log.d("Area", bundle.getString("area"));
        fManager = getFragmentManager().beginTransaction();
        btnPublish.setOnClickListener(f -> {

            if (title.getText().length() > 0 && description.getText().length() > 10){
                String childName = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)");
                FirebaseDatabase.getInstance().getReference("users").child(childName).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                  accountModel user = dataSnapshot.getValue(accountModel.class);
                                  userName = user.getName();
                                    String projName = userName+Calendar.getInstance().getTime().getTime();

                                    FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("area")).child(projName).setValue(new projectModel(bundle.getString("price"), title.getText().toString(), bundle.getString("group"), Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() + "", description.getText().toString(), userName, bundle.getString("skills"), childName, bundle.getString("area")));
                                    FirebaseDatabase.getInstance().getReference("users").child(childName).child("projects").child(projName).setValue(new userProjectModel("open", bundle.getString("area"), "", null));

                                  Bundle bundle = new Bundle();
                                   bundle.putString("type", "client");
                                  tabsFragment tabsFragment = new tabsFragment();
                                  tabsFragment.setArguments(bundle);
                                   getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//                                  fManager.replace(R.id.mainFrame, tabsFragment);
//                                 fManager.commit();
                               closeActivity();

                             }
                      }

                      @Override
                       public void onCancelled(DatabaseError databaseError) {

                        }
                });

            }else{
                Toast.makeText(getContext(), "Fill in the details. Description must be more than 10 signs", Toast.LENGTH_LONG).show();
            }


            FragmentTransaction fManager = getFragmentManager().beginTransaction();

//            Bundle bundle2 = new Bundle();
//            bundle2.putString("type", bundle.getString("type"));
//            bundle2.putString("group",  bundle.getString("group"));
//            bundle2.putString("area", bundle.getString("area"));
//            bundle2.putString("price", priceRange.getSelectedItem().toString());
//            createProjectTypeFragment projectTypeFragment = new createProjectTypeFragment();
//            projectTypeFragment.setArguments(bundle2);
//            fManager.replace(R.id.mainFrame, projectTypeFragment);
//            fManager.commit();
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void closeActivity(){

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();

    }


}
