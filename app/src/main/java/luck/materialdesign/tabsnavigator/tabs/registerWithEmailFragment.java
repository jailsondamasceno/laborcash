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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.loginActivity;
import luck.materialdesign.tabsnavigator.model.accountClientModel;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.utils.Country;
import luck.materialdesign.tabsnavigator.utils.countryListAdapter;

/**
 * Created by BeS on 09.09.2017.
 */

public class registerWithEmailFragment extends Fragment {

    @BindView(R.id.regName)
    EditText nameEdit;

    @BindView(R.id.regSurname)
    EditText surnameEdit;

    @BindView(R.id.regEmail)
    EditText emailEdit;

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
        View v = inflater.inflate(R.layout.register_email_layout, container, false);
        ButterKnife.bind(this, v);
        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
            nc.add(c);
        }
        activity = getActivity();
        countryListAdapter adapter = new countryListAdapter(getContext(), nc);
        country.setAdapter(adapter);

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
            if (nameEdit.getText().toString().trim().length() > 0 && surnameEdit.getText().toString().trim().length() > 0
                    && emailEdit.getText().toString().trim().length() > 0 && passwordEdit.getText().toString().trim().length() > 0){
                if (passwordEdit.getText().length() > 6){
                    if (emailEdit.getText().toString().contains("@") && emailEdit.getText().toString().contains(".")){
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            accountClientModel accountClientModel = new accountClientModel("0", "0", "0", "0", null);
                                            accountFreeModel accountFreeModel = new accountFreeModel(skillName, "", "0", "0", "0", "0", "0", null);
                                            FirebaseDatabase.getInstance().getReference("users").child(emailEdit.getText().toString().replace(".","(dot)")).setValue(new accountModel(nameEdit.getText().toString()+" "+surnameEdit.getText().toString(), emailEdit.getText().toString(), "https://firebasestorage.googleapis.com/v0/b/imperatrix-b4fef.appspot.com/o/dummy.jpg?alt=media&token=5cf88d13-e829-4ff8-b9e7-c7056ebc5344", countryName, " ", accountFreeModel, accountClientModel));

                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra("type", "true");
                                            activity.startActivity(intent);

                                            activity.finish();
                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                                    activity.recreate();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(), "Incorrecto de la dirección de correo electrónico", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(), "Debe llenar los campos obligatorios", Toast.LENGTH_LONG).show();
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
