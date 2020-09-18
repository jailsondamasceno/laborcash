package luck.materialdesign.tabsnavigator.utils.chatAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;


import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.chat.ChatModel;
import luck.materialdesign.tabsnavigator.utils.Util;
import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by BeS on 30.08.2017.
 */

public class RecyclerFirebaseAdapter   extends RecyclerView.Adapter<RecyclerFirebaseAdapter.MyViewHolder> {

    private Context mContext;
    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private static final int RIGHT_MSG_VIDEO = 4;
    private static final int LEFT_MSG_VIDEO = 5;
    private static final int RIGHT_MSG_SOUND = 6;
    private static final int LEFT_MSG_SOUND = 7;
    private List<ChatModel> messageList;
    private String nameUser;
    int viewType;

    private ClickListenerChatFirebase mClickListenerChatFirebase;


    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
        TextView tvTimestamp,tvLocation;
        EmojiconTextView txtMessage;
        ImageView ivChatPhoto, play;
        ChatMessageView messageView;
        TextView curTime, maxTime;
        SeekBar soungProgress;


        public MyViewHolder(View view) {
            super(view);
                if (viewType == RIGHT_MSG_SOUND || viewType == LEFT_MSG_SOUND){
                    play = (ImageView)view.findViewById(R.id.btnPlay);
                    curTime = (TextView)view.findViewById(R.id.soundCurTime);
                    maxTime = (TextView)view.findViewById(R.id.soundMaxTime);
                    soungProgress = (SeekBar)view.findViewById(R.id.soundProgress);
                    messageView = (ChatMessageView)view.findViewById(R.id.contentMessageChat);
                }else {
                    tvTimestamp = (TextView) view.findViewById(R.id.timestamp);
                    txtMessage = (EmojiconTextView) view.findViewById(R.id.txtMessage);
                    tvLocation = (TextView) view.findViewById(R.id.tvLocation);
                    ivChatPhoto = (ImageView) view.findViewById(R.id.img_chat);
                }


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = messageList.get(position);
            if (model.getMapModel() != null){
                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
            }else{
                if (model.getFile().getType().equals("img")) {
                    mClickListenerChatFirebase.clickImageChat(view, position, model.getUserModel().getName(), model.getUserModel().getPhoto_profile(), model.getFile().getUrl_file());
                }

                else{
                    mClickListenerChatFirebase.clickVideoChat(view, position, model.getUserModel().getName(), model.getUserModel().getPhoto_profile(), model.getFile().getName_file());
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    public RecyclerFirebaseAdapter(Context mContext, String nameUser, List<ChatModel> albumList, ClickListenerChatFirebase mClickListenerChatFirebase) {

        this.mContext = mContext;
        this.messageList = albumList;
        this.nameUser = nameUser;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;

    }

    public RecyclerFirebaseAdapter(Context mContext, String nameUser, List<ChatModel> albumList) {

        this.mContext = mContext;
        this.messageList = albumList;
        this.nameUser = nameUser;


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        this.viewType = viewType;
        Log.d("ViewType", String.valueOf(viewType));
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == LEFT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == RIGHT_MSG_IMG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == LEFT_MSG_IMG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == RIGHT_MSG_VIDEO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_video,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == LEFT_MSG_VIDEO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_video,parent,false);
            return new MyViewHolder(view);
        }else if (viewType == LEFT_MSG_SOUND){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_sound,parent,false);
            return new MyViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_sound,parent,false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model =  messageList.get(position);
        if (model.getMapModel() != null){
            if (model.getUserModel().getName().equals(nameUser)){
                return RIGHT_MSG_IMG;
            }else{
                return LEFT_MSG_IMG;
            }
        }else if (model.getFile() != null){
            if (model.getFile().getType().equals("img")) {
                if (model.getUserModel().getName().equals(nameUser)) {
                    return RIGHT_MSG_IMG;
                } else {
                    return LEFT_MSG_IMG;
                }
            }
            else if (model.getFile().getType().equals("sound")) {
                if (model.getUserModel().getName().equals(nameUser)) {
                    return RIGHT_MSG_SOUND;
                } else {
                    return LEFT_MSG_SOUND;
                }
            }else {

                if (model.getUserModel().getName().equals(nameUser)) {
                    return RIGHT_MSG_VIDEO;
                } else {
                    return LEFT_MSG_VIDEO;
                }
            }
        }else if (model.getUserModel().getName().equals(nameUser)){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ChatModel model = messageList.get(position);
        if (model.getMessage() != null && holder.txtMessage != null) {
            holder.txtMessage.setText(model.getMessage());
        }
        if (model.getTimeStamp() != null && holder.tvTimestamp != null) {
            holder.tvTimestamp.setText(converteTimestamp(model.getTimeStamp()));
        }
//        holder.tvLocation.setVisibility(View.GONE);
        if (model.getFile() != null) {

            if (model.getFile().getType().equals("sound")) {
                String duration = model.getFile().getName_file().substring(0, model.getFile().getName_file().indexOf("min")) + ":" + model.getFile().getName_file().substring(model.getFile().getName_file().indexOf("min") + 3, model.getFile().getName_file().indexOf("sec"));
                holder.maxTime.setText(duration);
                holder.soungProgress.setEnabled(false);
                holder.play.setEnabled(false);
                holder.messageView.setOnClickListener(holder);

            } else {

                holder.tvLocation.setVisibility(View.GONE);
                if (model.getFile().getUrl_file() != null) {
                    Glide.with(mContext)
                            .load(model.getFile().getUrl_file())
                            .override(150, 150)
                            .fitCenter()
                            .into(holder.ivChatPhoto);
                    holder.ivChatPhoto.setOnClickListener(holder);
                }
            }
        }
            else if(model.getMapModel() != null){

                    Glide.with(mContext)
                        .load(Util.local(model.getMapModel().getLatitude(),model.getMapModel().getLongitude()))
                        .override(150, 150)
                        .fitCenter()
                        .into(holder.ivChatPhoto);
                    holder.ivChatPhoto.setOnClickListener(holder);
                    holder.tvLocation.setVisibility(View.VISIBLE);

            }

    }
    private CharSequence converteTimestamp(String mileSegundos){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(Long.parseLong(mileSegundos));
        //here your time in miliseconds
        Calendar cl2 = Calendar.getInstance();
        cl2.setTimeInMillis(System.currentTimeMillis());
        String date = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + cl.get(Calendar.MONTH) + ":" + cl.get(Calendar.YEAR);
        String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
        if (cl.get(Calendar.DAY_OF_MONTH) == cl2.get(Calendar.DAY_OF_MONTH)){
            return DateFormat.format("HH:mm", Long.parseLong(mileSegundos));
        }
        else{
            return DateFormat.format("dd.MM  hh:mm", Long.parseLong(mileSegundos));
//            return DateFormat.format("HH:mm:ss", Long.parseLong(mileSegundos));
//        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_TIME);
        }
    }

    public void setItems(List<ChatModel> persons) {
        messageList = persons;
    }

}