package com.example.likelok.company;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.likelok.R;
import com.example.likelok.adapter.CompanyLokerAdapter;
import com.example.likelok.models.Loker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyLokerFragment extends Fragment {

    private ImageButton ib_add;
    private ProgressBar loading;
    private TextView tv_total, tv_finish;
    private DatabaseReference databaseReference;
    private SharedPreferences pref;
    private String email, userId;
    private int total = 0, finished = 0;
    private Date currentTime = Calendar.getInstance().getTime();
    private RecyclerView rv_loker_company;
    private ArrayList<Loker> lokerList;
    private CompanyLokerAdapter adapter;

    public CompanyLokerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_loker, container, false);

        pref = getActivity().getSharedPreferences("likelok", Context.MODE_PRIVATE);
        email = pref.getString("email", null);
        databaseReference = FirebaseDatabase.getInstance().getReference("loker");

        ib_add = v.findViewById(R.id.ib_add);
        loading = v.findViewById(R.id.loading);

        tv_total = v.findViewById(R.id.tv_total);
        tv_finish = v.findViewById(R.id.tv_finish);

        rv_loker_company = v.findViewById(R.id.rv_loker_company);
        rv_loker_company.setHasFixedSize(true);
        rv_loker_company.setLayoutManager(new LinearLayoutManager(getContext()));


        loadData();

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateLokerActivity.class));
            }
        });

        return v;
    }

    private void loadData() {
        loading.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(userId)) {
            userId = databaseReference.push().getKey();
        }
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                lokerList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Loker loker = postSnapshot.getValue(Loker.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    lokerList.add(loker);

                    total++;
                }
                tv_total.setText(String.valueOf(total));
                //Log.d("OWYOWYWOWUYOWYWOYWOYW", lokerList.toString());
                adapter = new CompanyLokerAdapter(lokerList, getContext());
                rv_loker_company.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
