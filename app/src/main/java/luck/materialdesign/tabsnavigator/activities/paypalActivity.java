package luck.materialdesign.tabsnavigator.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountFreeModel;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.notificationModel;
import luck.materialdesign.tabsnavigator.model.userProjectModel;
import luck.materialdesign.tabsnavigator.tabs.tabsFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by BeS on 22.09.2017.
 */

public class paypalActivity   extends AppCompatActivity {

    // private static final String TAG = "paymentdemoblog";
    /**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    // private static final String CONFIG_ENVIRONMENT =
    // PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;


    String token;
    String curUser;
    String freeChild;
    String projectUserChild;
    String project;
    String group;
    String payment;
    String projectName;

    SharedPreferences sPref;
    Float comis;
    // note that these credentials will differ between live & sandbox
    // environments.
    private final String CONFIG_CLIENT_ID = "AccZcMwylDI4wJ9lvuGZ2W0RDLRDKECDg4sXM3Hz4uwdLVHeopRrCFuNHOCK7hrVVn30zrj3AfE5VC19";

    private final int REQUEST_CODE_PAYMENT = 1;
    private final int REQUEST_CODE_PAYMENT2 = 3;
    private final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    Intent service;
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(CONFIG_ENVIRONMENT)
//            .clientId(CONFIG_CLIENT_ID)
//
//    // the following are only used in PayPalFuturePaymentActivity.
//            .merchantName("Hipster Store")
//            .merchantPrivacyPolicyUri(
//            Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(
//            Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_layout);

        payment = getIntent().getStringExtra("payment");
        group = getIntent().getStringExtra("group");
        project = getIntent().getStringExtra("project");
        projectUserChild = getIntent().getStringExtra("projectuserchild");
        freeChild = getIntent().getStringExtra("freeChild");
        projectName = getIntent().getStringExtra("projectName");


        comis = Float.parseFloat(payment) / 11;

                            PayPalConfiguration config = new PayPalConfiguration()
                                    .environment(CONFIG_ENVIRONMENT)
                                    .clientId(CONFIG_CLIENT_ID)
                                    .merchantName("Hipster Store")
                                    .merchantPrivacyPolicyUri(
                                            Uri.parse("https://www.example.com/privacy"))
                                    .merchantUserAgreementUri(
                                            Uri.parse("https://www.example.com/legal"));
                            service = new Intent(getBaseContext(), PayPalService.class);
                            service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                            startService(service);


                            thingToBuy = new PayPalPayment(new BigDecimal(payment), "BRL",
                                    "A pagar", PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent2 = new Intent(paypalActivity.this,
                                    PaymentActivity.class);

//                thingToBuy.payeeEmail("beskishev-facilitator@yandex.ru");
                            intent2.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                            startActivityForResult(intent2, REQUEST_CODE_PAYMENT);



    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(paypalActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));

                        Toast.makeText(getApplicationContext(), "Order placed",
                                Toast.LENGTH_LONG).show();

                        stopService(new Intent(this, PayPalService.class));

                        finishProject();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
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



    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

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
                                Toast.makeText(getBaseContext(), "Vá em notificações para avaliar o projeto", Toast.LENGTH_LONG).show();
                                finish();
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

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}