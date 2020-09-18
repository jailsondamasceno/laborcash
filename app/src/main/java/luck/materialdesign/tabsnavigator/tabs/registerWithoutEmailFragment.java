package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountClientModel;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.feedbackModel;
import luck.materialdesign.tabsnavigator.utils.Country;
import luck.materialdesign.tabsnavigator.utils.countryListAdapter;

/**
 * Created by BeS on 09.09.2017.
 */

public class registerWithoutEmailFragment extends Fragment {


    @BindView(R.id.regPassword)
    EditText passwordEdit;

    @BindView(R.id.country)
    Spinner country;

    @BindView(R.id.skills)
    Spinner skills;

    @BindView(R.id.btnReg)
    TextView btnReg;

    String countryName = "Andorra";
    String skillName;

    FragmentTransaction fManager;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_without_email_layout, container, false);
        ButterKnife.bind(this, v);
        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
            nc.add(c);
        }
        countryListAdapter adapter = new countryListAdapter(getContext(), nc);
        country.setAdapter(adapter);
        activity = getActivity();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryName = nc.get(position).getName();
                Log.d("Country", countryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        skills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skillName = skills.getSelectedItem().toString();
                Log.d("Skill", skillName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReg.setOnClickListener(f -> {
                if (passwordEdit.getText().length() > 6){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updatePassword(passwordEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d("Name", user.getDisplayName());
                                Log.d("Icon", String.valueOf(user.getPhotoUrl()));

                                accountClientModel accountClientModel = new accountClientModel("0", "0", "0", "0", null);
                                accountFreeModel accountFreeModel = new accountFreeModel(skillName, "", "0", "0", "0", "0", "0", null);
                                FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().replace(".","(dot)")).setValue(new accountModel(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), countryName, " ", accountFreeModel, accountClientModel));
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("type", "true");
                                activity.startActivity(intent);

                                activity.finish();

                            }else{
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                                activity.recreate();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
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




}
