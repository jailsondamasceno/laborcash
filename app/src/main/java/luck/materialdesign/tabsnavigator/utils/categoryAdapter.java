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
import luck.materialdesign.tabsnavigator.tabs.categoryFragment;
import luck.materialdesign.tabsnavigator.tabs.emptyFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;

/**
 * Created by BeS on 20.08.2017.
 */

public class categoryAdapter  extends RecyclerView.Adapter<categoryAdapter.MyViewHolder> {

    private Context mContext;
    private FragmentTransaction fTrans;
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


    public categoryAdapter(Context mContext, FragmentTransaction fTrans, List<categoryAlbum> albumList) {

        this.mContext = mContext;
        this.albumList = albumList;
        this.fTrans = fTrans;
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
        if (!album.getCount().equals(" ")){
            holder.count.setText(String.valueOf(album.getCount()));
            holder.text.setText(R.string.projtext);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryFragment main = new categoryFragment();
                OnClick onClick = (OnClick) main;
                onClick.onClick(mContext, fTrans, album.getIcon(), album.getName());

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

    public interface OnClick {
        public void onClick(Context context, FragmentTransaction fTrans,  int icon, String name);

    }
}
