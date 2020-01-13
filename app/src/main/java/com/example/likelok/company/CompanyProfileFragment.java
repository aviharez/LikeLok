package com.example.likelok.company;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.likelok.R;
import com.example.likelok.WelcomeActivity;
import com.example.likelok.models.CompanyProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyProfileFragment extends Fragment {

    private LinearLayout bt_edit_profile, bt_logout;
    private TextView tv_name, tv_email, tv_industri, tv_tlp, tv_jml_pegawai, tv_alamat;
    private ProgressBar loading;
    private CircleImageView profile_pic;
    private DatabaseReference databaseReference;
    private SharedPreferences pref;
    private String email, userId;


    public CompanyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_profile, container, false);

        pref = getActivity().getSharedPreferences("likelok", Context.MODE_PRIVATE);
        email = pref.getString("email", null);
        databaseReference = FirebaseDatabase.getInstance().getReference("company_profile");

        bt_edit_profile = v.findViewById(R.id.bt_edit_profile);
        bt_logout = v.findViewById(R.id.logout);
        profile_pic = v.findViewById(R.id.profile_image);

        tv_name = v.findViewById(R.id.tv_name);
        tv_email = v.findViewById(R.id.tv_email);
        tv_alamat = v.findViewById(R.id.tv_alamat);
        tv_industri = v.findViewById(R.id.tv_industri);
        tv_tlp = v.findViewById(R.id.tv_tlp);
        tv_jml_pegawai = v.findViewById(R.id.tv_jml_pegawai);

        loading = v.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        loadData();

        bt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CompanyProfilEditActivity.class));
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();
                                Intent i = new Intent(getContext(), WelcomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda yakin untuk logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return v;
    }

    private void loadData() {

        if (TextUtils.isEmpty(userId)) {
            userId = databaseReference.push().getKey();
        }
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CompanyProfile companyProfile = postSnapshot.getValue(CompanyProfile.class);

                    if (!companyProfile.getPic().equals("-")) {
                        Glide.with(getContext()).load(companyProfile.getPic()).into(profile_pic);
                    }

                    tv_name.setText(companyProfile.getName());
                    tv_email.setText(companyProfile.getEmail());
                    tv_industri.setText(companyProfile.getIndustri());
                    tv_tlp.setText(companyProfile.getTelepon());
                    tv_jml_pegawai.setText(companyProfile.getTelepon());
                    tv_alamat.setText(companyProfile.getAlamat());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
