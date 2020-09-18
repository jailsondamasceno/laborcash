package luck.materialdesign.tabsnavigator.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.loginActivity;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.chat.ChatModel;
import luck.materialdesign.tabsnavigator.model.chat.FileModel;
import luck.materialdesign.tabsnavigator.model.chat.MapModel;
import luck.materialdesign.tabsnavigator.model.chat.UserModel;
import luck.materialdesign.tabsnavigator.model.userChatModel;
import luck.materialdesign.tabsnavigator.utils.Util;
import luck.materialdesign.tabsnavigator.utils.chatAdapter.ClickListenerChatFirebase;
import luck.materialdesign.tabsnavigator.utils.chatAdapter.RecyclerFirebaseAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

/**
 * Created by BeS on 16.09.2017.
 */

public class chatActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ClickListenerChatFirebase, View.OnTouchListener {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 4;
    private static final int REQUEST_TAKE_GALLERY_AUDIO = 5;

    public static final int RECORD_AUDIO = 0;
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView btSendMessage,btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    private ImageView btnMic;

    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";
    private static final String PHOTOS="photos";

    static final String TAG = "TAG";
    static final String CHAT_REFERENCE = "chatmodel";
    private static Uri imageUri;
    public MediaRecorder mrec = null;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;

    String childChat;
    List<ChatModel> messageList;
    RecyclerFirebaseAdapter adapter;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    boolean isReading = false;
    boolean isMic = true;
    private Dialog dialog;
    private UserModel userModel;
    private File photo;
    TextView bytes;
    Dialog uploadDialog;
    int soundId;
    String token;
    int countMes = 0;
    Uri picUri;
    String user1;
    String user2;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        childChat = getIntent().getStringExtra("childChat");
        user1 = getIntent().getStringExtra("user1");
        user2 = getIntent().getStringExtra("user2");
        FirebaseDatabase.getInstance().getReference("users").child(user1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    accountModel userAcc = dataSnapshot.getValue(accountModel.class);
                    token = userAcc.getToken();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        messageList = new ArrayList<>();
        mrec = new MediaRecorder();

        bindViews();
        if (!Util.verificaConexao(this)){
            Util.initToast(this,"Check the internet connection");
            finish();
        }else{

            verificaUsuarioLogado();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .enableAutoManage(this, this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API)
//                    .build();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child(Util.FOLDER_STORAGE_IMG);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (result.getUri() != null) {
                    sendFileFirebase(storageRef, result.getUri());
                }
            }
        }else if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place!=null){
                    LatLng latLng = place.getLatLng();
                    MapModel mapModel = new MapModel(latLng.latitude+"",latLng.longitude+"");
                    ChatModel chatModel = new ChatModel(userModel, Calendar.getInstance().getTime().getTime()+"",mapModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(childChat).push().setValue(chatModel);
                }else{
                    //PLACE IS NULL
                }
            }
        }else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO){
            if (resultCode == RESULT_OK){
                if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                    Uri selectedImageUri = data.getData();

                    // OI FILE Manager
                    String filemanagerstring = selectedImageUri.getPath();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);

                    if (selectedImagePath != null) {

                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.MICRO_KIND);

                        StorageReference imageCameraRef = storageRef.child("video");
                        uploadDialog("Uploading video...");
                        sendVideoFileFirebase(imageCameraRef, selectedImageUri, bMap);
                        Toast.makeText(this, "Loading... Please wait", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sendPhoto:
                photoGalleryIntent();
//                photoCameraIntent();
                break;
            //case R.id.sendLocation:
               // locationPlacesIntent();
              //  break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.sendVideo:
                videoGalleryIntent();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Util.initToast(this,"Google Play Services error.");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonMessage:
                sendMessageFirebase();
                break;
        }
    }





    public void uploadDialog(String title){
        uploadDialog = new Dialog(chatActivity.this);
        Log.d("Dialog", "TRUE");

        LayoutInflater inflater = (LayoutInflater)chatActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.upload_layout, (ViewGroup)findViewById(R.id.dialog));
        uploadDialog.setContentView(layout);
        bytes = (TextView)layout.findViewById(R.id.bytes);
        uploadDialog.setTitle(title);
        uploadDialog.show();
    }

    private void sendFileFirebase(StorageReference storageReference, final Uri file){
        if (storageReference != null && file != null){
            uploadDialog("Uploading image...");
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name+"_gallery");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Progress", String.valueOf(taskSnapshot.getBytesTransferred())+"/"+String.valueOf(taskSnapshot.getTotalByteCount()));
                    if (bytes != null){
                        bytes.setText(String.format("%.2f", (double) taskSnapshot.getBytesTransferred()/1000000)+" / "+String.format("%.2f", (double) taskSnapshot.getTotalByteCount()/1000000) + " Mb");
                    }


                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG,"onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    uploadDialog.hide();
                    FileModel fileModel = new FileModel("img",downloadUrl.toString(),name,"");
                    ChatModel chatModel = new ChatModel(userModel,"",Calendar.getInstance().getTime().getTime()+"",fileModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(childChat).push().setValue(chatModel);
                    FirebaseDatabase.getInstance().getReference("users").child(user1).child("dialogs").child(user2).child("lastMessage").setValue("(Image)");
                    FirebaseDatabase.getInstance().getReference("users").child(user2).child("dialogs").child(user1).child("lastMessage").setValue("(Image)");

                }
            });
        }else{
            Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void sendVideoFileFirebase(final StorageReference storageReference, final Uri file, final Bitmap bmp){
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
        if (storageReference != null){
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name+"_gallery");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Progress", String.valueOf(taskSnapshot.getBytesTransferred())+"/"+String.valueOf(taskSnapshot.getTotalByteCount()));
                    if (bytes != null){
                        bytes.setText(String.format("%.2f", (double) taskSnapshot.getBytesTransferred()/1000000)+" / "+String.format("%.2f", (double) taskSnapshot.getTotalByteCount()/1000000) + " Mb");
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG,"onSuccess sendFileFirebase");
                    final Uri downloadVideoUrl = taskSnapshot.getDownloadUrl(); //Video URL


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = storageReference.putBytes(data2);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d("URL", String.valueOf(downloadUrl));
                            uploadDialog.hide();
                            FileModel fileModel = new FileModel("video",downloadUrl.toString(),downloadVideoUrl.toString(),"");
                            ChatModel chatModel = new ChatModel(userModel,"",Calendar.getInstance().getTime().getTime()+"",fileModel);
                            mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(childChat).push().setValue(chatModel);
                            FirebaseDatabase.getInstance().getReference("users").child(user1).child("dialogs").child(user2).child("lastMessage").setValue("(Video)");
                            FirebaseDatabase.getInstance().getReference("users").child(user2).child("dialogs").child(user1).child("lastMessage").setValue("(Video)");
                        }
                    });

                }
            });
        }else{
            //IS NULL
        }

    }


    public void videoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

    }

    public void audioGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio"),REQUEST_TAKE_GALLERY_AUDIO);

    }

    /**
     * Envia o arvquivo para o firebase
     */
    private void sendFileFirebase(StorageReference storageReference, final File file){
        if (storageReference != null){
            Uri photoURI = FileProvider.getUriForFile(chatActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            UploadTask uploadTask = storageReference.putFile(photoURI);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG,"onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel("img",downloadUrl.toString(),file.getName(),file.length()+"");
                    ChatModel chatModel = new ChatModel(userModel,"",Calendar.getInstance().getTime().getTime()+"",fileModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(childChat).push().setValue(chatModel);
                    FirebaseDatabase.getInstance().getReference("users").child(user1).child("dialogs").child(user2).child("lastMessage").setValue("(Image)");
                    FirebaseDatabase.getInstance().getReference("users").child(user2).child("dialogs").child(user1).child("lastMessage").setValue("(Image)");
                }
            });
        }else{
            //IS NULL
        }

    }

    /**
     * Obter local do usuario
     */
    private void locationPlacesIntent(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enviar foto tirada pela camera
     */
    private void photoCameraIntent(){

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(photo));
////        imageUri = Uri.fromFile(photo);
//        imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".alessandro.firebaseandroid.provider", photo);
//        startActivityForResult(intent, IMAGE_CAMERA_REQUEST);


//        if (savedInstanceState==null) {
        photo=new File(new File(getFilesDir(), PHOTOS), "Pic.jpg");

        if (photo.exists()) {
            photo.delete();
        }
        else {
            photo.getParentFile().mkdirs();
        }

        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputUri=FileProvider.getUriForFile(this, AUTHORITY, photo);

        i.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip=
                    ClipData.newUri(getContentResolver(), "A photo", outputUri);

            i.setClipData(clip);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else {
            List<ResolveInfo> resInfoList=
                    getPackageManager()
                            .queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, outputUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        try {
            startActivityForResult(i, IMAGE_CAMERA_REQUEST);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Camera error", Toast.LENGTH_LONG).show();
            finish();
        }
//        }
//        else {
//            output=(File)savedInstanceState.getSerializable(EXTRA_FILENAME);
//        }

    }

    /**
     * Enviar foto pela galeria
     */
    private void photoGalleryIntent(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);

        Intent intent = CropImage.activity(picUri)
                .setAutoZoomEnabled(false)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0)
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .getIntent(this);
        startActivityForResult(intent, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Enviar msg de texto simples para chat
     */
    private void sendMessageFirebase(){
        if (edMessage.getText().toString().length() > 0) {
            ChatModel model = new ChatModel(userModel, edMessage.getText().toString(), Calendar.getInstance().getTime().getTime() + "", null);
            mFirebaseDatabaseReference.child(CHAT_REFERENCE).child(childChat).push().setValue(model);
            if (countMes == 0){
                sendMessage("Brand",  "Uma nova mensagem de " + userModel.getName(), "", "");
                countMes++;
            }
            FirebaseDatabase.getInstance().getReference("users").child(user1).child("dialogs").child(user2).child("lastMessage").setValue(edMessage.getText().toString());
            FirebaseDatabase.getInstance().getReference("users").child(user2).child("dialogs").child(user1).child("lastMessage").setValue(edMessage.getText().toString());
            edMessage.setText(null);
        }
    }


    /**
     * Ler collections chatmodel Firebase
     */
    private void lerMessagensFirebase(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Observable.create(f ->{
            mFirebaseDatabaseReference.child("chatmodel").child(childChat).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        ChatModel model = dataSnapshot.getValue(ChatModel.class);
                        messageList.add(model);
                        f.onNext(model);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }) .subscribeOn(Schedulers.io())

                .doOnError(er -> Log.d("Error", String.valueOf(er)))
                .doOnNext(model -> {
                    adapter = new RecyclerFirebaseAdapter(getBaseContext(), userModel.getName(), messageList, chatActivity.this);
                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            int friendlyMessageCount = adapter.getItemCount();
                            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (lastVisiblePosition == -1 ||
                                    (positionStart >= (friendlyMessageCount - 1) &&
                                            lastVisiblePosition == (positionStart - 1))) {
                                rvListMessage.scrollToPosition(positionStart);
                            }
                        }
                    });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        v -> {
                            rvListMessage.setAdapter(adapter);
                        },
                        e -> Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show(),
                        () -> {
                            System.out.println("Completed");
                        }
                );


//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//
//        mFirebaseDatabaseReference.child("chatmodel").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.getValue() != null){
//                    ChatModel model = dataSnapshot.getValue(ChatModel.class);
//                    messageList.add(model);
//                    adapter = new RecyclerFirebaseAdapter(getBaseContext(), userModel.getName(), messageList, chatActivity.this);
//                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                        @Override
//                        public void onItemRangeInserted(int positionStart, int itemCount) {
//                            super.onItemRangeInserted(positionStart, itemCount);
//                            int friendlyMessageCount = adapter.getItemCount();
//                            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                            if (lastVisiblePosition == -1 ||
//                                    (positionStart >= (friendlyMessageCount - 1) &&
//                                            lastVisiblePosition == (positionStart - 1))) {
//                                        rvListMessage.scrollToPosition(positionStart);
//                            }
//                        }
//                    });
//                    rvListMessage.setAdapter(adapter);
//                    if (model.getFile() != null){
//                        Log.d("File type", model.getFile().getType());
//                        Log.d("File url", model.getFile().getUrl_file());
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    /**
     * Verificar se usuario está logado
     */
    private void verificaUsuarioLogado(){
        mFirebaseAuth = FirebaseAuth.getInstance();

        String curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        if (curUser == null){
            startActivity(new Intent(this, loginActivity.class));
            finish();
        }else{
            FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        accountModel user = dataSnapshot.getValue(accountModel.class);
                        userModel = new UserModel(user.getName(), user.getPhoto_profile(), FirebaseAuth.getInstance().getCurrentUser().getUid() );
                        lerMessagensFirebase();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    /**
     * Vincular views com Java API
     */
    private void bindViews(){
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText)findViewById(R.id.editTextMessage);
        btSendMessage = (ImageView)findViewById(R.id.buttonMessage);
        btSendMessage.setOnClickListener(this);



        btEmoji = (ImageView)findViewById(R.id.buttonEmoji);
        emojIcon = new EmojIconActions(this,contentRoot,edMessage,btEmoji);
        emojIcon.ShowEmojIcon();
        rvListMessage = (RecyclerView)findViewById(R.id.messageRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        rvListMessage.setLayoutManager(mLinearLayoutManager);

        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
    /**
     * Sign Out no login
     */
    private void signOut(){
       // mFirebaseAuth.signOut();
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
       startActivity(new Intent(this, MainActivity.class));
       // finish();
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(chatActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    chatActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent();
        }
    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();
    public void sendMessage(final String title, final String body, final String icon, final String message) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("to", token);

                    String result = postToFCM(root.toString());

                    Log.d("1111111111111111", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
//                try {
//                    JSONObject resultJson = new JSONObject(result);
//                    int success, failure;
//                    success = resultJson.getInt("success");
//                    failure = resultJson.getInt("failure");
////                    Toast.makeText(getApplication(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
////                    Toast.makeText(getApplication(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
//                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=AAAASoPU9nw:APA91bFHXyo0Fn7UGDVQsHWJWB2GuGf3mTfWF5Ed2rm-Bq1mS3rfm0w0ABXdR50ZxRqMuxRKwyMTnP76MmOFG9YH7Oi9WqiRoOseMl6_3cZxJuaqBDGiEaaoNei8sNtZwjWtihXs_dWy")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    photoCameraIntent();
                }
                break;
        }
    }

    @Override
    public void clickImageChat(View view, int position, String nameUser, String urlPhotoUser, String urlPhotoClick) {
        Log.d("ClickImage", " TRUE ");
        Intent intent = new Intent(chatActivity.this, FullScreenImageActivity.class);
        intent.putExtra("nameUser", nameUser);
        intent.putExtra("urlPhotoUser", urlPhotoUser);
        intent.putExtra("urlPhotoClick", urlPhotoClick);
        startActivity(intent);
    }

    @Override
    public void clickVideoChat(View view, int position, String nameUser, String urlPhotoUser, String urlVideoClick) {
        Log.d("ClickVideo", " TRUE ");
        Intent intent = new Intent(chatActivity.this, FullScreenVideoActivity.class);
        intent.putExtra("nameUser", nameUser);
        intent.putExtra("urlPhotoUser", urlPhotoUser);
        intent.putExtra("urlVideoClick", urlVideoClick);
        startActivity(intent);
    }



    @Override
    public void clickImageMapChat(View view, int position, String latitude, String longitude) {
        String uri = String.format("geo:%s,%s?z=17&q=%s,%s", latitude,longitude,latitude,longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}