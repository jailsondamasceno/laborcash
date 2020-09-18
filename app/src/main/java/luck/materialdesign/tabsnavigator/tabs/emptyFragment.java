package luck.materialdesign.tabsnavigator.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.common.ErrorDialogFragment;
//import com.stripe.Stripe;
//import com.stripe.android.TokenCallback;
//import com.stripe.android.model.Card;
//import com.stripe.android.model.Token;
//import com.stripe.android.view.CardInputWidget;
//import com.stripe.model.Charge;
//import com.stripe.net.RequestOptions;


import java.util.HashMap;
import java.util.Map;

import luck.materialdesign.tabsnavigator.R;

import luck.materialdesign.tabsnavigator.activities.ParalaxToobarActivity;
import luck.materialdesign.tabsnavigator.activities.freelancerAccount;
import luck.materialdesign.tabsnavigator.utils.adapter;
import luck.materialdesign.tabsnavigator.utils.album;


public class emptyFragment extends Fragment implements adapter.OnClick{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stripe_layout, container, false);


        //STRIPE PAYMENT

//        View v = inflater.inflate(R.layout.stripe_layout, container, false);
//        CardInputWidget mCardInputWidget = (CardInputWidget) v.findViewById(R.id.card_input_widget);
//
//        TextView btnSend = (TextView)v.findViewById(R.id.btnSend);

////        btnSend.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Card card = mCardInputWidget.getCard();
//                Card card = new Card("4242-4242-4242-4242", 12, 2018, "123");
//
////                if (card == null) {
////                    Toast.makeText(getContext(),
////                            "Invalid Card Data",
////                            Toast.LENGTH_LONG
////                    ).show();
////
////                }else{
//
//
//
//                    Stripe.apiKey = "pk_test_7plGAQWyXyi31XuP4x6wzIpq";
//                    RequestOptions requestOptions = RequestOptions.builder().setApiKey("sk_test_HISY3daS6LmEQDezdybCuyco").build();
//
//                    com.stripe.android.Stripe stripe = new com.stripe.android.Stripe(getContext(), "pk_test_7plGAQWyXyi31XuP4x6wzIpq");
//
//                     stripe.createToken(
//                             card,
//                            new TokenCallback() {
//                                public void onSuccess(Token token) {
//                                    // Send token to your server
//                                    Toast.makeText(getContext(),
//                                            "Success: " + token.getCard().toString(),
//                                            Toast.LENGTH_LONG
//                                    ).show();
//
//
//                                    // getTokenList().addToList(token);
//                                    Log.e("Token Json", "27th March::-" + token);
//                                    final Map<String, Object> chargeParams = new HashMap<String, Object>();
//                                    chargeParams.put("amount", 250);
//                                    chargeParams.put("currency", "brl");
//                                    chargeParams.put("card", token.getId());
//                                    new AsyncTask<Void, Void, Void>() {
//
//                                        Charge charge;
//
//                                        @Override
//                                        protected Void doInBackground(
//                                                Void... params) {
//                                            try {
//
//                                                charge = Charge
//                                                        .create(chargeParams, requestOptions);
//                                                Log.d("Charge", String.valueOf(charge.getCreated()));
//                                            } catch (Exception e) {
//                                                Log.d("Charge", String.valueOf(e));
//                                                // TODO Auto-generated catch block
//                                                e.printStackTrace();
//                                                // showAlert("Exception while charging the card!",
//                                                // e.getLocalizedMessage());
//                                            }
//                                            return null;
//                                        }
//
//                                        protected void onPostExecute(Void result) {
////                                            Toast.makeText(
////                                                    getContext(),
////                                                    "Card Charged : "
////                                                            + charge.getCreated()
////                                                            + "\nPaid : "
////                                                            + charge.getPaid(),
////                                                    Toast.LENGTH_LONG).show();
//
//                                            Log.d("PAYMENT", "CREATE");
//                                        }
//
//                                        ;
//
//                                    }.execute();
//                                }
//
//
//                                public void onError(Exception error) {
//                                    // Show localized error message
//                                    Toast.makeText(getContext(),
//                                            "ERROR: " + error.toString(),
//                                            Toast.LENGTH_LONG
//                                    ).show();
//                                }
//                            }
//                    );
//
//
//
//
////                }
////            }
////        });

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
        Log.d("Icon", String.valueOf(icon));
        Log.d("Name", name);
//        Bundle bundle = new Bundle();
//        bundle.putInt("icon", icon);
//        bundle.putString("name", name);

//        Intent intent = new Intent(context, descriptionFragment.class);
//        intent.putExtra("icon", icon);
//        context.startActivity(intent);

//        tabsFragment tabsFragment = new tabsFragment();
//        pushFragment pushFragment = new pushFragment();
//        pushFragment.setArguments(bundle);
//        fTrans.remove(tabsFragment);
//        fTrans.replace(R.id.mainFrame, pushFragment);
//        fTrans.addToBackStack("push");
//        fTrans.commit();
    }



}
