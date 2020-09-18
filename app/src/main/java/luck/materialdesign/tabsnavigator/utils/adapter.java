package luck.materialdesign.tabsnavigator.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.tabs.emptyFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;

/**
 * Created by BeS on 20.08.2017.
 */

public class adapter  extends RecyclerView.Adapter<adapter.MyViewHolder> {

    private Context mContext;

    private List<album> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView adress;
        public TextView open;
        public ImageView icon;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
//            name = (TextView)view.findViewById(R.id.name);
//            adress = (TextView)view.findViewById(R.id.adress);
//            open = (TextView)view.findViewById(R.id.open);
//            icon = (ImageView)view.findViewById(R.id.icon);
//            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }


    public adapter(Context mContext, List<album> albumList) {

        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final album album = albumList.get(position);
//        holder.name.setText(album.getName());
////        holder.adress.setText(album.getAdress());
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                emptyFragment main = new emptyFragment();
//                OnClick onClick = (OnClick) main;
//                onClick.onClick(mContext, album.getfTrans(), album.getIcon(), album.getName());
//
//            }
//        });
//
//        holder.open.setText(album.getOpen());
//        Glide
//                .with(mContext)
//                .load(album.getIcon())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .skipMemoryCache(false)
//                .into(holder.icon);
    }

    @Override
    public int getItemCount() {

        return albumList.size();
    }

    public interface OnClick {
        public void onClick(Context context, FragmentTransaction fTrans, int icon, String name);

    }
}
