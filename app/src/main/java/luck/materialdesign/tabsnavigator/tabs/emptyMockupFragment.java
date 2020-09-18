package luck.materialdesign.tabsnavigator.tabs;
import android.content.Intent;
import android.media.Image;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.createProjectActivity;
import luck.materialdesign.tabsnavigator.activities.freelancerAccount;
import luck.materialdesign.tabsnavigator.utils.categoryAdapter;
import luck.materialdesign.tabsnavigator.utils.categoryAlbum;
import luck.materialdesign.tabsnavigator.utils.panelAlbum;
import luck.materialdesign.tabsnavigator.utils.panelControlAdapter;

/**
 * Created by BeS on 08.09.2017.
 */

public class emptyMockupFragment extends Fragment {

    int[] image = new int[]{R.drawable.profile_dummy, R.drawable.dummy, R.drawable.rocket};
    String[] imageName = new String[]{"Seu perfil está incompleto", "Você ainda não fez nenhuma proposta", "Você ainda não possui projetos ativos"};
    String[] btnText = new String[]{"COMPLETAR SEUS DADOS", "ENVIAR UMA PROPOSTA", "BUSCAR PROJETOS"};

    ImageView icon;
    TextView text;
    TextView btn;
    String type;

    public static emptyMockupFragment newInstance(int position, String type) {

        Bundle args = new Bundle();
        args.putString("page", String.valueOf(position));
        args.putString("type", type);

        emptyMockupFragment fragment = new emptyMockupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.empty_mockup, container, false);
        icon = (ImageView)v.findViewById(R.id.icon);
        text = (TextView)v.findViewById(R.id.text);
        btn = (TextView)v.findViewById(R.id.btn);
        if (getArguments().getString("type").equals("empty_profile")) {
            icon.setImageResource(image[0]);
            text.setText(imageName[0]);
            btn.setText(btnText[0]);
            btn.setOnClickListener(f -> {
                startActivity(new Intent(getActivity(), freelancerAccount.class));
            });
        }else if (getArguments().getString("type").equals("empty_proposal")) {
            icon.setImageResource(image[1]);
            text.setText(imageName[1]);
            btn.setText(btnText[1]);
            btn.setOnClickListener(f -> {
                Bundle bundle = new Bundle();
                bundle.putString("type", "search");
                tabsFragment tabsFragment = new tabsFragment();
                tabsFragment.setArguments(bundle);
                FragmentTransaction fManager = getFragmentManager().beginTransaction();
                fManager.replace(R.id.mainFrame, tabsFragment);
                fManager.addToBackStack(" ");
                fManager.commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("PROJETOS DE PESQUISA");
            });
        }else if (getArguments().getString("type").equals("empty_project")){
                icon.setImageResource(image[2]);
                text.setText(imageName[2]);
                btn.setText(btnText[2]);
                btn.setOnClickListener(f -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "search");
                    tabsFragment tabsFragment = new tabsFragment();
                    tabsFragment.setArguments(bundle);
                    FragmentTransaction fManager = getFragmentManager().beginTransaction();
                    fManager.replace(R.id.mainFrame, tabsFragment);
                    fManager.addToBackStack(" ");
                    fManager.commit();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("PROJETOS DE PESQUISA");
                });
        }else if (getArguments().getString("type").equals("empty_notification")){
            icon.setImageResource(image[1]);
            text.setText("Não ha mensagens!");
            text.setTextSize(22);
            btn.setVisibility(View.INVISIBLE);
        }else if (getArguments().getString("type").equals("empty_freelancer")){
            icon.setImageResource(image[1]);
            text.setText(imageName[1]);
            text.setTextSize(30);
            btn.setText(btnText[1]);

        }else if (getArguments().getString("type").equals("empty_client")){
            icon.setImageResource(image[2]);
            text.setText("Você ainda não tem projetos");
            text.setTextSize(30);
            btn.setText(btnText[2]);

        }

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
