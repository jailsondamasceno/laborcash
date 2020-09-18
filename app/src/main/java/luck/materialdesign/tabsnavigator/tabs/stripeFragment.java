package luck.materialdesign.tabsnavigator.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;


import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BeS on 26.09.2017.
 */

public class stripeFragment extends Fragment{

    String token;
    String curUser;
    String freeChild;
    String projectUserChild;
    String project;
    String group;
    String payment;
    String projectName;
    Float comis;
    Float pay;
    private ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stripe_layout, container, false);


//        //STRIPE PAYMENT
        CardInputWidget mCardInputWidget = (CardInputWidget) v.findViewById(R.id.card_input_widget);
        TextView btnSend = (TextView)v.findViewById(R.id.btnSend);

        Bundle bundle = getArguments();
        freeChild = bundle.getString("freeChild");
        group = bundle.getString("group");
        project = bundle.getString("project");
        projectUserChild = bundle.getString("projectuserchild");
        projectName = bundle.getString("projectName");
        payment = bundle.getString("payment");
        pay = Float.parseFloat(payment);
        comis = Float.parseFloat(payment) / 11;
        dialog = new ProgressDialog(getActivity());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card = mCardInputWidget.getCard();
                dialog.setMessage("O pagamento é processado");
                dialog.show();
//                Card card = new Card("4242-4242-4242-4242", 12, 2018, "123");

                if (card == null) {
                    Toast.makeText(getContext(),
                            "Invalid Card Data",
                            Toast.LENGTH_LONG
                    ).show();

                }else{



                    Stripe.apiKey = "pk_test_7plGAQWyXyi31XuP4x6wzIpq";
                    RequestOptions requestOptions = RequestOptions.builder().setApiKey("sk_test_HISY3daS6LmEQDezdybCuyco").build();

                    com.stripe.android.Stripe stripe = new com.stripe.android.Stripe(getContext(), "pk_test_7plGAQWyXyi31XuP4x6wzIpq");

                     stripe.createToken(
                             card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
//                                    Toast.makeText(getContext(),
//                                            "Success: " + token.getCard().toString(),
//                                            Toast.LENGTH_LONG
//                                    ).show();


                                    // getTokenList().addToList(token);
                                    Log.e("Token Json", "::-" + token);
                                    final Map<String, Object> chargeParams = new HashMap<String, Object>();
                                    chargeParams.put("amount", (int)(pay*100));

                                    chargeParams.put("currency", "brl");
                                    chargeParams.put("card", token.getId());
                                    new AsyncTask<Void, Void, Void>() {

                                        Charge charge;

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();

                                        }

                                        @Override
                                        protected Void doInBackground(
                                                Void... params) {
                                            try {

                                                charge = Charge
                                                        .create(chargeParams, requestOptions);
                                                Log.d("Charge", String.valueOf(charge.getCreated()));
                                            } catch (Exception e) {
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Toast.makeText(getContext(),
                                                        "ERROR: " + e.toString(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                                Log.d("Charge", String.valueOf(e));
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                // showAlert("Exception while charging the card!",
                                                // e.getLocalizedMessage());
                                            }
                                            return null;
                                        }

                                        protected void onPostExecute(Void result) {
//                                            Toast.makeText(
//                                                    getContext(),
//                                                    "Card Charged : "
//                                                            + charge.getCreated()
//                                                            + "\nPaid : "
//                                                            + charge.getPaid(),
//                                                    Toast.LENGTH_LONG).show();

                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                            finishProject();
                                            Log.d("PAYMENT", "CREATE");
                                        }

                                        ;

                                    }.execute();
                                }


                                public void onError(Exception error) {
                                    // Show localized error message

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(getContext(),
                                            "ERROR: " + error.toString(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                }
                            }
                    );




                }
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


    public void finishProject(){
        String curChild = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        FirebaseDatabase.getInstance().getReference("projects").child("in progress").child(group).child(project).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    FirebaseDatabase.getInstance().getReference("projects").child("finished").child(group).child(project).setValue(dataSnapshot.getValue());
                    FirebaseDatabase.getInstance().getReference("projects").child("finished").child(group).child(project).child("bid_count").removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(curChild).child("projects").child(project).child("status").setValue("finished");
                    FirebaseDatabase.getInstance().getReference("projects").child("in progress").child(group).child(project).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(freeChild).child("freelancer").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                accountFreeModel freeAcc = dataSnapshot.getValue(accountFreeModel.class);
                                Float budger = Float.parseFloat(freeAcc.getBudget()) + (Float.parseFloat(payment) - comis);
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).child("freelancer").child("budget").setValue(String.valueOf(budger));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference("users").child(freeChild).child("freeprojects").child(project).child("status").setValue("finished");
                    FirebaseDatabase.getInstance().getReference("users").child(curChild).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                accountModel curUser = dataSnapshot.getValue(accountModel.class);
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null){
                                            accountModel freeAcc = dataSnapshot.getValue(accountModel.class);
                                            token = freeAcc.getToken();
                                            sendMessage("Brand", curUser.getName() + " concluiu o projeto " + projectName, "", "");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("users").child(freeChild).child("notification").child(project).setValue(new notificationModel(projectName, curChild, curUser.getName(), curUser.getPhoto_profile(), group, "finishedFreelancer"));
                                FirebaseDatabase.getInstance().getReference("users").child(curChild).child("notification").child(project).setValue(new notificationModel(projectName, curChild, curUser.getName(), curUser.getPhoto_profile(), group, "finishedClient"));
                                Toast.makeText(getContext(), "Vá em notificações para avaliar o projeto", Toast.LENGTH_LONG).show();



                                FragmentTransaction fManager;
                                Bundle bundle = new Bundle();
                                bundle.putString("type", "client");
                                tabsFragment tabsFragment = new tabsFragment();
                                tabsFragment.setArguments(bundle);
                                fManager = getFragmentManager().beginTransaction();
                                fManager.replace(R.id.mainFrame, tabsFragment);
                                fManager.commit();
                                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("COMO CLIENTE");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


}
