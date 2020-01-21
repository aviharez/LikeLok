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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.likelok.DetailLokerActivity;
import com.example.likelok.R;
import com.example.likelok.adapter.CompanyAdapter;
import com.example.likelok.adapter.CompanyLokerAdapter;
import com.example.likelok.adapter.RecyclerTouchListener;
import com.example.likelok.models.Company;
import com.example.likelok.models.Loker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyHomeFragment extends Fragment {

    private ProgressBar loading;
    private DatabaseReference drLoker;
    private DatabaseReference drCompany;
    private SharedPreferences pref;
    private String email, userId;
    private RecyclerView rv_loker, rv_company;
    private ArrayList<Loker> lokerList;
    private ArrayList<Company> companyList;
    private CompanyLokerAdapter adapterLoker;
    private CompanyAdapter adapterCompany;



    public CompanyHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_company_home, container, false);
        pref = getActivity().getSharedPreferences("likelok", Context.MODE_PRIVATE);
        drLoker = FirebaseDatabase.getInstance().getReference("loker");
        drCompany = FirebaseDatabase.getInstance().getReference("company_profile");

        loading = v.findViewById(R.id.loading);
        rv_loker = v.findViewById(R.id.rv_loker);
        rv_loker.setHasFixedSize(true);
        rv_loker.setLayoutManager(new LinearLayoutManager(getContext()));
        //rv_loker.setNestedScrollingEnabled(false);

        rv_company = v.findViewById(R.id.rv_companies);
        rv_company.setHasFixedSize(true);
        rv_company.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //rv_company.setNestedScrollingEnabled(false);

        loading.setVisibility(View.VISIBLE);
        loadData();

        return v;
    }

    private void loadData() {
        if (TextUtils.isEmpty(userId)){
            userId = drCompany.push().getKey();
        }
        drCompany.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companyList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Company company = postSnapshot.getValue(Company.class);
                    companyList.add(company);

                }
                adapterCompany = new CompanyAdapter(companyList, getContext());
                rv_company.setAdapter(adapterCompany);
                RecyclerTouchListener.addTo(rv_company).setOnItemClickListener(new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getContext(), CompanyProfileActivity.class);
                        //i.putExtra("pos", (Parcelable) list.get(position));

                        startActivity(i);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        drLoker.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                lokerList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Loker loker = postSnapshot.getValue(Loker.class);
                    lokerList.add(loker);
                }
                adapterLoker = new CompanyLokerAdapter(lokerList, getContext());
                rv_loker.setAdapter(adapterLoker);
                RecyclerTouchListener.addTo(rv_loker).setOnItemClickListener(new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getContext(), DetailLokerActivity.class);
                        i.putExtra("data_loker", lokerList.get(position));
                        i.putExtra("type", "company");
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
