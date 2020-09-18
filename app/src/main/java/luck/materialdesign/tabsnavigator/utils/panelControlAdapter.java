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
import luck.materialdesign.tabsnavigator.tabs.panelDeControlFragment;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;

/**
 * Created by BeS on 20.08.2017.
 */

public class panelControlAdapter  extends RecyclerView.Adapter<panelControlAdapter.MyViewHolder> {

    private Context mContext;

    private List<panelAlbum> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView btn;
        private TextView text;
        private ImageView icon;

        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            text = (TextView)view.findViewById(R.id.text);
            btn = (TextView)view.findViewById(R.id.btn);
            icon = (ImageView)view.findViewById(R.id.icon);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }


    public panelControlAdapter(Context mContext, List<panelAlbum> albumList) {

        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_mockup, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final panelAlbum album = albumList.get(position);
        holder.text.setText(album.getText());
        holder.btn.setText(album.getBtnText());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelDeControlFragment main = new panelDeControlFragment();
                OnClick onClick = (OnClick) main;
                onClick.onClick(mContext, position);

            }
        });
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
        public void onClick(Context context, int position);

    }
}
