package com.example.likelok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.likelok.models.Company;
import com.example.likelok.models.Loker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailLokerActivity extends AppCompatActivity {

    private ImageView bt_back, iv_loker;
    private TextView tv_item_name, tv_office_name,
            tv_address, tv_salary, tv_item_description,
            tv_type_job, tv_lowongan, tv_type_education,
            tv_exp_date, tv_type_industri;
    private SharedPreferences pref;
    private String email, userId;
    private DatabaseReference databaseReference;
    private Loker loker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loker);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);
        bt_back = findViewById(R.id.bt_back);
        iv_loker = findViewById(R.id.iv_loker);
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_office_name = findViewById(R.id.tv_office_name);
        tv_address = findViewById(R.id.tv_address);
        tv_salary = findViewById(R.id.tv_salary);
        tv_item_description = findViewById(R.id.tv_item_description);
        tv_type_job = findViewById(R.id.tv_type_job);
        tv_lowongan = findViewById(R.id.tv_lowongan);
        tv_type_education = findViewById(R.id.tv_type_education);
        tv_exp_date = findViewById(R.id.tv_exp_date);
        tv_type_industri = findViewById(R.id.tv_type_industri);

        databaseReference = FirebaseDatabase.getInstance().getReference("company_profile");
        loker = getIntent().getParcelableExtra("data_loker");

        loadData();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bt_back.bringToFront();


    }

    private void loadData() {
        if (!loker.getPic().equals("-"))
            Glide.with(getApplicationContext()).load(loker.getPic()).into(iv_loker);

        tv_item_name.setText(loker.getNama_loker());

        tv_salary.setText(loker.getGaji());
        tv_item_description.setText(loker.getDeskripsi());
        tv_type_job.setText(loker.getJenis());
        tv_lowongan.setText(loker.getLowongan());
        tv_type_education.setText(loker.getTingkat());
        tv_exp_date.setText(loker.getDeadline());

        Query query = databaseReference.orderByChild("email").equalTo(loker.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Company cp = postSnapshot.getValue(Company.class);
                    tv_office_name.setText(cp.getName());
                    tv_address.setText(cp.getAlamat());
                    tv_type_industri.setText(cp.getIndustri());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
