package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;

/**
 * Created by BeS on 13.09.2017.
 */

public class createProjectSelectSkillFragment extends Fragment {

    @BindView(R.id.editSkill)
    MultiAutoCompleteTextView skillText;

    @BindView(R.id.btnNext)
    TextView btnNext;


    Bundle bundle;

    CallbackManager callbackManager;
    FragmentTransaction fManager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthListerner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_project_skill, container, false);
        ButterKnife.bind(this, v);
        bundle = getArguments();

        Log.d("GROUP", bundle.getString("group"));
        Log.d("Area", bundle.getString("area"));

        String[] skills;
        if (bundle.getString("area").equals("DESIGN E MULTIMIDIA")) {
            skills = getResources().getStringArray(R.array.design);
        }else if (bundle.getString("area").equals("MARKETING E VENDAS")){
            skills = getResources().getStringArray(R.array.sales);
        }else if (bundle.getString("area").equals("PROGRAMAÇÃO E TI")){
            skills = getResources().getStringArray(R.array.it_skills_array);
        }else if (bundle.getString("area").equals("CONSTRUÇÃO CIVIL")){
            skills = getResources().getStringArray(R.array.itens_construcao_civil);
        }else if (bundle.getString("area").equals("AUDIOVISUAL")){
            skills = getResources().getStringArray(R.array.itens_audiovisual);
        }else if (bundle.getString("area").equals("AUTOMOVEIS")){
            skills = getResources().getStringArray(R.array.itens_automoveis);
        }else if (bundle.getString("area").equals("GASTRONOMIA")){
            skills = getResources().getStringArray(R.array.itens_gastronomia);
        }else{
            skills = getResources().getStringArray(R.array.writingAndcontent);
        }



        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, skills);

        skillText.setAdapter(adapter);
        skillText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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

        btnNext.setOnClickListener(f -> {
            String[] res = skillText.getText().toString().trim().split(",");
            List<String> list = Arrays.asList(skills);
            String result = " ";
            for( String ss : res) {

                if (list.contains(ss)) {
                    result = result + ss + ",";
                }
            }

            if (result.length()>2) {
                FragmentTransaction fManager = getFragmentManager().beginTransaction();

                Bundle bundle2 = new Bundle();

                bundle2.putString("group", bundle.getString("group"));
                bundle2.putString("area", bundle.getString("area"));
                bundle2.putString("skills", result);
                createProjectTypeFragment projectTypeFragment = new createProjectTypeFragment();
                projectTypeFragment.setArguments(bundle2);
                fManager.replace(R.id.mainFrame, projectTypeFragment);
                fManager.addToBackStack(" ");
                fManager.commit();
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
