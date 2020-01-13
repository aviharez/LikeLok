package com.example.likelok.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.likelok.R;
import com.example.likelok.seeker.SeekerHomeFragment;
import com.example.likelok.seeker.SeekerProfileFragment;
import com.example.likelok.seeker.SeekerSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyMainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    initFragment(new CompanyHomeFragment());
                    return true;
                case R.id.item_search:
                    initFragment(new CompanySearchFragment());
                    return true;
                case R.id.item_upload:
                    initFragment(new CompanyLokerFragment());
                    return true;
                case R.id.item_profile:
                    initFragment(new CompanyProfileFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        initFragment(new CompanyHomeFragment());

    }

    private void initFragment(Fragment classFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, classFragment);
        transaction.commit();
    }
}
