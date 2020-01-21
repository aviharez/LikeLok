package com.example.likelok.seeker;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likelok.DetailLokerActivity;
import com.example.likelok.R;
import com.example.likelok.adapter.CompanyLokerAdapter;
import com.example.likelok.adapter.RecyclerTouchListener;
import com.example.likelok.models.Loker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeekerSearchFragment extends Fragment {
    private ProgressBar loading;
    private EditText et_search;
    private ImageButton bt_search;
    private ImageView iv_start;
    private TextView tv_total;
    private LinearLayout ll_no_data;
    private RecyclerView rv_loker;
    private CompanyLokerAdapter adapter;
    private ArrayList<Loker> lokerList;
    private DatabaseReference databaseReference;
    private String userId;
    private int total = 0;


    public SeekerSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_search, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("loker");

        loading = v.findViewById(R.id.loading);
        et_search = v.findViewById(R.id.et_search);
        bt_search = v.findViewById(R.id.bt_search);
        iv_start = v.findViewById(R.id.iv_start);
        tv_total = v.findViewById(R.id.tv_total);
        ll_no_data = v.findViewById(R.id.ll_no_data);

        rv_loker = v.findViewById(R.id.rv_loker);
        rv_loker.setHasFixedSize(true);
        rv_loker.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_loker.setItemAnimator(new DefaultItemAnimator());

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Jangan dikosongin dong :(", Toast.LENGTH_LONG).show();
                } else {
                    loadData(et_search.getText().toString());
                }
            }
        });

        return v;
    }
    private void loadData(String keyword) {
        loading.setVisibility(View.VISIBLE);
        iv_start.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);
        if (TextUtils.isEmpty(userId)){
            userId = databaseReference.push().getKey();
        }
        Query query = databaseReference.orderByChild("nama_loker").startAt(keyword).endAt(keyword+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                total = 0;
                lokerList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Loker loker = postSnapshot.getValue(Loker.class);
                    lokerList.add(loker);
                    total++;
                }

                if (lokerList.isEmpty()) {
                    ll_no_data.setVisibility(View.VISIBLE);
                    tv_total.setText("Menampilkan " + total + " data");
                } else {
                    tv_total.setText("Menampilkan " + total + " data");
                    adapter = new CompanyLokerAdapter(lokerList, getContext());
                    rv_loker.setAdapter(adapter);
                    RecyclerTouchListener.addTo(rv_loker).setOnItemClickListener(new RecyclerTouchListener.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            Intent i = new Intent(getContext(), DetailLokerActivity.class);
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
