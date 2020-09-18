package luck.materialdesign.tabsnavigator.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.findProjectActivity;
import luck.materialdesign.tabsnavigator.model.countProjects;
import luck.materialdesign.tabsnavigator.utils.adapter;
import luck.materialdesign.tabsnavigator.utils.album;
import luck.materialdesign.tabsnavigator.utils.categoryAdapter;
import luck.materialdesign.tabsnavigator.utils.categoryAlbum;

/**
 * Created by BeS on 08.09.2017.
 */

public class categoryFragment extends Fragment implements categoryAdapter.OnClick{


    int[] image = new int[]{R.drawable.desi, R.drawable.vend, R.drawable.ti, R.drawable.contru, R.drawable.audiov, R.drawable.auto, R.drawable.gastro, R.drawable.escrit};
    String[] imageName = new String[]{"DESIGN E MULTIMIDIA", "MARKETING E VENDAS", "PROGRAMAÇÃO E TI", "CONSTRUÇÃO CIVIL", "AUDIOVISUAL", "AUTOMOVEIS", "GASTRONOMIA", "ESCRITA E TRADUÇÃO"};
    int[] color = new int[]{Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120),   Color.rgb(135, 31, 120)};
    Long[] countProj = new Long[]{Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0)};
    String type;
    countProjects count;
    RecyclerView recyclerView;
    List<categoryAlbum> albumList;
    categoryAlbum a;
    categoryAdapter adapter;

    public static categoryFragment newInstance(int position, String type) {

        Bundle args = new Bundle();
        args.putString("page", String.valueOf(position));
        args.putString("type", type);

        categoryFragment fragment = new categoryFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);


        type = getArguments().getString("type");
        albumList = new ArrayList<>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
//        StaggeredGridLayoutManager layoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        Intent intent = new Intent(getContext(), freelancerAccount.class);
//

//        getActivity().startActivity(intent);
        FirebaseDatabase.getInstance().getReference("projects").child("open").child("count_proj").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    count = dataSnapshot.getValue(countProjects.class);
                    if (count.getDesign_count() != null) {
                        countProj[0] = count.getDesign_count();
                    }
                    if (count.getSales_count() != null) {
                        countProj[1] = count.getSales_count();
                    }
                    if (count.getIt_count() != null) {
                        countProj[2] = count.getIt_count();
                    }
                    if (count.getContru_count() != null) {
                        countProj[3] = count.getContru_count();
                    }
                    if (count.getAudiovisual_count() != null) {
                        countProj[4] = count.getAudiovisual_count();
                    }
                    if (count.getAutomoveis_count() != null) {
                        countProj[5] = count.getAutomoveis_count();
                    }
                    if (count.getGastronomia_count() != null) {
                        countProj[6] = count.getGastronomia_count();
                    }
                    if (count.getWriting_count() != null) {
                        countProj[7] = count.getWriting_count();
                    }
                }
                for (int i = 0; i < 8; i++){

                    a = new categoryAlbum(image[i], imageName[i], countProj[i], color[i]);

                    albumList.add(a);
                    FragmentTransaction fManager = getFragmentManager().beginTransaction();
                    adapter = new categoryAdapter(getContext(), fManager, albumList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    public void onClick(Context context, FragmentTransaction fTrans, int icon, String name) {

        Intent intent = new Intent(context, findProjectActivity.class);
        intent.putExtra("type", name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

//        activity.finish();
//        Log.d("Click", name);
//
//        Bundle bundle = new Bundle();
//        bundle.putString("area", name);
//        createProjectGroupFragment categoryFragment = new createProjectGroupFragment();
//        categoryFragment.setArguments(bundle);
//        fTrans.replace(R.id.mainFrame, categoryFragment);
//        fTrans.addToBackStack(" ");
//        fTrans.commit();
    }
}
