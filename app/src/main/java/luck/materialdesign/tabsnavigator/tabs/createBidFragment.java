package luck.materialdesign.tabsnavigator.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.model.bidModel;

/**
 * Created by BeS on 15.09.2017.
 */

public class createBidFragment  extends Fragment {

    @BindView(R.id.bids)
    TextView bids;

    @BindView(R.id.budget)
    TextView budget;

    @BindView(R.id.editPrice)
    EditText editPrice;

    @BindView(R.id.editComis)
    EditText editComis;

    @BindView(R.id.editTotal)
    EditText editTotal;

    @BindView(R.id.btnEnviar)
    TextView btnEnviar;

    @BindView(R.id.editMessage)
    EditText editMessage;

    private FragmentTransaction fManager;
    accountModel user;
    Bundle bundle;
    String curUser;
    String price;
    String bidCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_bid_layout, container, false);
        ButterKnife.bind(this, v);

        bundle = getArguments();
        price = bundle.getString("price");
        bidCount = bundle.getString("bidCount");


        if (bidCount == null){
            bids.setText("0");
        }else {
            bids.setText(bidCount);
        }
        budget.setText(price);

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    Double comis = Double.parseDouble(String.valueOf(s)) * 0.1;
                    editComis.setText(String.format(String.valueOf(comis), ".%1d"));
                    editTotal.setText(String.valueOf(Double.parseDouble(String.valueOf(s)) + comis));
                }else{
                    editComis.setText("");
                    editTotal.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
        btnEnviar.setOnClickListener(f -> {

            FirebaseDatabase.getInstance().getReference("users").child(curUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        try{
                        user = dataSnapshot.getValue(accountModel.class);



                            if (editPrice.getText().length()>0) {

                                FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            FirebaseDatabase.getInstance().getReference("projects").child("open").child(bundle.getString("group")).child(bundle.getString("project")).child("bid").child(curUser).setValue(new bidModel(user.getName(), editTotal.getText().toString(), editMessage.getText().toString(), user.getPhoto_profile()));
                                            getFragmentManager().popBackStack();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }catch (NullPointerException e){

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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

}
