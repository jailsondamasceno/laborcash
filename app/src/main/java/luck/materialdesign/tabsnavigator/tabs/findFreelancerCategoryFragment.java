package luck.materialdesign.tabsnavigator.tabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.countProjects;
import luck.materialdesign.tabsnavigator.utils.categoryAlbum;

/**
 * Created by BeS on 14.09.2017.
 */

public class findFreelancerCategoryFragment extends Fragment {

    int[] image = new int[]{R.drawable.desi, R.drawable.vend, R.drawable.ti, R.drawable.contru, R.drawable.audiov, R.drawable.auto, R.drawable.gastro, R.drawable.escrit};
    String[] imageName = new String[]{"DESIGN E MULTIMIDIA", "MARKETING E VENDAS", "PROGRAMAÇÃO E TI", "CONSTRUÇÃO CIVIL", "AUDIOVISUAL", "AUTOMOVEIS", "GASTRONOMIA", "ESCRITA E TRADUÇÃO"};
    int[] color = new int[]{Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120), Color.rgb(135, 31, 120),   Color.rgb(135, 31, 120)};
    Long[] countProj = new Long[]{Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0)};
    String type;
    countProjects count;
    RecyclerView recyclerView;
    List<categoryAlbum> albumList;
    FragmentTransaction fManager;
    categoryAlbum a;
    categoryCreateAdapter adapter;

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
        for (int i = 0; i< 8; i++) {
            a = new categoryAlbum(image[i], imageName[i], countProj[i], color[i]);

            albumList.add(a);
        }
        fManager = getFragmentManager().beginTransaction();
        adapter = new categoryCreateAdapter(getContext(), albumList);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void nextScreen(int icon, String name) {
        Log.d("Click", name);

        Bundle bundle = new Bundle();
        bundle.putString("area", name);
        findFreelancerFragment fragment = new findFreelancerFragment();
        fragment.setArguments(bundle);
        fManager.replace(R.id.mainFrame, fragment);
        fManager.addToBackStack(" ");
        fManager.commit();
    }

    public class categoryCreateAdapter  extends RecyclerView.Adapter<categoryCreateAdapter.MyViewHolder> {

        private Context mContext;
        private List<categoryAlbum> albumList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView count;
            private TextView text;
            private TextView color;
            private ImageView icon;

            public CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.name);
                count = (TextView)view.findViewById(R.id.countProj);
                text = (TextView)view.findViewById(R.id.textProj);
                color = (TextView)view.findViewById(R.id.colorItem);
                icon = (ImageView)view.findViewById(R.id.icon);
                cardView = (CardView)view.findViewById(R.id.card_view);
            }
        }


        public categoryCreateAdapter(Context mContext,  List<categoryAlbum> albumList) {

            this.mContext = mContext;
            this.albumList = albumList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final categoryAlbum album = albumList.get(position);
            holder.name.setText(album.getName());
            holder.name.setTextColor(album.getColor());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nextScreen(album.getIcon(), album.getName());

                }
            });
            holder.color.setBackgroundColor(album.getColor());
            Glide
                    .with(mContext)
                    .load(album.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(false)
                    .into(holder.icon);
        }

        @Override
        public int getItemCount() {

            return albumList.size();
        }

    }

}
