package luck.materialdesign.tabsnavigator.tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import luck.materialdesign.tabsnavigator.MainActivity;
import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.loginActivity;
import luck.materialdesign.tabsnavigator.utils.adapter;

/**
 * Created by BeS on 29.09.2017.
 */

public class configFragment  extends Fragment {

    @BindView(R.id.btnSignOut)
    TextView btnSignOut;

    @BindView(R.id.btnRemove)
    TextView btnRemove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.config_layout, container, false);
        ButterKnife.bind(this, v);
        btnSignOut.setOnClickListener(f -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), loginActivity.class));
            getActivity().finish();
        });

        btnRemove.setOnClickListener(f -> {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("REMOVER UMA CONTA")
                    .setMessage("você tem certeza?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            getActivity().startActivity(new Intent(getActivity(), loginActivity.class));
                            String curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","(dot)");
                            FirebaseDatabase.getInstance().getReference("users").child(curUser).removeValue();
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
        return v;
    }
}